package com.ordermanger.online.ordermanager.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;
import android.os.AsyncTask;

public class AsyncHttpGet extends AsyncTask<String, Void, String> {

	private String url;
	private Map<String, String> params;
	public String backString = "";
	private int requestCode;
	private HttpGetCallBack handler;

	private long starTime;

	public AsyncHttpGet(HttpGetCallBack ctx, String url, Map<String, String> params) {
		this.handler = ctx;
		this.url = url;
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[]) requestCode,
	 * 第一个参数，请求标识符
	 */
	@Override
	protected String doInBackground(String... params) {
		starTime = System.currentTimeMillis();
		if (params[0] != null) {
			requestCode = Integer.parseInt(params[0]);
		}
		 try {
			this.backString = HttpRequestUtil.sendGetRequest(url, this.params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.backString;
	}

	@Override
	protected void onPostExecute(String result) {
		if (handler != null)
			handler.callBackFunction(result, requestCode);
		System.out.println("Url:" + url + " ---------> " + (System.currentTimeMillis() - starTime));
		for (Map.Entry<String, String> entry : params.entrySet()) {
			System.out.println("Url:" + entry.getKey() + ":" + entry.getValue());
		}
	}

	public interface HttpGetCallBack {
		/*
		 * result 请求的返回结果requestCode 请求标识符
		 */
		public void callBackFunction(String result, int requestCode);
	}

}
