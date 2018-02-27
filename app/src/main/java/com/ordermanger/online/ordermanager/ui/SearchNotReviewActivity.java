package com.ordermanger.online.ordermanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.ordermanger.online.ordermanager.adapter.SearchOrdersAdapter;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.model.OrderDetail;
import com.ordermanger.online.ordermanager.model.OrderFilter;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.SharedPreferenceUtils;
import com.ordermanger.online.ordermanager.util.StringUtil;
import com.ordermanger.online.ordermanager.util.Wethod;
import com.ordermanger.online.ordermanager.view.pulltorefresh.PullToRefreshBase;
import com.ordermanger.online.ordermanager.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/6/8.
 */
@ContentView(R.layout.activity_search_orders)
public class SearchNotReviewActivity extends BaseActivity implements View.OnClickListener  {

    private int SearchOrdersTag = 1;
    private int OrderSureTag = 2;
    private int OrderCancelTag = 3;
    private int request_code_order_filter = 4;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private int currentPage = 1;
    private int pageLength = 10;//限制每页显示data条数

    @ViewInject(R.id.iv_sure)
    ImageView iv_sure;
//    @ViewInject(R.id.et_search)
//    EditText et_search;
    @ViewInject(R.id.search_orders_list)
    PullToRefreshListView search_orders_list;

    SearchOrdersAdapter searchGoodsAdapter;

    List<OrderDetail> ordersDetails = new ArrayList<OrderDetail>();

    String searchtxt = "";
    OrderFilter orderFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackBtn();
        setTitle("未审核订单");

        displayTextAction("过滤条件", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("orderFilter", orderFilter);
                intent.setClass(SearchNotReviewActivity.this, OrderFilterActivity.class);
                startActivityForResult(intent, request_code_order_filter);
            }
        });

        search_orders_list.setMode(PullToRefreshBase.Mode.BOTH);
        search_orders_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                postUserLoc(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                postUserLoc(QUERY_MORE);
            }
        });
        boolean isReview = MyApplication.getInstance().isReviewRole(this);
        searchGoodsAdapter = new SearchOrdersAdapter(ordersDetails, this, isReview);
        search_orders_list.setAdapter(searchGoodsAdapter);

        orderFilter = new OrderFilter();
        initOrderFilter();

        postUserLoc(QUERY_REFERSH);

//        et_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                MyApplication.getHttpQueue().cancelAll(SearchOrdersTag);
//                SearchOrdersTag = new Random().nextInt(100000) + 100000;
//                postUserLoc(QUERY_REFERSH);
//            }
//        });
    }

    private void initOrderFilter() {
        String today = dateToStrLong(new Date());
        orderFilter.setStart_at(today);
        orderFilter.setEnd_at(today);
        searchtxt = Des.encrypt( " SHEETDATE >='" + today + "' AND SHEETDATE <= '" + today + "'");
    }

    @Override
    public void onClick(View view) {

    }

    /**
     *
     */
    public void httpOrderSure(final OrderDetail orderDetail){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认要审核通过吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDialog("正在处理...");
                GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
                Gson gson = gsonbuilder.create();
                String body = gson.toJson(orderDetail);
                Wethod.jsonPost(Request.Method.POST, SearchNotReviewActivity.this, OrderSureTag,  SubaruConfig.Http.auditOrder, Des.encrypt(body), SearchNotReviewActivity.this);
            }});
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void httpOrderCancel(final OrderDetail orderDetail){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认要取消此订单吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDialog("正在处理...");
                GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
                Gson gson = gsonbuilder.create();
                String body = gson.toJson(orderDetail);
                Wethod.jsonPost(Request.Method.POST, SearchNotReviewActivity.this, OrderCancelTag,  SubaruConfig.Http.cancelOrder, Des.encrypt(body), SearchNotReviewActivity.this);
            }});
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void postUserLoc(int type) {
        String locNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo);
        String ownerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo);
        if (type == QUERY_REFERSH) {
            isRefresh = 1;
            currentPage = 1;
        } else {
            isRefresh = 2;
            currentPage += 10;
        }
        Wethod.jsonPost(Request.Method.GET, this, SearchOrdersTag, String.format(SubaruConfig.Http.getOrderMastersUnAduit, locNo, ownerNo, currentPage, pageLength, searchtxt), "", this);
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == SearchOrdersTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
//                    ToastUtils.show(this, "未获取到订单信息");
                    ordersDetails.clear();
                    searchGoodsAdapter.notifyDataSetChanged();
                    search_orders_list.onRefreshComplete();
                } else {
                    String jsonArrayStr = jsonObject.getString("ActionResultsList");
                    Gson gs = new Gson();
                    List<OrderDetail> jsonListObject = gs.fromJson(jsonArrayStr, new TypeToken<List<OrderDetail>>() {
                    }.getType());
                    LogUtil.print("search goods count:" + jsonListObject.size());
                    if (jsonListObject.size() > 0) {
                        if (isRefresh == 1) {
                            ordersDetails.clear();
                        }
                        ordersDetails.addAll(jsonListObject);
                        searchGoodsAdapter.notifyDataSetChanged();
                        search_orders_list.onRefreshComplete();
                    } else if (jsonListObject.size() == 0 && isRefresh == 1) {
                        ordersDetails.clear();
                        searchGoodsAdapter.notifyDataSetChanged();
                        search_orders_list.onRefreshComplete();
                    } else {
                        search_orders_list.onRefreshComplete();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(reqcode == OrderCancelTag){
            cancelDialog();
            postUserLoc(QUERY_REFERSH);
        }else if(reqcode == OrderSureTag){
            cancelDialog(); //{"ActionResults":0,"ErrorMessage":null,"ActionResultsList":["订单审核成功!"]}
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpSucessCode) {
                    String jsonArrayStr = jsonObject.getJSONArray("ActionResultsList").getString(0);
                    ToastUtils.show(this, jsonArrayStr);
                }
                postUserLoc(QUERY_REFERSH);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_code_order_filter && resultCode == RESULT_OK){
            searchtxt = data.getStringExtra("QueryCondition");
            orderFilter = (OrderFilter)data.getSerializableExtra("orderFilter");
            postUserLoc(QUERY_REFERSH);
        }
    }
}
