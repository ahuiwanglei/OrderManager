package com.ordermanger.online.ordermanager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.ui.LoginActivity;
import com.ordermanger.online.ordermanager.util.CrashHandler;
import com.ordermanger.online.ordermanager.util.SharedPreferenceUtils;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/15.
 */

public class MyApplication extends Application {
    public static RequestQueue requestQueue;

    private ArrayList<Activity> activities = new ArrayList<Activity>();
    private static MyApplication instance;
    private static boolean is_need_update;
    @Override
    public void onCreate() {
        super.onCreate();
//        MyException.getInstance().init(getApplicationContext());
//        CrashHandler.getInstance().init(getApplicationContext());
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext

        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueue() {
        return requestQueue;
    }

    //单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }

        return instance;
    }
    public void exit() {
        try {
            for (Activity activity : activities) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }


    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    //从容器中删除Activity
    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void setNeedUpdateApk(boolean bool) {
        is_need_update = bool;
    }

    public static boolean isReviewRole(Context ctx){
        String roleKey = SharedPreferenceUtils.getString(ctx, SubaruConfig.CONFIGNAME, SubaruConfig.UserRole);
        return "999".equals(roleKey);
    }
}
