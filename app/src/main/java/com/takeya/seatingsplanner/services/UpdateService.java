package com.takeya.seatingsplanner.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.takeya.seatingsplanner.model.Customer;
import com.takeya.seatingsplanner.model.Table;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.exceptions.RealmException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Updates Customers and Tables from Server once
 * can be forces to update with Extra UpdateService.FORCE_UPDATE_EXTRA
 *
 * Created by Takeya on 20.11.2017.
 */

public class UpdateService extends IntentService {
    public static final String TAG = UpdateService.class.getName();

    private static final String CUSTOMERS_JSON_FILE_NAME = "customers.json";
    private static final String TABLES_JSON_FILE_NAME = "tables.json";

    public static final String UPDATE_CUSTOMERS_ACTION = "update_customers";
    public static final String UPDATE_TABLES_ACTION = "update_tables";
    public static final String UPDATE_TABLES_AND_CUSTOMERS_ACTION = "update_both";
    public static final String FORCE_UPDATE_EXTRA = "force_update";

    public static final String CUSTOMERS_URL
            = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/customer-list.json";
    public static final String TABLES_URL
            = " https://s3-eu-west-1.amazonaws.com/quandoo-assessment/table-map.json";

    private static final String UPDATES_DOWNLOADED_PREF_KEY = "updates_downloaded";

    public UpdateService() {
        super("CustomerUpdater");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean updatesDownloaded = preferences.getBoolean(UPDATES_DOWNLOADED_PREF_KEY, false);
        //updates already pulled. forcedto update?
        if (updatesDownloaded && !intent.hasExtra(FORCE_UPDATE_EXTRA)) {
            return;
        }
        String intentAction = intent.getAction();
        if (intentAction.equals(UPDATE_CUSTOMERS_ACTION) ||
                intentAction.equals(UPDATE_TABLES_AND_CUSTOMERS_ACTION)) {
            updatesCustomers();
        }
        if (intentAction.equals(UPDATE_TABLES_ACTION) ||
                intentAction.equals(UPDATE_TABLES_AND_CUSTOMERS_ACTION)) {
            updateTables();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(UPDATES_DOWNLOADED_PREF_KEY, true);
        editor.apply();
    }

    private void updatesCustomers() {
        JSONArray customersJson = retrieveJson(CUSTOMERS_URL);
        if (customersJson.length() != 0) {
            saveOrUpdateCustomerToDB(customersJson);
            saveJsonLocally(customersJson, CUSTOMERS_JSON_FILE_NAME);
        }
    }

    private void updateTables() {
        JSONArray tablesJson = retrieveJson(TABLES_URL);
        if (tablesJson.length() != 0) {
            saveOrUpdateTableToDB(tablesJson);
            saveJsonLocally(tablesJson, TABLES_JSON_FILE_NAME);
        }
    }

    public JSONArray retrieveJson(String url) {
        JSONArray customersJson = new JSONArray();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response != null && response.body() != null) {
                customersJson = new JSONArray(response.body().string());
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not establish connection to server");
        } catch (JSONException e) {
            Log.e(TAG, "Could not parse JSON");
        }
        return customersJson;
    }

    private void saveOrUpdateCustomerToDB(JSONArray customersJson) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Customer.class, customersJson);
            realm.commitTransaction();
        } catch (RealmException e) {
            Log.e(TAG, "Could not save Customer to realm DB", e);
        }
    }

    private void saveOrUpdateTableToDB(JSONArray tablesJson) {
        try {
            for (int i = 0; i < tablesJson.length(); i++) {
                Table.createOrUpdateTable(i, tablesJson.getBoolean(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Tables json is malformed.", e);
        }
    }

    private void saveJsonLocally(JSONArray customersJson, String fileName) {
        try (FileOutputStream outputStream
                     = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            outputStream.write(customersJson.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing file to internal file store");
        }
    }
}
