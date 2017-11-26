package com.takeya.seatingsplanner.services;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import static com.takeya.seatingsplanner.services.UpdateService.CUSTOMERS_URL;
import static com.takeya.seatingsplanner.services.UpdateService.TABLES_URL;

/**
 * Created by Takeya on 21.11.2017.
 */
public class UpdateServiceTest {

    @Test
    public void retrieveCustomersJson() throws Exception {
        UpdateService updateService = new UpdateService();
        JSONArray result = updateService.retrieveJson(CUSTOMERS_URL);
        Assert.assertTrue(result.length() != 0);
    }

    @Test
    public void retrieveTablesJson() throws Exception {
        UpdateService updateService = new UpdateService();
        JSONArray result = updateService.retrieveJson(TABLES_URL);
        Assert.assertTrue(result.length() != 0);
    }

}