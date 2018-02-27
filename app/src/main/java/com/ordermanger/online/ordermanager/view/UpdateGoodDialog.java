package com.ordermanger.online.ordermanager.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.util.StringUtil;

/**
 * Created by admin on 2017/7/21.
 */

public class UpdateGoodDialog extends Dialog implements View.OnClickListener {

    private OnUpdateChangeListener onUpdateChangeListener;
    private TextView tv_delete;
    private TextView tv_updatecount;
    private EditText et_good_count;
    private EditText et_good_price;
    GoodsDetail goodsDetail;

    public UpdateGoodDialog(@NonNull Context context) {
        super(context);
    }

    public UpdateGoodDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public UpdateGoodDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnUpdateChangeListener(OnUpdateChangeListener onUpdateChangeListener) {
        this.onUpdateChangeListener = onUpdateChangeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_goods_dialog);
        setCanceledOnTouchOutside(true);

        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_updatecount = (TextView) findViewById(R.id.tv_updatecount);
        et_good_count = (EditText) findViewById(R.id.et_good_count);
        et_good_price = (EditText) findViewById(R.id.et_good_price);
        tv_delete.setOnClickListener(this);
        tv_updatecount.setOnClickListener(this);
        if (goodsDetail != null) {
            et_good_count.setText(goodsDetail.getGoodsOrderQty() + "");
            et_good_price.setText(goodsDetail.getGoodsSellPrice() + "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_delete:
                if (onUpdateChangeListener != null) {
                    onUpdateChangeListener.deleteGoods();
                }
                cancel();
                break;
            case R.id.tv_updatecount:
                if (onUpdateChangeListener != null) {
                    if (StringUtil.isNotEmpty(et_good_price.getText().toString()) && Float.parseFloat(et_good_price.getText().toString()) <= 0) {
                        DialogUtils.showAlertNoTitle(getContext(), "金额必须大于0").show();
                        return;
                    }
                    onUpdateChangeListener.changeCount(Integer.parseInt(et_good_count.getText().toString()), et_good_price.getText().toString());

                }
                cancel();
                break;
        }
    }

    public void setGoods(GoodsDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public interface OnUpdateChangeListener {
        public void changeCount(int count, String price);

        public void deleteGoods();
    }
}
