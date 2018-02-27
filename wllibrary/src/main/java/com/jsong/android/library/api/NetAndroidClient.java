/**
 * 
 */
package com.jsong.android.library.api;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;

import com.lidroid.xutils.utils.LogUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 客户端实现，封装了API调用<br>
 * 
 * 
 */
public class NetAndroidClient {
	private Context context;
	private static NetAndroidClient to8ToAndroidClient;
	private int connectTimeout = 10000;// 10
	private int readTimeout = 30000;// 30

	public static NetAndroidClient getNetAndroidClient(Context context) {
		if (to8ToAndroidClient == null) {
			init(context);
		}
		return to8ToAndroidClient;
	}

	private static void init(Context context) {
		to8ToAndroidClient = new NetAndroidClient();
		to8ToAndroidClient.setContext(context);
	}

	private NetAndroidClient() {
	}

	/**
	 * 
	 * @param params
	 *            系统及业务参
	 * 
	 * @param listener
	 *            api调用回调处理监听器，不能为null
	 * 
	 * @throws IllegalArgumentException
	 *             当参code>params</code>�?code>listener</code>为null
	 */
	public void api(final NetParameters params, final String hosturl,
			final Handler handler, final boolean ispostmethod) {
		if (params == null) {
			throw new IllegalArgumentException("params must not null.");
		}
		if (handler == null) {
			throw new IllegalArgumentException("listener must not null.");
		}

		new Thread() {
			@Override
			public void run() {
				invokeApi(params, handler, hosturl, ispostmethod);
			}
		}.start();

	}

	@SuppressWarnings("static-access")
	private void invokeApi(NetParameters params, Handler handler,
			String hosturl, boolean ispostmethod) {
		try {
			if (ispostmethod) {
				String jsonStr = WebUtils.doPost(context, hosturl,
						this.generateApiParams(params),
						params.getAttachments(), connectTimeout, readTimeout);
				handleApiResponse(handler, jsonStr);
				LogUtils.e(jsonStr);
			} else {
				String jsonStr = WebUtils.doGet(context, hosturl,this.generateApiParams(params));
				LogUtils.e(jsonStr);
				handleApiResponse(handler, jsonStr);
			}
		} catch (Exception e) {
			Message msgMessage = handler.obtainMessage();
			msgMessage.obj = e;
			msgMessage.what = 2;
			handler.sendMessage(msgMessage);
		}
	}

	private void handleApiResponse(Handler handler, String jsonStr)
			throws JSONException {
		Message msgMessage = handler.obtainMessage();
		msgMessage.obj = jsonStr;
		msgMessage.what = 1;
		handler.sendMessage(msgMessage);

	}

	public static Map<String, String> generateApiParams(
			NetParameters topParameters) throws IOException {
		TreeMap<String, String> params = new TreeMap<String, String>();
		Map<String, String> map = topParameters.getParams();
		if (map != null) {
			Set<Entry<String, String>> set = map.entrySet();
			for (Entry<String, String> entry : set) {
				params.put(entry.getKey(), entry.getValue());
			}
		}

		return params;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

}
