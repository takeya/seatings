package com.takeya.seatingsplanner.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takeya.seatingsplanner.R;
import com.takeya.seatingsplanner.adapters.CustomerListAdapter;
import com.takeya.seatingsplanner.model.Customer;
import com.takeya.seatingsplanner.services.UpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by Takeya on 19.11.2017.
 */

public class CustomerListFragment extends Fragment implements CustomerListAdapter.OnClickCallback {

    private static final String TAG = CustomerListFragment.class.getName();
    private Realm realm;

    @BindView(R.id.customer_recycler_view)
    public RecyclerView recyclerView;

    private CustomerListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        adapter = new CustomerListAdapter(getActivity().getApplicationContext(),
                realm.where(Customer.class).findAll(),
                this);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity().getApplicationContext(),
                        DividerItemDecoration.VERTICAL));

        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onListItemClicked(int customerId) {
        TableOverviewFragment tableOverviewFragment = new TableOverviewFragment();
        Bundle tableOverviewBundle = new Bundle();
        tableOverviewBundle.putInt(TableOverviewFragment.CUSTOMER_ID, customerId);
        tableOverviewFragment.setArguments(tableOverviewBundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.main_container, tableOverviewFragment);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(0, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // No swipe used
        }


    }
}
