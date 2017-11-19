package com.takeya.seatingsplanner.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Takeya on 19.11.2017.
 */

public class Customers extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<Customer> customersList;

    public RealmList<Customer> getCustomersList() {
        return customersList;
    }
}
