package com.takeya.seatingsplanner.model;

import java.util.concurrent.atomic.AtomicInteger;

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

    private static AtomicInteger INTEGER_COUNTER = new AtomicInteger(0);

    @PrimaryKey
    private int id;
    @Required
    private String surename;
    @Required
    private String name;

    @Ignore
    private int reservedTableId;

    public Customer(String surename, String name, int id) {
        this.surename = surename;
        this.name = name;
        this.id = id;
    }

    static void create(Realm realm) {
        Customers customers = realm.where(Customers.class).findFirst();
        RealmList<Customer> items = customers.getCustomersList();
        Customer customer = realm.createObject(Customer.class, increment());
        items.add(customer);
    }

    private static int increment() {
        return INTEGER_COUNTER.getAndIncrement();
    }

    public String getSurename() {
        return surename;
    }

    public String getName() {
        return name;
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
