package com.ordermanger.online.ordermanager.util;

import com.android.volley.VolleyError;

/**
 * Created by 涂有泽 .
 * Date by 2016/3/19
 * Use by
 */
public interface VolleyCallBack<Object>{
    void onSuccess(int reqcode, Object result);
    void onFailed(int reqcode, Object result);
    void onError(VolleyError volleyError);
}
