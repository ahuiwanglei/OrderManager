package com.ordermanger.online.ordermanager.util.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bdkj.bdlibrary.utils.AppUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.ordermanger.online.ordermanager.MyApplication;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.common.SubaruConfig;
import com.ordermanger.online.ordermanager.util.StringUtil;
import com.ordermanger.online.ordermanager.util.http.AsyncHttpPost;

/**
 * 描述：App更新管理类
 * 
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-29
 */
public class UpdateManager implements AsyncHttpPost.HttpPostCallBack {

	private static final int DOWN_NOSDCARD = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_FINISH = 3;
	private static final int DOWN_ERROR = 4;

	private static final int DIALOG_TYPE_LATEST = 0;
	private static final int DIALOG_TYPE_FAIL = 1;

	public static final String HTTPGETVERSION = "check_version";

	private static UpdateManager updateManager;

	private Activity mContext;
	// 通知对话框
	private Dialog noticeDialog;
	// 下载对话框
	private Dialog downloadDialog;
	// '已经是最新' 或者 '无法获取最新版本' 的对话框
	private Dialog latestOrFailDialog;
	// 进度条
	private ProgressBar mProgress;
	// 显示下载数值
	private TextView mProgressText;
	// 查询动画
	private ProgressDialog mProDialog;
	// 进度值
	private int progress;
	// 下载线程
	private Thread downLoadThread;
	// 终止标记
	private boolean interceptFlag;
	// 提示语
	private String updateMsg = "";
	// 返回的安装包url
	private String apkUrl = "";
	// 下载包保存路径
	private String savePath = "";
	// apk保存完整路径
	private String apkFilePath = "";
	// 临时下载文件路径
	private String tmpFilePath = "";
	// 下载文件大小
	private String apkFileSize;
	// 已下载文件大小
	private String tmpFileSize;

	private AppVersion mUpdate;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_FINISH:
				// 进度条对话框不显示 - 检测结果也不显示
				if (mProDialog != null && !mProDialog.isShowing()) {
					return;
				}
				// 关闭并释放释放进度条对话框
				if (mProDialog != null) {
					mProDialog.dismiss();
					mProDialog = null;
				}
				if (msg.obj != null) {
					try {
						JSONObject object = new JSONObject(msg.obj.toString());
						if (object.getBoolean("isupdate")) {
							updateMsg = object.getString("message");
							apkUrl = object.getString("url");
							MyApplication.setNeedUpdateApk(true);
							showNoticeDialog();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mProgressText.setText(tmpFileSize + "/" + apkFileSize);
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			case DOWN_NOSDCARD:
				downloadDialog.dismiss();
				ToastUtils.show(mContext, "无法下载安装文件，请检查SD卡是否挂载");
				break;
			case DOWN_ERROR:
				ToastUtils.show(mContext, "下载失败，请重新下载");
				downloadDialog.dismiss();
				interceptFlag = true;
				break;
			}
		};
	};

	private String getKey(String result) {
		if (StringUtil.isNotEmpty(result)) {
			try {
				JSONObject resultObject = new JSONObject(result);
				if (resultObject.getBoolean("success")) {
					String time = resultObject.getString("jsonString");
					// 启动坐标发送服务
					return time;
				}
			} catch (Exception e) {
				// DialogUtil.toast(this, "异常：" + e.getMessage());
				e.printStackTrace();
			}
		}
		return "0";
	}

	public static UpdateManager getUpdateManager() {
		if (updateManager == null) {
			updateManager = new UpdateManager();
		}
		updateManager.interceptFlag = false;
		return updateManager;
	}

	String updateVersionCode;

	/**
	 * 检查App更新
	 * 
	 * @param context
	 * @param isShowMsg
	 *            是否显示提示消息
	 */
	public void checkAppUpdate(final Activity context, final boolean isShowMsg) {
		this.mContext = context;
		if (isShowMsg) {
			if (mProDialog == null)
				mProDialog = ProgressDialog.show(mContext, null, "正在检测，请稍后...", true, true);
			else if (mProDialog.isShowing() || (latestOrFailDialog != null && latestOrFailDialog.isShowing()))
				return;
		}
		getHttpCheckVer();
	}

	private void getHttpCheckVer() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", AppUtils.getVersionCode(mContext) + "");
//		SubaruPost loginPost = new SubaruPost(this.mContext, SubaruConfig.Http.UpdateAppVersion, params, false);
//		loginPost.post(HTTPGETVERSION + "");
	}

	/**
	 * 显示'已经是最新'或者'无法获取版本信息'对话框
	 */
	private void showLatestOrFailDialog(int dialogType) {
		if (latestOrFailDialog != null) {
			// 关闭并释放之前的对话框
			latestOrFailDialog.dismiss();
			latestOrFailDialog = null;
		}
		Builder builder = new Builder(mContext);
		builder.setTitle("系统提示");
		if (dialogType == DIALOG_TYPE_LATEST) {
			builder.setMessage("您当前已经是最新版本");
		} else if (dialogType == DIALOG_TYPE_FAIL) {
			builder.setMessage("无法获取版本更新信息");
		}
		builder.setPositiveButton("确定", null);
		latestOrFailDialog = builder.create();
		latestOrFailDialog.show();
	}

	/**
	 * 显示版本更新通知对话框
	 */
	private void showNoticeDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		StringBuffer sb = new StringBuffer();
		if (updateMsg != null) {
			String[] updateList = updateMsg.replaceAll("\\\\n", "====").split("====");
			for (int i = 0; i < updateList.length; i++) {
				sb.append(updateList[i]);
				if (i != updateList.length - 1) {
					sb.append("\n");
				}
			}

		}
		builder.setMessage(sb.toString());
		builder.setPositiveButton("立即更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();

			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("正在下载新版本");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		mProgressText = (TextView) v.findViewById(R.id.update_progress_text);
		mProgressText.setText("检测到新版本");
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = builder.create();
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				String apkName = "MJdDaApp_" + ".apk";
				String tmpApk = "MJdDaApp_" + ".tmp";
				// 判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();
				if (storageState.equals(Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + SubaruConfig.WY_UPDATE_APK_PATH;
					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdirs();
					}
					apkFilePath = savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}

				// 没有挂载SD卡，无法下载文件
				if (apkFilePath == null || apkFilePath == "") {
					mHandler.sendEmptyMessage(DOWN_NOSDCARD);
					return;
				}

				File ApkFile = new File(apkFilePath);

				// //是否已下载更新文件
				// if(ApkFile.exists()){
				// downloadDialog.dismiss();
				// installApk();
				// return;
				// }

				// 输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);

				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Content-type", "text/html");
				conn.setRequestProperty("Accept-Charset", "utf-8");
				conn.setRequestProperty("contentType", "utf-8");
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				// 显示文件大小格式：2个小数点显示
				DecimalFormat df = new DecimalFormat("0.00");
				// 进度条下面显示的总文件大小
				apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					// 进度条下面显示的当前下载文件大小
					tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
					// 当前进度值
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成 - 将临时下载文件转成APK文件
						if (tmpFile.renameTo(ApkFile)) {
							// 通知安装
							mHandler.sendEmptyMessage(DOWN_OVER);
						}
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = DOWN_ERROR;
				mHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = DOWN_ERROR;
				mHandler.sendMessage(msg);
			}

		}
	};

	/**
	 * 下载apk
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(apkFilePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		MyApplication.setNeedUpdateApk(false);
	}

	@Override
	public void callBackFunction(String result, String requestCode) {
		if (requestCode.equals(HTTPGETVERSION + "")) {
			Message msg = new Message();
			msg.what = DOWN_FINISH;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	}

}
