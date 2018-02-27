package com.ordermanger.online.ordermanager.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.MyApplication;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.SharedPreferenceUtils;
import com.ordermanger.online.ordermanager.util.StringBytes;
import com.ordermanger.online.ordermanager.util.StringUtil;
import com.ordermanger.online.ordermanager.util.Wethod;
import com.ordermanger.online.ordermanager.view.UpdateGoodDialog;
import com.ordermanger.online.ordermanager.zbar.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/5/20.
 */

@ContentView(R.layout.activity_add_order)
public class AddOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final int request_code_add_goods = 1;
    private static final int request_code_scan = 2;
    private static final int request_code_customer_no = 3;
    private static final int OrderSureTag = 8;
    private static final int request_code_http_getorderid_pre = 3;
    private static final int request_code_http_getorderid_submit = 5;
    private static final int request_code_http_addorder = 4;

    @ViewInject(R.id.tv_DeptNo)
    TextView tv_DeptNo;
    @ViewInject(R.id.tv_LocNo)
    TextView tv_LocNo;
    @ViewInject(R.id.tv_OwnerNo)
    TextView tv_OwnerNo;
    @ViewInject(R.id.et_SheetID)
    EditText et_SheetID;
    @ViewInject(R.id.tv_CustomerNo)
    TextView tv_CustomerNo;

    @ViewInject(R.id.sp_SheetType)
    Spinner sp_SheetType;

    @ViewInject(R.id.et_Memo)
    EditText et_Memo;
    @ViewInject(R.id.linear_goods)
    LinearLayout linear_goods;
    @ViewInject(R.id.linear_goods_control)
    LinearLayout linear_goods_control;

    @ViewInject(R.id.iv_add_goods)
    ImageView iv_add_goods;
    @ViewInject((R.id.iv_scan))
    ImageView iv_scan;
    @ViewInject(R.id.btn_sure)
    Button btn_sure;
    @ViewInject(R.id.linear_btns)
    LinearLayout linear_btns;
    @ViewInject(R.id.btn_review)
    Button btn_review;
    @ViewInject(R.id.btn_next_add)
    Button btn_next_add;

    String orderid;
    List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackBtn();
        setTitle("新增订单");

        tv_DeptNo.setTag(Des.decrypt(StringBytes.hexStr2Bytes((SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.DeptNo)))));
        tv_LocNo.setTag(Des.decrypt(StringBytes.hexStr2Bytes((SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo)))));
        tv_OwnerNo.setTag(Des.decrypt(StringBytes.hexStr2Bytes((SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo)))));

        tv_LocNo.setText(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocName) + "(" + tv_LocNo.getTag().toString() + ")");
        tv_OwnerNo.setText(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerName) + "(" + tv_OwnerNo.getTag().toString() + ")");
        tv_DeptNo.setText(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.DeptName) + "(" + tv_DeptNo.getTag().toString() + ")");

        iv_add_goods.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        btn_next_add.setOnClickListener(this);
        tv_CustomerNo.setOnClickListener(this);

        String[] mItems = getResources().getStringArray(R.array.order_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_SheetType.setAdapter(adapter);
        sp_SheetType.setSelection(0);
        sp_SheetType.setTag(0);
        sp_SheetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                sp_SheetType.setTag(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        showDialog("正在获取订单编号");
        getOrderID(request_code_http_getorderid_pre);
        boolean isReview = MyApplication.getInstance().isReviewRole(this);
        if(!isReview){
            btn_review.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        try {
            //{"ActionResults":0,"ErrorMessage":null,"ActionResultsList":["OT17052200139"]}
            if (reqcode == request_code_http_getorderid_pre || reqcode == request_code_http_getorderid_submit) {
                cancelDialog();
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                    ToastUtils.show(this, "未获取到订单编号");
                    cancelDialog();
                } else {
                    orderid = jsonObject.getJSONArray("ActionResultsList").getString(0);
                    if (request_code_http_getorderid_submit == reqcode) {
                        httpPostAddOrder();
                    }
                }
            } else if (reqcode == request_code_http_addorder) {
                cancelDialog();
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpSucessCode) {
                    String msg = jsonObject.getJSONArray("ActionResultsList").getString(0);
                    ToastUtils.show(this, msg);
                    btn_sure.setVisibility(View.GONE);
                    linear_goods_control.setVisibility(View.GONE);
                    linear_btns.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.show(this, jsonObject.getString("ErrorMessage") + "");
                }
            } else if (reqcode == OrderSureTag) {
                LogUtil.print("OrderSureTag:" + result);
                cancelDialog();
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpSucessCode) {
                    String msg = jsonObject.getJSONArray("ActionResultsList").getString(0);
                    ToastUtils.show(this, msg);
                    btn_review.setEnabled(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    private void postReview() {
        try {
            showDialog("正在提交审核...");
            JSONObject json = new JSONObject();
            json.put("LocNo", tv_LocNo.getTag().toString());
            json.put("OwnerNo", tv_OwnerNo.getTag().toString());
            json.put("LocName", SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocName));
            json.put("OwnerName", SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.DeptName));
            json.put("Memo", et_Memo.getText().toString());
            json.put("SheetType", sp_SheetType.getTag() + "");
            json.put("CustomerNo", tv_CustomerNo.getTag().toString());//动态获取
            json.put("DeptNo", tv_DeptNo.getTag().toString());
            json.put("SheetID", orderid);
            GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
            Gson gson = gsonbuilder.create();
            String body = gson.toJson(json);
            LogUtil.print("body:" + body);
            Wethod.jsonPost(Request.Method.POST, AddOrderActivity.this, OrderSureTag, SubaruConfig.Http.auditOrder, Des.encrypt(body), AddOrderActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_goods:
                Intent intent = new Intent();
                intent.setClass(this, SearchGoodsActivity.class);
                Gson gson = new Gson();
                intent.putExtra("selectGoods", gson.toJson(convertListGoodsDetail(goodsDetailList)));
                startActivityForResult(intent, request_code_add_goods);
                LogUtil.print(" startActivity SearchGoodsActivity");
                break;
            case R.id.iv_scan:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.CAMERA,}, 110);
                    } else {
                        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                        startActivityForResult(openCameraIntent, request_code_scan);
                    }
                } else {
                    Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                    startActivityForResult(openCameraIntent, request_code_scan);
                }
                break;
            case R.id.btn_sure:
                if (StringUtil.isEmpty(orderid)) {
                    showDialog("正在获取订单编号...");
                    getOrderID(request_code_http_getorderid_submit);
                    return;
                } else {
                    httpPostAddOrder();
                }
                break;
            case R.id.btn_review:
                postReview();
                break;
            case R.id.btn_next_add:
                getOrderID(request_code_http_getorderid_pre);
                linear_btns.setVisibility(View.GONE);
                linear_goods_control.setVisibility(View.VISIBLE);
                sp_SheetType.setSelection(0);
                et_Memo.setText("");
                tv_CustomerNo.setText("");
                goodsDetailList.clear();
                initGoodsView();
                btn_sure.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_CustomerNo:
                Intent intent4 = new Intent(this, SearchCustomerNoActivity.class);
                startActivityForResult(intent4, request_code_customer_no);
                break;
        }
    }

    //合并相同商品的数目
    private List<GoodsDetail> convertListGoodsDetail(List<GoodsDetail> goodsDetailList){
        List<GoodsDetail> allGoodDetailList = new ArrayList<>();
        Map<String, Integer> maps = new HashMap<>();
        for (int i=0; i< goodsDetailList.size();i++){
            if(maps.containsKey(goodsDetailList.get(i).getGoodsNo())){
                maps.put(goodsDetailList.get(i).getGoodsNo(), maps.get(goodsDetailList.get(i).getGoodsNo()) + goodsDetailList.get(i).getGoodsOrderQty());
            }else{
                maps.put(goodsDetailList.get(i).getGoodsNo(), goodsDetailList.get(i).getGoodsOrderQty());
            }
        }

        for (int i = 0; i < goodsDetailList.size(); i++) {
            if (!allGoodDetailList.contains(goodsDetailList.get(i))) {
                allGoodDetailList.add((GoodsDetail) goodsDetailList.get(i).copy());
            }
        }
        for(int i=0;i< allGoodDetailList.size();i++){
            allGoodDetailList.get(i).setGoodsOrderQty(maps.get(allGoodDetailList.get(i).getGoodsNo()));
        }

        return allGoodDetailList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            Intent openCameraIntent = new Intent(this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, request_code_scan);
        }
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code_add_goods && resultCode == RESULT_OK) {
            Gson gson = new Gson();
            List<GoodsDetail> jsonListObject = gson.fromJson(data.getStringExtra("selectGoods"), new TypeToken<List<GoodsDetail>>() {
            }.getType());
//            List<GoodsDetail> allNewGoods = new ArrayList<GoodsDetail>();
//            for (int n = 0; n < jsonListObject.size(); n++) {
//                int temp = 0;
//                for (int i = 0; i < goodsDetailList.size(); i++) {
//                    if (goodsDetailList.get(i).getGoodsNo().equals(jsonListObject.get(n).getGoodsNo())) {
//                        goodsDetailList.get(i).setGoodsOrderQty(jsonListObject.get(n).getGoodsOrderQty() + goodsDetailList.get(i).getGoodsOrderQty());
//                        break;
//                    } else {
//                        temp = temp + 1;
//                    }
//                }
//                if (temp == goodsDetailList.size()) {
//                    allNewGoods.add(jsonListObject.get(n));
//                }
//            }
            goodsDetailList.addAll(jsonListObject);
            initGoodsView();
        } else if (requestCode == request_code_scan && resultCode == RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {//二维码
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");

                Intent intent = new Intent();
                intent.setClass(this, SearchGoodsActivity.class);
                Gson gson = new Gson();
                intent.putExtra("selectGoods", gson.toJson(convertListGoodsDetail(goodsDetailList)));
                intent.putExtra("scanCode", scanResult);
                startActivityForResult(intent, request_code_add_goods);
            }
        } else if (requestCode == request_code_customer_no && resultCode == RESULT_OK) {
            LogUtil.print(data.getStringExtra("customerNo"));
            tv_CustomerNo.setText(data.getStringExtra("customerName") + " (" + (data.getStringExtra("customerNo") + ")"));
            tv_CustomerNo.setTag(data.getStringExtra("customerNo"));
        }
    }

    private void httpPostAddOrder() {
        String workerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO);
        JSONObject json = new JSONObject();
        try {
            if (goodsDetailList.size() == 0) {
                ToastUtils.show(this, "至少选择一个商品");
                return;
            } else if (StringUtil.isEmpty(tv_CustomerNo.getTag().toString())) {
                ToastUtils.show(this, "请选择客户编号");
                return;
            }
            showDialog("正在提交订单...");
            Gson gson = new Gson();
            JSONArray jsonArray = new JSONArray(gson.toJson(goodsDetailList));
            json.put("Memo", et_Memo.getText().toString());
            json.put("SheetType", sp_SheetType.getTag() + "");
            json.put("CustomerNo", tv_CustomerNo.getTag().toString());
            json.put("DeptNo", tv_DeptNo.getTag().toString());
            json.put("LocNo", tv_LocNo.getTag().toString());
            json.put("OwnerNo", tv_OwnerNo.getTag().toString());
            json.put("SheetID", orderid);
            json.put("GoodsInfo", jsonArray);

            Wethod.jsonPost(Request.Method.POST, this, request_code_http_addorder, SubaruConfig.Http.addOrderDetails, Des.encrypt(json.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getOrderID(int tag) {
        String locNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo);
        String ownerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo);
        Wethod.jsonPost(Request.Method.GET, this, tag, String.format(SubaruConfig.Http.getOrderId, locNo, ownerNo), "", this);
    }

    private void initGoodsView() {
        linear_goods.removeAllViews();
        for (int i = 0; i < goodsDetailList.size(); i++) {
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_goods_item, null);
            TextView tv_GoodsName = (TextView) view.findViewById(R.id.tv_GoodsName);
            TextView tv_GoodsNo = (TextView) view.findViewById(R.id.tv_GoodsNo);
            TextView tv_GoodsPackQty = (TextView) view.findViewById(R.id.tv_GoodsPackQty);
            TextView tv_GoodsPackUnit = (TextView) view.findViewById(R.id.tv_GoodsPackUnit);
            TextView tv_GoodsOrderQty = (TextView) view.findViewById(R.id.tv_GoodsOrderQty);
            TextView tv_GoodsSellPrice = (TextView)view.findViewById(R.id.tv_GoodsSellPrice);
            tv_GoodsName.setText(goodsDetailList.get(i).getGoodsName());
            tv_GoodsNo.setText("(" + goodsDetailList.get(i).getGoodsNo() + ")");
            tv_GoodsPackQty.setText(goodsDetailList.get(i).getGoodsPackQty());
            tv_GoodsPackUnit.setText(goodsDetailList.get(i).getGoodsPackUnit());
            tv_GoodsOrderQty.setText(goodsDetailList.get(i).getGoodsOrderQty() + "");
            tv_GoodsSellPrice.setText(StringUtil.isNotEmpty(goodsDetailList.get(i).getGoodsSellPrice()) ? goodsDetailList.get(i).getGoodsSellPrice() : "未指定");
            tv_GoodsSellPrice.setTag(goodsDetailList.get(i).getGoodsSellPrice());

            view.setTag(goodsDetailList.get(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final GoodsDetail goodsDetail = (GoodsDetail) view.getTag();
                    UpdateGoodDialog updateGoodDialog = new UpdateGoodDialog(AddOrderActivity.this);
                    updateGoodDialog.setGoods(goodsDetail);
                    updateGoodDialog.setOnUpdateChangeListener(new UpdateGoodDialog.OnUpdateChangeListener() {
                        @Override
                        public void changeCount(int count, String price) {
                            goodsDetail.setGoodsOrderQty(count);
                            goodsDetail.setGoodsSellPrice(price);
                            initGoodsView();
                        }

                        @Override
                        public void deleteGoods() {
                            goodsDetailList.remove(goodsDetail);
                            initGoodsView();
                        }
                    });
                    updateGoodDialog.show();


                }
            });
            linear_goods.addView(view);
        }
    }
}
