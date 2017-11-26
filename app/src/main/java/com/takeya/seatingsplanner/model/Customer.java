package com.takeya.seatingsplanner.model;

import io.realm.MutableRealmInteger;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Takeya on 19.11.2017.
 */

public class Customer extends RealmObject {

    public static final String FIELD_ID = "id";
    public static final int NO_RESERVED_TABLE = -1;

    private static final MutableRealmInteger customerCounter = MutableRealmInteger.valueOf(0);

    @PrimaryKey
    private int id;
    @Required
    private String customerFirstName;
    @Required
    private String customerLastName;

    private int reservedTableId = NO_RESERVED_TABLE;

    public Customer(){

    }

    public Customer(String firstName, String lastName, int id) {
        this.customerFirstName = firstName;
        this.customerLastName = lastName;
        this.id = id;
    }

    static Customer create(Realm realm) {
        customerCounter.set(realm.where(Customer.class).count());
        customerCounter.increment(1);
        realm.beginTransaction();
        Customer customer = realm.createObject(Customer.class, customerCounter.get());
        realm.commitTransaction();
        return customer;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public int getId() {
        return id;
    }

    public void setReservedTableId(int reservedTableId) {
        this.reservedTableId = reservedTableId;
    }

    public int getReservedTableId() {
        return reservedTableId;
    }
}
