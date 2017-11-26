package com.takeya.seatingsplanner.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.takeya.seatingsplanner.R;
import com.takeya.seatingsplanner.adapters.TableOverviewAdapter;
import com.takeya.seatingsplanner.model.Customer;
import com.takeya.seatingsplanner.model.Table;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.exceptions.RealmException;

import static com.takeya.seatingsplanner.model.Customer.NO_RESERVED_TABLE;
import static com.takeya.seatingsplanner.model.Table.NO_CUSTOMER;

/**
 * Created by Takeya on 23.11.2017.
 */

public class TableOverviewFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = TableOverviewFragment.class.getName();
    public static final String CUSTOMER_ID = "customerId";
    private int currentCustomerId;

    @BindView(R.id.fragment_table_overview_grid)
    public GridView gridView;

    private TableOverviewAdapter tableAdapter;

    private Unbinder unbinder;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        Bundle arguments = getArguments();
        if (arguments == null) {
            Log.e(TAG, "current Customer Id is not available");
            throw new NullPointerException();
        }
        currentCustomerId = arguments.getInt(CUSTOMER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_overview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        if (tableAdapter == null) {
            try (Realm realm = Realm.getDefaultInstance()) {
                List<Table> tables = realm.where(Table.class).findAll();

                //This is the GridView adapter
                tableAdapter = new TableOverviewAdapter(getActivity().getApplicationContext(),
                        currentCustomerId);
                tableAdapter.setData(tables);
                //This is the GridView which will display the list of cities
                gridView.setAdapter(tableAdapter);
                gridView.setOnItemClickListener(TableOverviewFragment.this);
                tableAdapter.notifyDataSetChanged();
                gridView.invalidate();
            } catch (RealmException e) {
                Log.e(TAG, "Could not find any Table.class in DB");
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Table table = tableAdapter.getItem(position);
        if (!table.isReserved()) {
            reserveTableForCustomer(position);
        } else if (table.getReservedByCustomerId() == currentCustomerId) {
            removeReservation(position);
        }
        tableAdapter.notifyDataSetChanged();
    }

    private void reserveTableForCustomer(final int tableId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Customer customer = getCurrentCustomer(realm);
                    if (customer == null) {
                        return;
                    }
                    //customer had other reservation before => remove reservation
                    removeOldReservation(customer);
                    customer.setReservedTableId(tableId);
                    Table newTable = tableAdapter.getItem(tableId);
                    newTable.setReservedByCustomerId(currentCustomerId);
                }
            });
        } catch (RealmException e) {
            Log.e(TAG, "Could not persist reservation");
        }
    }

    private void removeReservation(final int tableId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Customer customer = getCurrentCustomer(realm);
                    if (customer == null) {
                        return;
                    }
                    //customer had other reservation before => remove reservation
                    removeOldReservation(customer);
                    customer.setReservedTableId(NO_RESERVED_TABLE);
                }
            });
        } catch (RealmException e) {
            Log.e(TAG, "Could not persist reservation");
        }
    }

    private void removeOldReservation(Customer customer) {
        if (customer.getReservedTableId() != NO_RESERVED_TABLE) {
            Table oldTable = tableAdapter.getItem(customer.getReservedTableId());
            oldTable.setReservedByCustomerId(NO_CUSTOMER);
        }
    }

    @Nullable
    private Customer getCurrentCustomer(Realm realm) {
        Customer customer = realm.where(Customer.class).equalTo("id", currentCustomerId)
                .findFirst();
        if (customer == null) {
            Log.e(TAG, "Could not find Customer with id " + currentCustomerId);
            return null;
        }
        return customer;
    }
}
