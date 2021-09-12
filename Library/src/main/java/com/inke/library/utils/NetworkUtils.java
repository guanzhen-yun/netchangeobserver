package com.inke.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.inke.library.NetworkManager;
import com.inke.library.type.NetType;

public class NetworkUtils {
    /**
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager) NetworkManager.getDefault().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr == null) return false;
        //返回所有网络信息
        NetworkInfo[] info = connMgr.getAllNetworkInfo();
        if(info != null) {
            for (NetworkInfo anInfo : info) {
                if(anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前的网络类型:<br/> -1:没有网络 <br/>1: WIFI网络<br/>2: wap 网络<br/>3: net网络
     */
    @SuppressLint("MissingPermission")
    public static NetType getNetType() {
        ConnectivityManager connMgr = (ConnectivityManager) NetworkManager.getDefault().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr == null) return NetType.NONE;
        //获取当前激活的网络连接信息
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null) {
            return NetType.NONE;
        }
        int nType = networkInfo.getType();

        if(nType == ConnectivityManager.TYPE_MOBILE) {
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return NetType.CMNET;
            } else {
                return NetType.CMWAP;
            }
        } else if(nType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

    /**
     *
     */
}
