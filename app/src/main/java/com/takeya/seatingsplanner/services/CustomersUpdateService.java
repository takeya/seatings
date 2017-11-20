package com.takeya.seatingsplanner.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Takeya on 20.11.2017.
 */

public class CustomersUpdateService extends IntentService {
    private static final Logger LOGGER = Logger.getLogger(CustomersUpdateService.class);

    public static final String UPDATE_CUSTOMERS_INTENT = "update_customers";
    public static final String CUSTOMERS_URL
            = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/customer-list.json";

    public CustomersUpdateService() {
        super("CustomerUpdater");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.hasExtra(UPDATE_CUSTOMERS_INTENT)) {
            retrieveCustomerJson();
        }
    }

    private void retrieveCustomerJson() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(CUSTOMERS_URL)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String customersJson = response.body().string();

        } catch (IOException e) {
            LOGGER.error("Could not establish connection to server");
        }
    }
}
