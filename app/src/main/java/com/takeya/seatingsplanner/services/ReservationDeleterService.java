package com.takeya.seatingsplanner.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.takeya.seatingsplanner.model.Customer;
import com.takeya.seatingsplanner.model.Table;

import java.util.List;

import io.realm.Realm;
import io.realm.exceptions.RealmException;

/**
 * Deletes all reservations with a given interval
 *
 * Created by Takeya on 25.11.2017.
 */

public class ReservationDeleterService extends IntentService{

    private static final String TAG = ReservationDeleterService.class.getName();

    public ReservationDeleterService() {
        super("ReservationDeleterService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try(Realm realm = Realm.getDefaultInstance()) {
            realm.beginTransaction();
            List<Table> tables = realm.where(Table.class).findAll();
            for (Table table : tables) {
                table.setReservedByCustomerId(Table.NO_CUSTOMER);
            }
            List<Customer> customers = realm.where(Customer.class).findAll();
            for (Customer customer : customers) {
                customer.setReservedTableId(Customer.NO_RESERVED_TABLE);
            }
            realm.commitTransaction();
        } catch(RealmException e){
            Log.e(TAG, "could not remove all reservations from DB" );
        }
    }
}
