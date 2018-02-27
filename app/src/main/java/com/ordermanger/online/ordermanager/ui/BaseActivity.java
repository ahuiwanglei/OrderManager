package com.ordermanger.online.ordermanager.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.utils.InjectUtils;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.VolleyCallBack;
import com.ordermanger.online.ordermanager.util.http.AsyncHttpPost;

import java.text.SimpleDateFormat;

public abstract class BaseActivity extends Activity implements VolleyCallBack {

	public static final String EXITACTION = "com.online.android.subaru.ui.base.notify.exit";
	private Dialog dialog;

	private TextView tv_title;

	private TextView back;

	private ImageButton actionBtn;

	protected TextView tv_action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InjectUtils.inject(this);

		registerAction(EXITACTION);
	}

	public void displayAction(int vis) {
		actionBtn = (ImageButton) findViewById(R.id.actionBtn);
		if (actionBtn != null) {
			actionBtn.setVisibility(vis);
		}
	}

	public void setBackActionListener(OnClickListener listener) {
		ImageButton menubtn = (ImageButton) findViewById(R.id.back);
		if (menubtn != null) {
			menubtn.setOnClickListener(listener);
		}
	}

	public void setActionListener(OnClickListener listener) {
		actionBtn = (ImageButton) findViewById(R.id.actionBtn);
		if (actionBtn != null) {
			actionBtn.setVisibility(View.VISIBLE);
			actionBtn.setOnClickListener(listener);
		}
	}

	public void displayTextAction(String text, OnClickListener listener) {
		tv_action = (TextView) findViewById(R.id.tv_action);
		if (tv_action != null) {
			tv_action.setText(text);
			displayAction(View.GONE);
			tv_action.setVisibility(View.VISIBLE);
			tv_action.setOnClickListener(listener);
		}
	}

	public void callBackFunction(String result, String requestCode) {
		LogUtil.print("requestCode=" + requestCode);
		LogUtil.print("result:" + result);
	}

	public void setTitle(String title) {
		this.tv_title = (TextView) findViewById(R.id.title);
		if (this.tv_title != null) {
			this.tv_title.setText(title);
		}
	}

	public void initBackBtn() {
		this.back = (TextView) findViewById(R.id.back);
		if (this.back != null) {
			this.back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BaseActivity.this.finish();
				}
			});
		}
	}


	public static String dateToStrLong(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	// 注册
	private void registerAction(String action) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(action);
		this.registerReceiver(exitActivity, filter);
	}
	
	public void logoutApp(){
		Intent intent = new Intent();
		intent.setAction(EXITACTION);
		sendBroadcast(intent);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(exitActivity);
		cancelDialog();
		super.onDestroy();
	}

	public void cancelDialog() {
		if (dialog != null)
			dialog.cancel();
	}

	public void showDialog(String message) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if(!this.isDestroyed() && !this.isFinishing())
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
                dialog = null;
            }
		}
		dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.checking_new_version_layout);
		TextView textView = (TextView) dialog.findViewById(R.id.checking_new_version_textView);
		textView.setText(message);
		dialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		cancelDialog();
	}

	/**
	 * ScrollView 中嵌套ListView
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + listView.getDividerHeight();
		listView.setLayoutParams(params);
	}

	private BroadcastReceiver exitActivity = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (EXITACTION.equals(action)) {
//				if (!(BaseActivity.this instanceof LoginActivity)) {
					
					BaseActivity.this.finish();
					Intent i = new Intent();
					i.setClass(BaseActivity.this, LoginActivity.class);
					BaseActivity.this.startActivity(i);
//				}
			}
		}
	};
}
