package com.takeya.seatingsplanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.takeya.seatingsplanner.R;
import com.takeya.seatingsplanner.model.Customer;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Takeya on 19.11.2017.
 */

public class CustomerListAdapter extends
                                 RealmRecyclerViewAdapter<Customer, CustomerListAdapter.CustomerViewHolder> {

    private final Context mCtx;
    private final OnClickCallback customerListFragmentCallback;

    public interface OnClickCallback {
        void onListItemClicked(int customerId);
    }

    public CustomerListAdapter(Context ctx, OrderedRealmCollection<Customer> entries,
                               OnClickCallback customerListFragment) {
        super(entries, true);
        this.mCtx = ctx;
        this.customerListFragmentCallback = customerListFragment;
        setHasStableIds(true);
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_customer_list_item, parent, false);
        return new CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        final Customer customer = getItem(position);
        holder.customerName.setText(
                String.format("%s %s", customer.getCustomerFirstName(),
                        customer.getCustomerLastName()));
        holder.tableNumber.setText(
                String.format(mCtx.getString(R.string.cutomer_item_table_number_d),
                        customer.getReservedTableId()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerListFragmentCallback.onListItemClicked(customer.getId());
            }
        });
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }



    class CustomerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.customer_item_name)
        TextView customerName;
        @BindView(R.id.customer_item_table_numer)
        TextView tableNumber;
        public View layout;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layout = itemView;
        }
    }
}
