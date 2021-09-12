package com.inke.netchange;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.inke.library.NetworkManager;
import com.inke.library.annotation.Network;
import com.inke.library.listener.NetChangeObserver;
import com.inke.library.type.NetType;
import com.inke.library.utils.Constants;

public class MainOldActivity extends AppCompatActivity implements NetChangeObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第一种方式 接口实现
//        NetworkManager.getDefault().setListener(this);
    }

    @Override
    public void onConnect(NetType netType) {
        Log.e(Constants.LOG_TAG, netType.name());
    }

    @Override
    public void onDisConnect() {
        Log.e(Constants.LOG_TAG, "onDisConnect");
    }
}