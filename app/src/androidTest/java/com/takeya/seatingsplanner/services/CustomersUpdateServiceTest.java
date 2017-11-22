package com.takeya.seatingsplanner.services;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Takeya on 21.11.2017.
 */
public class CustomersUpdateServiceTest {

    @Test
    public void retrieveCustomersJson() throws Exception {
        CustomersUpdateService cus = new CustomersUpdateService();
        JSONArray result = cus.retrieveCustomerJson();
        Assert.assertTrue(result.length() != 0);
    }

}