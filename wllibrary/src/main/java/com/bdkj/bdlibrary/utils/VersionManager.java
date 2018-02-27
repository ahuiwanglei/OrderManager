/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bdkj.bdlibrary.model.VersionInfo;
import com.bdkj.library.R;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.File;

/**
 * 版本更新管理器
 */
public class VersionManager {

    private final Context mContext;

    private final VersionInfo version;

    private VersionUpdateListener listener;

    public static final int DOWNLOAD_ERROR = 3;

    /* 下载中 */
    public static final int DOWNLOAD = 4;

    /* 下载结束 */
    public static final int DOWNLOAD_FINISH = 5;

    public static final int SHOW_DOWNLOAD = 6;

    public static final int UPDATE_TIP = 7;

    private Dialog noticeDialog = null;
    private Dialog downloadDialog = null;
    private ProgressBar progressBar = null;
    private int progress = 0;

    public final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD_ERROR:
                    showErrorDialog();
                    break;

                case DOWNLOAD:
                    progressBar.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    File installFile = new File(version.getDirPath()
                            + "/"
                            + AppUtils.getApplicationName(mContext)
                            + "_"
                            + version.getNewVersion() + ".apk");
                    if (installFile.exists()) {
                        PackageUtils.installApk(mContext, installFile.getAbsolutePath());
                        if (listener != null) {
                            listener.updateFinish(version.isForce());
                        }
                        return;
                    }
                    break;
                case SHOW_DOWNLOAD:
                    File apkFile = new File(version.getDirPath()
                            + "/"
                            + AppUtils.getApplicationName(mContext)
                            + "_"
                            + version.getNewVersion() + ".apk");
                    if (!apkFile.getParentFile().exists()) {
                        FileUtils.createFolder(apkFile.getParentFile());
                    }
                    if (apkFile.exists()) {
                        FileUtils.deleteFile(apkFile);
                    }
                    showDownload();
                    break;
                case UPDATE_TIP:
                    showNotice();
                    break;
            }
        }

        ;
    };

    public VersionManager(Context mContext, VersionInfo info) {
        this.mContext = mContext;
        this.version = info;
    }

    public void setOnVersionUpdateListener(VersionUpdateListener listener) {
        this.listener = listener;
    }

    public void updateVersion() {
        mHandler.sendEmptyMessage(UPDATE_TIP);
    }

    private void showErrorDialog() {
        noticeDialog = DialogUtils.showAlertDialog(mContext, mContext.getString(R.string.check_version_dialog_title), mContext.getString(R.string.soft_version_update_fail), mContext.getString(R.string.soft_version_update_fail_positive), mContext.getString(version.isForce() ? R.string.soft_version_update_fail_exit
                : R.string.soft_version_update_fail_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(SHOW_DOWNLOAD);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.updateCancel(version.isForce());
                }
            }
        }, Gravity.CENTER);
        noticeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.updateCancel(version.isForce());
                }
            }
        });
    }

    private void showNotice() {

        Resources resources = mContext.getResources();
        String message = resources.getString(R.string.check_version_dialog_update_message, version.getCurVersion(), version.getNewVersion())
                + (version.getDescription() == null
                || version.getDescription().equals("") ? ""
                : resources.getString(R.string.check_version_dialog_update_message_add, version.getDescription()));
        if (!version.isForce()) {

            noticeDialog = DialogUtils.showConfirm(mContext, resources.getString(R.string.check_version_dialog_title), message, resources.getString(R.string.check_version_dialog_update_goto), new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    mHandler.sendEmptyMessage(SHOW_DOWNLOAD);

                }
            }, resources.getString(R.string.check_version_dialog_update_negative), new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // TODO Auto-generated method stub
                    if (listener != null) {
                        listener.updateCancel(version.isForce());
                    }
                }
            });
        } else {
            noticeDialog = DialogUtils.showAlert(mContext, resources.getString(R.string.check_version_dialog_title), message, resources.getString(R.string.check_version_dialog_update_goto), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.sendEmptyMessage(SHOW_DOWNLOAD);
                }
            });
        }
        noticeDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
                if (listener != null) {
                    listener.updateCancel(version.isForce());
                }

            }
        });
        // noticeDialog.getWindow().setGravity(Gravity.LEFT);
        noticeDialog.show();

    }

    public void showDownload() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.softupdate_progress, null);
        progressBar = (ProgressBar) view.findViewById(R.id.update_progress);

        downloadDialog = new Dialog(mContext, R.style.my_dialog);
        downloadDialog.setContentView(view);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(R.string.check_version_dialog_title);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadFile();
    }

    // 下载文件
    private void downloadFile() {
        File downloadFile = new File(version.getDirPath()
                + "/"
                + AppUtils.getApplicationName(mContext)
                + "_"
                + version.getNewVersion() + ".apk_cache");
        FileUtils.deleteFile(downloadFile);
        final File saveFile = new File(version.getDirPath()
                + "/"
                + AppUtils.getApplicationName(mContext)
                + "_"
                + version.getNewVersion() + ".apk");
        HttpUtils.get(mContext, version.getUrl(), new FileAsyncHttpResponseHandler(downloadFile) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

                // TODO Auto-generated method stub
                file.renameTo(saveFile);
                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, File file) {

                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {

                // TODO Auto-generated method stub
                super.onProgress(bytesWritten, totalSize);
                progress = bytesWritten * 100 / totalSize;
                mHandler.sendEmptyMessage(DOWNLOAD);
            }

            @Override
            public void onFinish() {

                // TODO Auto-generated method stub
                super.onFinish();
                if (downloadDialog != null) {
                    downloadDialog.dismiss();
                    downloadDialog = null;
                }
            }
        });
    }


    public interface VersionUpdateListener {

        public void updateCancel(boolean isForce);// 错误对话框被取消

        public void updateFinish(boolean isForce);// 下载完成，安装apk
    }

}
