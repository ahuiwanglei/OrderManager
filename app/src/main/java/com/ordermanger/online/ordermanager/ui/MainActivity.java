package com.ordermanger.online.ordermanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.SharedPreferenceUtils;
import com.ordermanger.online.ordermanager.util.Wethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2017/5/20.
 */

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.linear_add_order)
    LinearLayout linear_add_order;
    @ViewInject(R.id.linear_search_order)
    LinearLayout linear_search_order;
    @ViewInject(R.id.linear_logout)
    LinearLayout linear_logout;
    @ViewInject(R.id.linear_review)
    LinearLayout linear_review;

    private static final int Http_Logout_Tag = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.app_name));
        findViewById(R.id.back).setVisibility(View.GONE);

        linear_add_order.setOnClickListener(this);
        linear_search_order.setOnClickListener(this);
        linear_logout.setOnClickListener(this);
        linear_review.setOnClickListener(this);

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 2017);
//        calendar.set(Calendar.MONTH, 8);
//        calendar.set(Calendar.DAY_OF_MONTH, 5);
//        calendar.set(Calendar.HOUR, 12);
//        calendar.set(Calendar.MINUTE, 0);
//        if(new Date().getTime() > calendar.getTime().getTime()){
//            ToastUtils.show(this, "签名过期， 请重新签名");
//        }
    }


    @Override
    public void onSuccess(int reqcode, Object result) {
        if(reqcode == Http_Logout_Tag){

            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        if(reqcode == Http_Logout_Tag){
            ToastUtils.show(this, "退出失败");
        }
    }

    @Override
    public void onError(VolleyError volleyError) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_add_order:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_search_order:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, SearchOrderActivity.class);
                startActivity(intent2);
                break;
            case R.id.linear_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认退出吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferenceUtils.clear(MainActivity.this, SubaruConfig.CONFIGNAME);
                        showDialog("正在退出...");
                        postLogout();
                   }});
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.linear_review:
                Intent intent23  = new Intent();
                intent23.setClass(MainActivity.this, SearchNotReviewActivity.class);
                startActivity(intent23);
                break;
        }
    }

    private void postLogout() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Worker_No",  SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO));
            jsonObject.put("PassWord", Des.encrypt(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.Pwd)));
            Wethod.jsonPost(Request.Method.POST, this, Http_Logout_Tag, SubaruConfig.Http.getLogout, jsonObject.toString(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
