/**
 * 
 */
package com.jsong.android.library.api;

/**
 * API调用的事件监听器
 * 
 * @author junyan.hj
 * 
 */
public interface NetResponseListener {
	/**
	 * API调用成功后返回以String对象方式通知监听
	 * 
	 * @param json
	 */
	void onComplete(Object object, int tag);

	/**
	 * 出现网络问题等未知异常时会回调此方法
	 * 
	 * @param e
	 */
	void onException(Exception e, int tag);
}
