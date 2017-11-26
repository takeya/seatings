package com.takeya.seatingsplanner.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.exceptions.RealmException;

/**
 * Table class created for easier extension at some point later
 * e.g. additional time slots
 *
 * Created by Takeya on 19.11.2017.
 */
public class Table extends RealmObject {
    private static final String TAG = Table.class.getName();
    public static final int NO_CUSTOMER = -1;

    @PrimaryKey
    private int id;

    private int reservedByCustomerId = NO_CUSTOMER;

    private boolean free;

    public Table() {}

    public Table(int id, boolean free) {
        this.id = id;
        this.free = free;
    }

    public int getId() {
        return id;
    }

    public static void createOrUpdateTable(int id, boolean free){
        try(Realm realm = Realm.getDefaultInstance()) {
            Table table = new Table(id, free);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(table);
            realm.commitTransaction();
        } catch(RealmException e){
            Log.e(TAG, "Could not save or update table to DB");
        }
    }

    public int getReservedByCustomerId() {
        return reservedByCustomerId;
    }

    public boolean isReserved() {
        return !free;
    }

    public void setReservedByCustomerId(int reservedByCustomerId) {
        free = reservedByCustomerId == NO_CUSTOMER;
        this.reservedByCustomerId = reservedByCustomerId;
    }
}
