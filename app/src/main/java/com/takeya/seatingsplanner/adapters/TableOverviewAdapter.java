package com.takeya.seatingsplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.takeya.seatingsplanner.R;
import com.takeya.seatingsplanner.model.Table;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Takeya on 25.11.2017.
 */

public class TableOverviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private int colorTableBlocked = Color.RED;
    private int colorTableFree = Color.LTGRAY;
    private int colorTableReserved = Color.GREEN;

    private List<Table> tables = null;
    private int currentCustomerId;

    public TableOverviewAdapter(Context context, int currentCustomerId) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentCustomerId = currentCustomerId;
    }

    public void setData(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public int getCount() {
        if (tables == null) {
            return 0;
        }
        return tables.size();
    }

    @Override
    public Table getItem(int position) {
        if (tables == null || tables.get(position) == null) {
            return null;
        }
        return tables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = inflater.inflate(R.layout.fragment_table_overview_item, parent, false);
        }

        Table table = tables.get(position);

        if (table != null) {
            TextView tableNumber = currentView.findViewById(R.id.table_number);
            tableNumber.setText(String.valueOf(table.getId()));
            View gridItem = currentView.findViewById(R.id.table_item_layout);
            if(table.isReserved()) {
                if(table.getReservedByCustomerId() == currentCustomerId) {
                    gridItem.setBackgroundColor(colorTableReserved);
                }else{
                    gridItem.setBackgroundColor(colorTableBlocked);
                }
            } else {
                gridItem.setBackgroundColor(colorTableFree);
            }

        }

        return currentView;
    }
}
