package com.ordermanger.online.ordermanager.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.ordermanger.online.ordermanager.MyApplication;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.MD5;
import com.ordermanger.online.ordermanager.util.SharedPreferenceUtils;
import com.ordermanger.online.ordermanager.util.StringBytes;
import com.ordermanger.online.ordermanager.util.StringUtil;
import com.ordermanger.online.ordermanager.util.Wethod;
import com.ordermanger.online.ordermanager.util.update.UpdateManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private static final int LOGIN_TAG = 1;
    private static final int User_Loc_TAG = 2;
    private static final int GetUserOwner_TAG = 3;
    private static final int GetUserDept_TAG = 4;
    private static final int GetUserRole_TAG = 5;

    @ViewInject((R.id.login_pwd_et))
    EditText etPassword;
    @ViewInject((R.id.login_name_et))
    EditText etUsername;

    UpdateManager upManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upManager = UpdateManager.getUpdateManager();
        upManager.checkAppUpdate(LoginActivity.this, false);

        if (StringUtil.isNotEmpty(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO)) && StringUtil.isNotEmpty(SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.TOKENKEY))) {
            goMain();
        } else {
            SharedPreferenceUtils.clear(this, SubaruConfig.CONFIGNAME);
        }

    }

    @OnClick({R.id.login_commit_btn})
    private void login_action(View v) {
        postLogin();
    }

    private void postLogin() {
        SharedPreferenceUtils.clear(this, SubaruConfig.CONFIGNAME);
        if (check()) {
            showDialog("正在登录...");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("WORKER_NO", Des.encrypt(etUsername.getText().toString()));
                jsonObject.put("PassWord", Des.encrypt(MD5.getMd5(etPassword.getText().toString())));
                SharedPreferenceUtils.putString(this, SubaruConfig.CONFIGNAME, SubaruConfig.Pwd, MD5.getMd5(etPassword.getText().toString()));
                Wethod.jsonPost(Request.Method.POST, this, LOGIN_TAG, SubaruConfig.Http.login, jsonObject.toString(), this);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    private void postUserLoc() {
        Wethod.jsonPost(Request.Method.GET, this, User_Loc_TAG, String.format(SubaruConfig.Http.userLoc, SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO)), "", this);
    }

    private void postGetUserOwner() {
        Wethod.jsonPost(Request.Method.GET, this, GetUserOwner_TAG, String.format(SubaruConfig.Http.getUserOwner, SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO)), null, this);
    }

    private void postGetUserRole() {
        String LocNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo);
        String workNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO);
        Wethod.jsonPost(Request.Method.GET, this, GetUserRole_TAG, String.format(SubaruConfig.Http.getUserRole, LocNo, workNo), null, this);
    }

    private void postGetUserDept() {
        String LocNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo);
        String OwnerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo);
        String WorkerNo = SharedPreferenceUtils.getString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO);
        Wethod.jsonPost(Request.Method.GET, this, GetUserDept_TAG, String.format(SubaruConfig.Http.getUserDept, LocNo, OwnerNo, WorkerNo), null, this);
    }


    private boolean check() {
        if (StringUtil.isEmpty(etUsername.getText().toString())) {
            ToastUtils.show(this, "请输入用户名！");
            return false;
        }
        if (StringUtil.isEmpty(etPassword.getText().toString())) {
            ToastUtils.show(this, "请输入密码！");
        }
        return true;

    }

    @Override
    public void onSuccess(int reqcode, Object result) {

        if (reqcode == LOGIN_TAG) {
            handlerLogin(result.toString());
        } else if (reqcode == User_Loc_TAG) {
            hanlderLoc(result.toString());
        } else if (reqcode == GetUserOwner_TAG) {
            handlerUserOwner(result.toString());
        } else if (reqcode == GetUserDept_TAG) {
            handlerUserDept(result.toString());
        } else if (reqcode == GetUserRole_TAG) {
            handlerUserRole(result.toString());
        }
    }

    private void handlerUserRole(String result) {
        //{"ActionResults":0,"ErrorMessage":null,"ActionResultsList":["000"]}
        LogUtil.print(result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                ToastUtils.show(this, "未获取到员工权限");
                cancelDialog();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ActionResultsList");
                SharedPreferenceUtils.putString(LoginActivity.this, SubaruConfig.CONFIGNAME, SubaruConfig.UserRole, jsonArray.getString(0));
                postGetUserOwner();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handlerUserDept(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                ToastUtils.show(this, "未获取到员工部门");
                cancelDialog();
            } else {
                cancelDialog();
                final JSONArray jsonArray = jsonObject.getJSONArray("ActionResultsList");
                String[] mItems = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    mItems[i] = jsonArray.getJSONObject(i).getString("DeptName");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("请选择员工部门");
                builder.setItems(mItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            SharedPreferenceUtils.putString(LoginActivity.this, SubaruConfig.CONFIGNAME, SubaruConfig.DeptNo, Des.encrypt(jsonArray.getJSONObject(which).getString("DeptNo")));
                            SharedPreferenceUtils.putString(LoginActivity.this, SubaruConfig.CONFIGNAME, SubaruConfig.DeptName, jsonArray.getJSONObject(which).getString("DeptName"));
                            goMain();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        cancelDialog();
                        SharedPreferenceUtils.clear(LoginActivity.this, SubaruConfig.CONFIGNAME);

                    }
                });
                builder.create();
                builder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handlerUserOwner(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                ToastUtils.show(this, "未获取到员工业主数据权限");
                cancelDialog();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ActionResultsList");
                hanlderUserOwner(jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hanlderUserOwner(final JSONArray userOwners) throws JSONException {
        String[] mItems = new String[userOwners.length()];
        for (int i = 0; i < userOwners.length(); i++) {
            mItems[i] = userOwners.getJSONObject(i).getString("OwnerName");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择员工业主数据权限");
        builder.setItems(mItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SharedPreferenceUtils.putString(LoginActivity.this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerNo, Des.encrypt(userOwners.getJSONObject(which).getString("OwnerNo")));
                    SharedPreferenceUtils.putString(LoginActivity.this, SubaruConfig.CONFIGNAME, SubaruConfig.OwnerName, userOwners.getJSONObject(which).getString("OwnerName"));
                    showDialog("正在获取部门...");
                    postGetUserDept();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                cancelDialog();
                SharedPreferenceUtils.clear(LoginActivity.this, SubaruConfig.CONFIGNAME);

            }
        });
        builder.create();
        builder.show();
    }

    private void hanlderLoc(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                ToastUtils.show(this, "未获取到员工仓库");
                cancelDialog();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ActionResultsList");
                SharedPreferenceUtils.putString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocNo, Des.encrypt(jsonArray.getJSONObject(0).getString("LocNo")));
                SharedPreferenceUtils.putString(this, SubaruConfig.CONFIGNAME, SubaruConfig.LocName, jsonArray.getJSONObject(0).getString("LocName"));
                postGetUserRole();
//                postGetUserOwner();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handlerLogin(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("ActionResults") == SubaruConfig.Http.HttpErrorCode) {
                cancelDialog();
                ToastUtils.show(this, "用户名或密码错误");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ActionResultsList");
                SharedPreferenceUtils.putString(this, SubaruConfig.CONFIGNAME, SubaruConfig.WORKERNO, Des.encrypt(jsonArray.getJSONObject(0).getString("Worker_No")));
                SharedPreferenceUtils.putString(this, SubaruConfig.CONFIGNAME, SubaruConfig.TOKENKEY, jsonArray.getJSONObject(0).getString("SessionKey"));
                postUserLoc();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(int reqcode, Object result) {
        SharedPreferenceUtils.clear(this, SubaruConfig.CONFIGNAME);
    }

    @Override
    public void onError(VolleyError volleyError) {
        cancelDialog();
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
