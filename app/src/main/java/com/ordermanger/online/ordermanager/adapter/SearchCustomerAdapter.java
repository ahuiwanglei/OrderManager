package com.ordermanger.online.ordermanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.Customer;
import com.ordermanger.online.ordermanager.model.OrderDetail;
import com.ordermanger.online.ordermanager.ui.OrderDetailShowActivity;
import com.ordermanger.online.ordermanager.ui.SearchOrderActivity;

import java.util.List;

/**
 * Created by admin on 2017/6/8.
 */

public class SearchCustomerAdapter extends MyBaseAdapter<Customer> {

    List<Customer> customerList;

    public SearchCustomerAdapter(List<Customer> list, Context mContext) {
        super(list, mContext);
        this.mContext = mContext;
        this.customerList = list;
    }
    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        Customer customer = this.customerList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_search_customer_list_item, parent, false);
            viewHolder.tv_CustomerName = (TextView) convertView.findViewById(R.id.tv_CustomerName);
            viewHolder.tv_CustomerNo = (TextView) convertView.findViewById(R.id.tv_CustomerNo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_CustomerName.setText(customer.getCustName() + "");
        viewHolder.tv_CustomerNo.setText(" (" + customer.getCustNo() + ")");
        return convertView;
    }

    class ViewHolder{
        private TextView tv_CustomerName;
        private TextView tv_CustomerNo;

    }
}
