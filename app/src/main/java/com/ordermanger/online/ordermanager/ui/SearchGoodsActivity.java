package com.ordermanger.online.ordermanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import com.ordermanger.online.ordermanager.adapter.SearchGoodsAdapter;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.model.GoodsFilter;
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
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/5/20.
 */
@ContentView(R.layout.activity_search_goods)
public class SearchGoodsActivity extends BaseActivity implements View.OnClickListener {

    private static final int request_code_goods_filter = 2;

    private int SearchGoodsTag = 1;
    final int QUERY_MORE = 0x0101;
    final int QUERY_REFERSH = 0x0110;
    private int isRefresh = 0;
    private int currentPage = 1;
    private int pageLength = 10;//限制每页显示data条数

    @ViewInject(R.id.btn_sure)
    Button btn_sure;
//    @ViewInject(R.id.et_search)
//    EditText et_search;
    @ViewInject(R.id.search_goods_list)
    PullToRefreshListView search_goods_list;

    SearchGoodsAdapter searchGoodsAdapter;

    List<GoodsDetail> goodsDetails = new ArrayList<GoodsDetail>();

    List<GoodsDetail> selectGoods;

    private GoodsFilter goodsFilter;
    String searchtxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackBtn();
        setTitle("选择商品");
        displayTextAction("过滤条件", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("goodsFilter", goodsFilter);
                intent.setClass(SearchGoodsActivity.this, GoodFilterActivity.class);
                startActivityForResult(intent, request_code_goods_filter);
            }
        });
        goodsFilter = new GoodsFilter();
        goodsFilter.setGoodsBarCode(getIntent().getStringExtra("scanCode"));

        Gson gson = new Gson();
        String selectStr = getIntent().getStringExtra("selectGoods");
        if (StringUtil.isNotEmpty(selectStr)) {
            selectGoods = gson.fromJson(selectStr, new TypeToken<List<GoodsDetail>>() {
            }.getType());
        }

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
        searchGoodsAdapter = new SearchGoodsAdapter(goodsDetails, this);
        search_goods_list.setAdapter(searchGoodsAdapter);
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
//                MyApplication.getHttpQueue().cancelAll(SearchGoodsTag);
//                SearchGoodsTag = new Random().nextInt(100000) + 100000;
//                postUserLoc(QUERY_REFERSH);
//            }
//        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GoodsDetail> selectGoods = searchGoodsAdapter.getSelectGoods();
                Intent intent = new Intent();
                Gson gs = new Gson();
                intent.putExtra("selectGoods", gs.toJson(selectGoods));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
        Wethod.jsonPost(Request.Method.GET, this, SearchGoodsTag, String.format(SubaruConfig.Http.getGoods, locNo, ownerNo, currentPage, pageLength, searchtxt), "", this);
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == SearchGoodsTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                    search_goods_list.onRefreshComplete();
                } else {
                    String jsonArrayStr = jsonObject.getString("ActionResultsList");
                    Gson gs = new Gson();
                    List<GoodsDetail> jsonListObject = gs.fromJson(jsonArrayStr, new TypeToken<List<GoodsDetail>>() {
                    }.getType());
                    LogUtil.print("search goods count:" + jsonListObject.size());
                    if (jsonListObject.size() > 0) {
                        if (isRefresh == 1) {
                            goodsDetails.clear();
                        }
                        /*******************************处理选中的个数***********************************/
                        if(selectGoods != null && selectGoods.size() != 0){
                            for (int i =0;i<selectGoods.size();i++){
                                for (int j=0;j< jsonListObject.size();j++){
                                    if(selectGoods.get(i).getGoodsNo().equals(jsonListObject.get(j).getGoodsNo())){
                                        jsonListObject.get(j).setGoodsOrderQty(jsonListObject.get(j).getGoodsOrderQty() + selectGoods.get(i).getGoodsOrderQty());
                                    }
                                }
                            }
                        }
                        /*******************************************************************************/

                        goodsDetails.addAll(jsonListObject);
                    } else if (jsonListObject.size() == 0 && isRefresh == 1) {
                        goodsDetails.clear();
                    }
                    dataChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_code_goods_filter && resultCode == RESULT_OK){
            searchtxt = data.getStringExtra("QueryCondition");
            goodsFilter = (GoodsFilter)data.getSerializableExtra("goodsFilter");
            postUserLoc(QUERY_REFERSH);
        }
    }

    private void dataChanged() {

        searchGoodsAdapter.notifyDataSetChanged();
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
            case R.id.iv_sure:
                break;
        }
    }
}
