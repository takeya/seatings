package com.takeya.seatingsplanner;

import android.app.Application;

import com.takeya.seatingsplanner.model.Customers;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Takeya on 19.11.2017.
 */

public class SeatingsPlannerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(Customers.class);
                    }})
                .build();
        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }
}
