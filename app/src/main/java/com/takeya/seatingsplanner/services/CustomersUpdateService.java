package com.takeya.seatingsplanner.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.takeya.seatingsplanner.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Takeya on 20.11.2017.
 */

public class CustomersUpdateService extends IntentService {
    public static final String TAG = CustomersUpdateService.class.getName();

    private static final String CUSTOMERS_JSON_FILE_NAME = "customers.json";

    public static final String UPDATE_CUSTOMERS_INTENT = "update_customers";
    public static final String CUSTOMERS_URL
            = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/customer-list.json";

    public CustomersUpdateService() {
        super("CustomerUpdater");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null || intent.getAction() == null ||
                !intent.getAction().equals(UPDATE_CUSTOMERS_INTENT)) {
            return;
        }
        JSONArray customersJson = retrieveCustomerJson();
        if(customersJson.length() != 0) {
            saveOrUpdateToDB(customersJson);
            saveJsonLocally(customersJson);
        }
    }

    public JSONArray retrieveCustomerJson() {
        JSONArray customersJson = new JSONArray();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(CUSTOMERS_URL)
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

    private void saveOrUpdateToDB(JSONArray customersJson) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            realm.createOrUpdateAllFromJson(Customer.class, customersJson);
            realm.commitTransaction();
        }
    }

    private void saveJsonLocally(JSONArray customersJson) {
        try (FileOutputStream outputStream
                     = openFileOutput(CUSTOMERS_JSON_FILE_NAME, Context.MODE_PRIVATE)) {
            outputStream.write(customersJson.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Error writing file to internal file store");
        }
    }
}
