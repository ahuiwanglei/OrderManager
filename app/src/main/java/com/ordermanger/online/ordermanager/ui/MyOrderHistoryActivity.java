package com.ordermanger.online.ordermanager.ui;

import android.os.Bundle;

import com.android.volley.VolleyError;
import com.lidroid.xutils.annotation.ContentView;
import com.ordermanger.online.ordermanager.R;

/**
 * Created by admin on 2017/5/20.
 */
@ContentView(R.layout.activity_myorder_history)
public class MyOrderHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
