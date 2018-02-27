package com.ordermanger.online.ordermanager.util.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class AsyncHttpPost extends AsyncTask<String, Void, String> {
	private static String JSESSIONID;
	private String url;
	private List<NameValuePair> params;
	public String backString = "";
	private String requestCode;
	private HttpPostCallBack handler;

	private long starTime;

	public AsyncHttpPost(HttpPostCallBack ctx, String url, List<NameValuePair> params) {
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
			requestCode = params[0];
		}
		this.backString = getHttpPostResult(url, this.params);
		return this.backString;
	}

	@Override
	protected void onPostExecute(String result) {
		if (handler != null)
			handler.callBackFunction(result, requestCode);
		System.out.println("Url:" + url + " ---------> " + (System.currentTimeMillis() - starTime));
		if (params != null)
			for (NameValuePair key : params) {
				System.out.println("Url:" + key.getName() + ":" + key.getValue());
			}
	}

	private String getHttpPostResult(String url, List<NameValuePair> values) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, HttpCommon.HTTP.TIMEOUT);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, HttpCommon.HTTP.TIMEOUT);
		HttpPost httpPost = new HttpPost(url);
		HttpContext context = new BasicHttpContext();
		CookieStore cookieStore = new BasicCookieStore();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		try {
			if (values != null) {
				HttpEntity httpEntity = new UrlEncodedFormEntity(values, "UTF-8");
				httpPost.setEntity(httpEntity);
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpResponse response = null;
		try {
			if (null != JSESSIONID) {
				httpPost.setHeader("Cookie", "ASP.NET_SessionId=" + JSESSIONID);
			}
			response = httpClient.execute(httpPost, context);
			List<Cookie> cookies = cookieStore.getCookies();
			if (!cookies.isEmpty()) {
				for (int i = cookies.size(); i > 0; i--) {
					Cookie cookie = cookies.get(i - 1);
					if (cookie.getName().equalsIgnoreCase("ASP.NET_SessionId")) {
						JSESSIONID = cookie.getValue();
					}
				}
			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public interface HttpPostCallBack {
		/*
		 * result 请求的返回结果requestCode 请求标识符
		 */
		public void callBackFunction(String result, String requestCode);
	}

}
