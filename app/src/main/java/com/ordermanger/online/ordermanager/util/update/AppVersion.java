package com.ordermanger.online.ordermanager.util.update;




/** 
 * 描述：程序版本
 * 
 * 作者: xianzheng.xie
 * 
 * 邮箱：xiexianzheng@qq.com
 * 
 * 创建时间: 2014-02-14
 */
public class AppVersion extends Base{

	private static final long serialVersionUID = 1741970662701723555L;

	protected double versionCode;//版本号
	
	protected String versionDesc;//版本说明
	
	protected String downloadURL;//下载地址

	public double getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(double versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	
}
