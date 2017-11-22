package com.takeya.seatingsplanner.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Table class created for easier extension at some point later
 * e.g. additional time slots
 *
 * Created by Takeya on 19.11.2017.
 */
public class Table extends RealmObject {

    @PrimaryKey
    private int id;

    private int reservedByCustomerId;

    private boolean reserved;

}
