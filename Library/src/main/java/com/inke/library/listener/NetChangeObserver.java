package com.inke.library.listener;

import com.inke.library.type.NetType;

/**
 * 网络监听接口
 */
public interface NetChangeObserver {

    void onConnect(NetType netType);

    void onDisConnect();

}
