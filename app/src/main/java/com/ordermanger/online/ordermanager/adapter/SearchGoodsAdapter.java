package com.ordermanger.online.ordermanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/21.
 */

public class SearchGoodsAdapter extends MyBaseAdapter<GoodsDetail> {

    private Context mContext;
    private List<GoodsDetail> merchantListBeen;
    private Map<String, Integer> ordersNo = new HashMap<>();


    public SearchGoodsAdapter(List<GoodsDetail> list, Context mContext) {
        super(list, mContext);
        this.mContext = mContext;
        this.merchantListBeen = list;
    }

    @Override
    public View initView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        GoodsDetail goodsDetail = merchantListBeen.get(position);
        LogUtil.print("position： " + position  + "  "  + goodsDetail.toString());
        if(ordersNo.containsKey(goodsDetail.getGoodsNo())){
            goodsDetail.setGoodsOrderQty(ordersNo.get(goodsDetail.getGoodsNo()));
        }

//        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_search_goods_list_item, parent, false);
            viewHolder.tv_GoodsName = (TextView) convertView.findViewById(R.id.tv_GoodsName);
            viewHolder.tv_GoodsNo = (TextView) convertView.findViewById(R.id.tv_GoodsNo);
            viewHolder.tv_GoodsBarCode = (TextView)convertView.findViewById(R.id.tv_GoodsBarCode);
            viewHolder.tv_GoodsPackQty = (TextView) convertView.findViewById(R.id.tv_GoodsPackQty);
            viewHolder.tv_GoodsPackUnit = (TextView) convertView.findViewById(R.id.tv_GoodsPackUnit);
            viewHolder.tv_GoodsOrderQty = (TextView) convertView.findViewById(R.id.tv_GoodsOrderQty);
            viewHolder.tv_GoodsSellPrice = (TextView)convertView.findViewById(R.id.tv_GoodsSellPrice);
            viewHolder.tv_GoodsQty = (TextView)convertView.findViewById(R.id.tv_GoodsQty);
            viewHolder.dishes_reduce = (ImageView) convertView.findViewById(R.id.dishes_reduce);
            viewHolder.dishes_num = (EditText) convertView.findViewById(R.id.dishes_num);
            viewHolder.dishes_add = (ImageView) convertView.findViewById(R.id.dishes_add);
            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
        viewHolder.tv_GoodsName.setText(goodsDetail.getGoodsName());
        viewHolder.tv_GoodsNo.setText("(" + goodsDetail.getGoodsNo() +")");
        viewHolder.tv_GoodsPackQty.setText(goodsDetail.getGoodsPackQty());
        viewHolder.tv_GoodsPackUnit.setText(goodsDetail.getGoodsPackUnit());
        LogUtil.print("viewHOlder:" + viewHolder.dishes_num +"  "  + goodsDetail );
        viewHolder.dishes_num.setText(goodsDetail.getGoodsOrderQty() + "");
        viewHolder.tv_GoodsQty.setText(goodsDetail.getGoodsQty()+"");
        viewHolder.tv_GoodsBarCode.setText(goodsDetail.getGoodsBarCode());
        viewHolder.tv_GoodsSellPrice.setText(goodsDetail.getGoodsSellPrice()+"");
        viewHolder.dishes_reduce.setTag(goodsDetail);
        viewHolder.dishes_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetail goodsDetail1 = (GoodsDetail)view.getTag();
                if (goodsDetail1.getGoodsOrderQty() > 0) {
                    goodsDetail1.setGoodsOrderQty(goodsDetail1.getGoodsOrderQty() - 1);
                }else{
                    goodsDetail1.setGoodsOrderQty(0);
                }
                ordersNo.put(goodsDetail1.getGoodsNo(), goodsDetail1.getGoodsOrderQty());
                notifyDataSetChanged();
            }
        });
        viewHolder.dishes_add.setTag(goodsDetail);
        viewHolder.dishes_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetail goodsDetail1 = (GoodsDetail)view.getTag();
                goodsDetail1.setGoodsOrderQty(goodsDetail1.getGoodsOrderQty() + 1);
                ordersNo.put(merchantListBeen.get(position).getGoodsNo(), merchantListBeen.get(position).getGoodsOrderQty());
                notifyDataSetChanged();
            }
        });
        viewHolder.dishes_num.clearFocus();
//        viewHolder.dishes_num.addTextChangedListener(null);
        viewHolder.dishes_num.setTag(merchantListBeen.get(position));
        viewHolder.dishes_num.addTextChangedListener( new  TextWatcher(){


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtil.print("afterTextChanged: "  +  editable +" pos=" + position);
                GoodsDetail goodsDetail1 = (GoodsDetail)viewHolder.dishes_num.getTag();
                if(StringUtil.isEmpty(editable.toString())){
                    goodsDetail1.setGoodsOrderQty(0);
                    ordersNo.put(goodsDetail1.getGoodsNo(), 0);
                }else{
                    goodsDetail1.setGoodsOrderQty(Integer.parseInt(editable.toString()));
                    ordersNo.put(goodsDetail1.getGoodsNo(), Integer.parseInt(editable.toString()));
                }
//                notifyDataSetChanged();
            }
        });
        return convertView;
    }



    public List<GoodsDetail> getSelectGoods() {
        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
        for (int i=0;i< merchantListBeen.size();i++){
            if(merchantListBeen.get(i).getGoodsOrderQty() >0) {
                goodsDetail.add(merchantListBeen.get(i));
            }
        }
        return goodsDetail;
    }

    class ViewHolder {
        TextView tv_GoodsName;
        TextView tv_GoodsNo;
        TextView tv_GoodsBarCode;
        TextView tv_GoodsPackQty;
        TextView tv_GoodsPackUnit;
        TextView tv_GoodsOrderQty;
        TextView tv_GoodsSellPrice;
        TextView tv_GoodsQty;
        ImageView dishes_reduce;
        EditText dishes_num;
        ImageView dishes_add;
    }
}
