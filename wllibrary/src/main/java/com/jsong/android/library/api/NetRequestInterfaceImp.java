package com.jsong.android.library.api;

import com.lidroid.xutils.utils.LogUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class NetRequestInterfaceImp implements NetRequestInterface {

	int repetcount = 0;
	boolean ispost = false;

	public void dorequest(final NetParameters params,
			final NetResponseListener responselistener, final Context context,
			final int tag) {
		final String url = params.getParam("url");
		if (params.getParam(NetRequestInterface.REQUESTYPE).equals("post")) {
			ispost = true;
		}
		params.removeParam(NetRequestInterface.REQUESTYPE);
		params.removeParam("url");
		Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				try {
  
					// 请求成功
					if (msg.what == 1) {
						LogUtils.e("tag=" + tag + " 返回：" + msg.obj.toString());
						responselistener.onComplete(msg.obj, tag);
					}
					// 请求异常
					if (msg.what == 2) {
						LogUtils.e("tag=" + tag + " 返回："
								+ ((Exception) msg.obj).getMessage());
						if (repetcount < 2) {
							NetAndroidClient.getNetAndroidClient(context).api(
									params, url, this, false);
							repetcount++;
						} else {
							responselistener.onException((Exception) msg.obj,
									tag);
						}

					}
					super.handleMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		NetAndroidClient.getNetAndroidClient(context).api(params, url, handler,
				ispost);

	}

}
