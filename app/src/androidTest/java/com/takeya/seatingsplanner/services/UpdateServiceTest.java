package com.takeya.seatingsplanner.services;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Takeya on 21.11.2017.
 */
public class UpdateServiceTest {

    @Test
    public void retrieveCustomersJson() throws Exception {
        UpdateService cus = new UpdateService();
        JSONArray result = cus.retrieveCustomerJson();
        Assert.assertTrue(result.length() != 0);
    }

}