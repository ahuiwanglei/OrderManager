/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bdkj.bdlibrary.model.VersionInfo;
import com.bdkj.bdlibrary.utils.VersionManager;

/**
 * Created by macchen on 15/3/26.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // ToastUtils.showInfo(this, "基础信息!!!!!!!!!!!!!!");
       // DialogUtils.showDialog(this,null,null,null,null,null,null);
        //DialogUtils.showAlert(this,"温馨提示","您的手机将在10秒后自动关机!");
        //DialogUtils.showAlertNoTitle(this,"没有标题的信息!");
        //ProgressDialog.show(this, "标题", "正在加载中...");
        //DialogUtils.showLoading(this);
       /* ImageView imageView = new ImageView(this);
        setContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        Drawable mDrawable = getWallpaper();*/
//Make this drawable mutable.
//A mutable drawable is guaranteed to not share its state with any other drawable.
        //mDrawable.mutate();
       /* ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        mDrawable.setColorFilter(cf);
        imageView.setImageDrawable(mDrawable);*/

        VersionInfo info = new VersionInfo();
        info.setCurVersion("10.10");
        info.setDescription("没有描述");
        info.setDirPath("/sdcard/sdfsdf");
        info.setForce(true);
        info.setNewVersion("10.11");
        info.setUpdate(3);
        info.setUrl("http://www.baidu.com");
        VersionManager manager = new VersionManager(this,info);
        manager.updateVersion();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
