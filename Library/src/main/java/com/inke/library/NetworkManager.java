package com.inke.library;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.inke.library.core.NetworkCallbackImpl;
import com.inke.library.listener.NetChangeObserver;
import com.inke.library.utils.Constants;

/**
 * 网络管理类
 */
public class NetworkManager {
    private static volatile NetworkManager instance;
    private Application application;
//    private NetStateReceiver2 receiver2;
private NetStateReceiver receiver;

    private NetworkManager() {
//        receiver2 = new NetStateReceiver2();
        receiver = new NetStateReceiver();
    }

//    public void setListener(NetChangeObserver listener) {
//        this.receiver2.setListener(listener);
//    }

    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public void register(Object object) {
        receiver.register(object);
    }

    public Application getApplication() {
        if(application == null) {
            throw new RuntimeException("......");
        }
        return application;
    }

    @SuppressLint("MissingPermission")
    public void init(Application application) {
        this.application = application;
        //第三种方式监听，不通过广播
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cmgr != null) cmgr.registerNetworkCallback(request, networkCallback);
            //if(cmgr != null) cmgr.unregisterNetworkCallback(networkCallback);
        } else {
            //动态广播注册
            //第二种方式
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);

            //第一种方式
//        application.registerReceiver(receiver2, filter);
        }
        
    }
}
