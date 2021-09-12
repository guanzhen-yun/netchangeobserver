package com.inke.netchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.inke.library.NetworkManager;
import com.inke.library.annotation.Network;
import com.inke.library.listener.NetChangeObserver;
import com.inke.library.type.NetType;
import com.inke.library.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getDefault().register(this);
    }

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG, "WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.e(Constants.LOG_TAG, netType.name());
                break;

            case NONE:
                Log.e(Constants.LOG_TAG, "没有网络");
                break;
        }
    }
    
}