package com.takeya.seatingsplanner.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.takeya.seatingsplanner.R;
import com.takeya.seatingsplanner.services.ReservationDeleterService;
import com.takeya.seatingsplanner.services.UpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_container)
    FrameLayout mainContainer;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        if (savedInstanceState != null) {
            return;
        }
        updateFromServer();
        CustomerListFragment customerListFragment = new CustomerListFragment();
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        customerListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, customerListFragment).commit();

        setUpRepeatingAlarm();
    }

    private void updateFromServer() {
        Intent updateCustomersAndTables = new Intent(this, UpdateService.class);
        updateCustomersAndTables.setAction(UpdateService.UPDATE_TABLES_AND_CUSTOMERS_ACTION);
        getApplicationContext().startService(updateCustomersAndTables);
    }

    /**
     * Sets a repeating alarm to remove all reservations every 15 minutes
     */
    private void setUpRepeatingAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ReservationDeleterService.class);
        PendingIntent alarmIntent = PendingIntent
                .getService(getApplicationContext(), 0, intent, 0);
        if (alarmMgr != null) {
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    alarmIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
