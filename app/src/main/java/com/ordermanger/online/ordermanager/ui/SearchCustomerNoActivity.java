package com.ordermanger.online.ordermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.MyApplication;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.adapter.SearchCustomerAdapter;
import com.ordermanger.online.ordermanager.adapter.SearchGoodsAdapter;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.model.Customer;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
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
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/6/8.
 */
@ContentView(R.layout.activity_search_goods)
public class SearchCustomerNoActivity extends BaseActivity implements View.OnClickListener {

    private int SearchCustomerNosTag = 1;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private int currentPage = 1;
    private int pageLength = 10;//限制每页显示data条数

    @ViewInject(R.id.iv_sure)
    ImageView iv_sure;
    @ViewInject(R.id.et_search)
    EditText et_search;
    @ViewInject(R.id.search_goods_list)
    PullToRefreshListView search_goods_list;

    SearchCustomerAdapter searchCustomerAdapter;

    List<Customer> selectCustomers = new ArrayList<Customer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtil.print("SearchCustomerNoActivity onCreate");
        initBackBtn();
        setTitle("选择客户");

        findViewById(R.id.linear_et_search).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_sure).setVisibility(View.GONE);
        search_goods_list.setMode(PullToRefreshBase.Mode.BOTH);
        search_goods_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                postUserLoc(QUERY_REFERSH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                postUserLoc(QUERY_MORE);
            }
        });
        searchCustomerAdapter = new SearchCustomerAdapter(selectCustomers, this);
        search_goods_list.setAdapter(searchCustomerAdapter);
        postUserLoc(QUERY_REFERSH);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MyApplication.getHttpQueue().cancelAll(SearchCustomerNosTag);
                SearchCustomerNosTag = new Random().nextInt(100000) + 100000;
                postUserLoc(QUERY_REFERSH);
            }
        });

        search_goods_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtil.print(selectCustomers.get(i-1).toString());
                Intent data = new Intent();
                data.putExtra("customerNo", selectCustomers.get(i-1).getCustNo());
                data.putExtra("customerName", selectCustomers.get(i-1).getCustName());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void postUserLoc(int type) {
        String ownerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo);
        if (type == QUERY_REFERSH) {
            isRefresh = 1;
            currentPage = 1;
        } else {
            isRefresh = 2;
            currentPage += 10;
        }
        String searchtxt = "";
        if (StringUtil.isNotEmpty(et_search.getText().toString())) {
            searchtxt = Des.encrypt(et_search.getText().toString());
        }
        LogUtil.print("et_search: " + et_search.getText().toString()+"  searchTxt:" + searchtxt);
        Wethod.jsonPost(Request.Method.GET, this, SearchCustomerNosTag, String.format(SubaruConfig.Http.getUserCustomer, ownerNo, currentPage, pageLength, searchtxt), "", this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == SearchCustomerNosTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                    search_goods_list.onRefreshComplete();
                } else {
                    String jsonArrayStr = jsonObject.getString("ActionResultsList");
                    Gson gs = new Gson();
                    List<Customer> jsonListObject = gs.fromJson(jsonArrayStr, new TypeToken<List<Customer>>() {
                    }.getType());
                    LogUtil.print("search goods count:" + jsonListObject.size());
                    if (jsonListObject.size() > 0) {
                        if (isRefresh == 1) {
                            selectCustomers.clear();
                        }

                        selectCustomers.addAll(jsonListObject);
                    } else if (jsonListObject.size() == 0 && isRefresh == 1) {
                        selectCustomers.clear();
                    }
                    dataChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void dataChanged() {
        searchCustomerAdapter.notifyDataSetChanged();
        search_goods_list.onRefreshComplete();
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
