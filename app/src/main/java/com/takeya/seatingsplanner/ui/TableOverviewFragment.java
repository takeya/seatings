package com.takeya.seatingsplanner.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.takeya.seatingsplanner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Takeya on 23.11.2017.
 */

public class TableOverviewFragment extends Fragment {
    public static final String CUSTOMER_ID = "customerId";

    @BindView(R.id.fragment_table_overview_grid)
    public GridView gridView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_overview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
