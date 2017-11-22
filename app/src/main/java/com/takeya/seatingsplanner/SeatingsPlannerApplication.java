package com.takeya.seatingsplanner;

import android.app.Application;


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
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);
    }
}
