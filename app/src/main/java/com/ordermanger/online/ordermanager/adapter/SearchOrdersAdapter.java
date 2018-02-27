package com.ordermanger.online.ordermanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.model.OrderDetail;
import com.ordermanger.online.ordermanager.ui.OrderDetailShowActivity;
import com.ordermanger.online.ordermanager.ui.SearchNotReviewActivity;
import com.ordermanger.online.ordermanager.ui.SearchOrderActivity;
import com.ordermanger.online.ordermanager.util.StringUtil;

import java.util.List;

/**
 * Created by admin on 2017/5/21.
 */

public class SearchOrdersAdapter extends MyBaseAdapter<OrderDetail> {

    private Context mContext;
    private List<OrderDetail> merchantListBeen;
    private boolean isReview;
    String[] mItems;
    public SearchOrdersAdapter(List<OrderDetail> list, Context mContext, boolean isReview) {
        super(list, mContext);
        this.mContext = mContext;
        this.merchantListBeen = list;
        this.isReview = isReview;
        mItems = mContext.getResources().getStringArray(R.array.order_type);
    }

    @Override
    public View initView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final OrderDetail orderDetail = this.merchantListBeen.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_search_orders_list_item, parent, false);
            viewHolder.tv_CustomerName = (TextView) convertView.findViewById(R.id.tv_CustomerName);
            viewHolder.tv_CustomerNo = (TextView) convertView.findViewById(R.id.tv_CustomerNo);
            viewHolder.tv_LocNo = (TextView) convertView.findViewById(R.id.tv_LocNo);
            viewHolder.tv_OwnerNo = (TextView) convertView.findViewById(R.id.tv_OwnerNo);
            viewHolder.tv_SHEETDATE = (TextView) convertView.findViewById(R.id.tv_SHEETDATE);
            viewHolder.tv_SheetType = (TextView) convertView.findViewById(R.id.tv_SheetType);
            viewHolder.tv_DeptNo = (TextView) convertView.findViewById(R.id.tv_DeptNo);
            viewHolder.tv_Memo = (TextView) convertView.findViewById(R.id.tv_Memo);
            viewHolder.btn_order_sure = (Button) convertView.findViewById(R.id.btn_order_sure);
            viewHolder.btn_order_cancel = (Button) convertView.findViewById(R.id.btn_order_cancel);
            viewHolder.linear_review = (LinearLayout)convertView.findViewById(R.id.linear_review);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_CustomerName.setText(orderDetail.getCustomerName() + "");
        viewHolder.tv_CustomerNo.setText(" (" + orderDetail.getCustomerNo() + ")");
        viewHolder.tv_LocNo.setText(orderDetail.getLocName() + " (" +orderDetail.getLocNo() +")");
        viewHolder.tv_OwnerNo.setText(orderDetail.getOwnerName() + " (" + orderDetail.getOwnerNo() +")");
        viewHolder.tv_SHEETDATE.setText(orderDetail.getSHEETDATE() + "");
        if(StringUtil.isNotEmpty(orderDetail.getSheetType()) && Integer.parseInt(orderDetail.getSheetType()) < mItems.length){
           viewHolder.tv_SheetType.setText(mItems[Integer.parseInt(orderDetail.getSheetType())]);
        }else{
            viewHolder.tv_SheetType.setText("");
        }
        viewHolder.tv_DeptNo.setText(orderDetail.getDeptName() +"("+ orderDetail.getDeptNo() +")");
        viewHolder.tv_Memo.setText(orderDetail.getMemo());
        if(this.isReview){
            viewHolder.linear_review.setVisibility(View.VISIBLE);
        }else{
            viewHolder.linear_review.setVisibility(View.GONE);
        }
        viewHolder.btn_order_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof SearchNotReviewActivity) {
                    SearchNotReviewActivity activity = (SearchNotReviewActivity) mContext;
                    activity.httpOrderSure(orderDetail);
                }
            }
        });
        viewHolder.btn_order_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof SearchNotReviewActivity) {
                    SearchNotReviewActivity activity = (SearchNotReviewActivity) mContext;
                    activity.httpOrderCancel(orderDetail);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mContext instanceof SearchOrderActivity) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailShowActivity.class);
                    intent.putExtra("order", orderDetail);
                    intent.putExtra("isReview", isReview);
                    mContext.startActivity(intent);
//                }
            }
        });

        return convertView;
    }


    class ViewHolder {
        TextView tv_CustomerName;
        TextView tv_CustomerNo;
        TextView tv_LocNo;
        TextView tv_OwnerNo;
        TextView tv_SheetType;
        TextView tv_DeptNo;
        TextView tv_SHEETDATE;
        TextView tv_Memo;
        Button btn_order_sure;
        Button btn_order_cancel;
        LinearLayout linear_review;

    }
}
