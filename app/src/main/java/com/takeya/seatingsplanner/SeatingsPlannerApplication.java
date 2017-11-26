package com.takeya.seatingsplanner;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


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

        /*DEBUG entry - comment in while debugging to remove all entries after app restart*/
//        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
//        SharedPreferences preferences = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
//        preferences.edit().clear().commit();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
