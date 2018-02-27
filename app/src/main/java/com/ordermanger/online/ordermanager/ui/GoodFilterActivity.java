package com.ordermanger.online.ordermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.GoodsFilter;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.StringUtil;

/**
 * Created by admin on 2017/6/15.
 */

@ContentView(R.layout.activity_good_filter)
public class GoodFilterActivity extends BaseActivity {

    @ViewInject(R.id.et_GoodsNo)
    private EditText et_GoodsNo;
    @ViewInject(R.id.et_GoodsName)
    private EditText et_GoodsName;
    @ViewInject(R.id.et_GoodsBarCode)
    private EditText et_GoodsBarCode;

    @ViewInject(R.id.btn_search)
    Button btn_search;

    private GoodsFilter goodFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackBtn();
        goodFilter = (GoodsFilter)getIntent().getSerializableExtra("goodsFilter");
        if(StringUtil.isNotEmpty(goodFilter.getGoodsNo())){
            et_GoodsNo.setText(goodFilter.getGoodsNo()+"");
        }
        et_GoodsName.setText(goodFilter.getGoodsName());
        et_GoodsBarCode.setText(goodFilter.getGoodsBarCode());
        displayTextAction("清空", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_GoodsNo.setText("");
                et_GoodsBarCode.setText("");
                et_GoodsName.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = "";
                if (StringUtil.isNotEmpty(et_GoodsNo.getText().toString())) {
                    query = "GoodsNo like '%" + et_GoodsNo.getText().toString() + "%' ";
                }
                if (StringUtil.isNotEmpty(et_GoodsName.getText().toString())) {
                    if (StringUtil.isNotEmpty(query)) {
                        query = query + " AND ";
                    }
                    query += " GoodsName like '%" + et_GoodsName.getText().toString() + "%' ";
                }
                if (StringUtil.isNotEmpty(et_GoodsBarCode.getText().toString())) {
                    if (StringUtil.isNotEmpty(query)) {
                        query = query + " AND ";
                    }
                    query += " GoodsBarCode like '%" + et_GoodsBarCode.getText().toString() + "%' ";
                }
                goodFilter.setGoodsNo(et_GoodsNo.getText().toString());
                goodFilter.setGoodsBarCode(et_GoodsBarCode.getText().toString());
                goodFilter.setGoodsName(et_GoodsName.getText().toString());
                LogUtil.print("goodsFilter:" + query);
                if (StringUtil.isNotEmpty(query)) {
                    query = Des.encrypt(query);
                }
                Intent intent = new Intent();
                intent.putExtra("goodsFilter", goodFilter);
                intent.putExtra("QueryCondition", query);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
    @Override
    public void onSuccess(int reqcode, Object result) {

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
