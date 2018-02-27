package com.ordermanger.online.ordermanager.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.model.GoodsDetail;
import com.ordermanger.online.ordermanager.model.OrderDetail;
import com.ordermanger.online.ordermanager.util.DensityUtil;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.StringUtil;
import com.ordermanger.online.ordermanager.util.Wethod;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 2017/5/21.
 */
@ContentView(R.layout.activity_order_detail_show)
public class OrderDetailShowActivity extends BaseActivity {

    private static final int OrderDetailTag = 2;

    OrderDetail orderDetail;

    @ViewInject(R.id.tv_DeptNo)
    TextView tv_DeptNo;
    @ViewInject(R.id.tv_LocNo)
    TextView tv_LocNo;
    @ViewInject(R.id.tv_OwnerNo)
    TextView tv_OwnerNo;
    @ViewInject(R.id.tv_SheetId)
    TextView tv_SheetId;
    @ViewInject(R.id.tv_SheetType)
    TextView tv_SheetType;
    @ViewInject(R.id.tv_Memo)
    TextView tv_Memo;

    @ViewInject(R.id.linear_goods)
    LinearLayout linear_goods;

    boolean isReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBackBtn();
        setTitle("订单详情");
        isReview = getIntent().getBooleanExtra("isReview", false);
        orderDetail = (OrderDetail) getIntent().getSerializableExtra("order");
        tv_DeptNo.setText(orderDetail.getDeptName()+"(" + orderDetail.getDeptNo() +")");
        tv_LocNo.setText(orderDetail.getLocName() +" (" + orderDetail.getLocNo()+")");
        tv_OwnerNo.setText(orderDetail.getOwnerName()+ " (" +  orderDetail.getOwnerNo()+")");
        tv_SheetId.setText(orderDetail.getSheetID());
        if(StringUtil.isNotEmpty(orderDetail.getSheetType())){
            String[] mItems = getResources().getStringArray(R.array.order_type);
            int index = Integer.parseInt(orderDetail.getSheetType());
            if(index >=0 && index < mItems.length){
                tv_SheetType.setText(mItems[index]);
            }
        }

        tv_Memo.setText(orderDetail.getMemo());

        getOrderDetail();
    }

    private void getOrderDetail() {
        String LocNo = Des.encrypt(orderDetail.getLocNo());
        String OwnerNo = Des.encrypt(orderDetail.getOwnerNo());
        String SheetID = Des.encrypt(orderDetail.getSheetID());
        String method = "";
        if(isReview){
            method =  String.format(SubaruConfig.Http.getOrderDetailUnAduit, LocNo, OwnerNo, SheetID);
        }else{
            method =  String.format(SubaruConfig.Http.getOrderDetail, LocNo, OwnerNo, SheetID);
        }
        Wethod.jsonPost(Request.Method.GET, this, OrderDetailTag, method , "", this);
    }

    @Override
    public void onSuccess(int reqcode, Object result) {
        if (reqcode == OrderDetailTag) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.toString());
                if(jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode){
                    ToastUtils.show(this, "未获取到商品信息");
                }else{
                    String jsonArrayStr = jsonObject.getString("ActionResultsList");
                    Gson gs = new Gson();
                    List<GoodsDetail> jsonListObject = gs.fromJson(jsonArrayStr, new TypeToken<List<GoodsDetail>>(){}.getType());
                    LogUtil.print("search goods count:" + jsonListObject.size());
                    initGoodsViews(jsonListObject);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void initGoodsViews(List<GoodsDetail> goods) {
        for (int i=0;i< goods.size();i++){
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_goods_item, null);
            TextView tv_GoodsName = (TextView) view.findViewById(R.id.tv_GoodsName);
            TextView tv_GoodsNo = (TextView)view.findViewById(R.id.tv_GoodsNo);
            TextView tv_GoodsPackQty = (TextView)view.findViewById(R.id.tv_GoodsPackQty);
            TextView tv_GoodsPackUnit = (TextView)view.findViewById(R.id.tv_GoodsPackUnit);
            TextView tv_GoodsOrderQty = (TextView)view.findViewById(R.id.tv_GoodsOrderQty);
            TextView tv_GoodsSellPrice = (TextView)view.findViewById(R.id.tv_GoodsSellPrice);
            tv_GoodsName.setText(goods.get(i).getGoodsName());
            tv_GoodsNo.setText("(" + goods.get(i).getGoodsNo()+")");
            tv_GoodsPackQty.setText(goods.get(i).getGoodsPackQty());
            tv_GoodsPackUnit.setText(goods.get(i).getGoodsPackUnit());
            tv_GoodsOrderQty.setText(goods.get(i).getGoodsOrderQty()+"");
            tv_GoodsSellPrice.setText(goods.get(i).getGoodsSellPrice());
            linear_goods.addView(view);
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
