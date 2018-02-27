/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package com.bdkj.bdlibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 网络检测工具类
 * 需要添加的权限有
 * <uses-permissionandroid:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <ul>
 * <li>{@link #wifiConnect(android.content.Context)}</li>
 * <li>{@link #networkConnect(android.content.Context)}</li>
 * <li>{@link #checkNetworkInfo(android.content.Context)}</li>
 * <li>{@link #getNetworkState(android.content.Context)}</li>
 * <li>{@link #getMacAddress(android.content.Context)}</li>
 * <li>{@link #getRealIp()}</li>
 * <li>{@link #getLocalIpAddress()}</li>
 * </ul>
 * Created by macchen on 15/3/26.
 */
public class NetworkUtils {
    /**
     * 网络未连接
     */
    public final static int NONE_CONNECT_STATE = 0;

    /**
     * wifi连接
     */
    public final static int WIFI_CONNECT_STATE = 1;

    /**
     * 流量网络连接
     */
    public final static int NETWORK_CONNECT_STATE = 2;

    /**
     * 流量、wifi网络
     */
    public final static int WIFI_NETWORK_CONNECT_STATE = 3;

    /**
     * 禁止构造
     */
    private NetworkUtils() {
        throw new AssertionError();
    }

    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public static boolean wifiConnect(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();
        int length = networkInfo.length;
        int i;
        for (i = 0; i < length; i++) {
            if (networkInfo[i].getTypeName().equals("WIFI")
                    && networkInfo[i].isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断移动网是否连接
     *
     * @return
     */
    public static boolean networkConnect(Context context) {

        boolean flag = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State network_state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null ? connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                : null;
        if (network_state == NetworkInfo.State.CONNECTED
                || network_state == State.CONNECTING) {
            flag = true;
        }
        return flag;
    }

    /**
     * 检查网络是否已连接
     *
     * @param context 上下文
     * @return
     */
    public static boolean checkNetworkInfo(Context context) {

        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        boolean isConnected = false;

        // 如果3G网络和wifi网络都连接
        if (mobile == State.CONNECTED || mobile == State.CONNECTING
                || wifi == State.CONNECTING || wifi == State.CONNECTED) {
            isConnected = true;
        }
        return isConnected;
    }

    /**
     * 获取当前的网络类型
     *
     * @return
     */
    public static int getNetworkState(Context context) {

        int state = 0;
        if (wifiConnect(context)) {
            if (networkConnect(context)) {
                state = WIFI_NETWORK_CONNECT_STATE;
            } else {
                state = WIFI_CONNECT_STATE;
            }
        } else {
            if (networkConnect(context)) {
                state = NETWORK_CONNECT_STATE;
            } else {
                state = NONE_CONNECT_STATE;
            }
        }
        return state;
    }

    /**
     * 获取网卡的mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String address = "";
        if (info != null) {
            address = info.getMacAddress();
        }
        return address == null ? "" : address;
    }

    /**
     * 获取真实的ip地址
     *
     * @return
     * @throws SocketException
     */
    public static String getRealIp() throws SocketException {

        String localip = null;// 当地的Ip地址
        String netip = null;// 网络ip地址

        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;//
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {
                    localip = ip.getHostAddress();
                }
            }
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    /**
     * 获取本地的ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            String ipv4;
            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
                        return ipv4;
                    }
                }

            }

        } catch (SocketException ex) {
            Log.e("d", ex.toString());
        }
        return "";
    }
}
