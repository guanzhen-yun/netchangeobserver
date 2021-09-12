package com.inke.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inke.library.annotation.Network;
import com.inke.library.bean.MethodManager;
import com.inke.library.type.NetType;
import com.inke.library.utils.Constants;
import com.inke.library.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
    //key: MainActivity / Fragment value: 该容器的所有订阅监听网络的方法集合
    private Map<Object, List<MethodManager>> networkList;

    public NetStateReceiver() {
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "异常...");
            return;
        }

        //处理广播的事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.LOG_TAG, "网络发生了变更");
            netType = NetworkUtils.getNetType();//赋值网络的类型

            if (NetworkUtils.isNetworkAvailable()) {
                Log.e(Constants.LOG_TAG, "网络连接成功...");
            } else {
                Log.e(Constants.LOG_TAG, "网络连接失败...");
            }
            post(netType);
        }
    }

    //网络匹配
    private void post(NetType netType) {
        if (networkList.isEmpty()) return;
        Set<Object> set = networkList.keySet();
        //比如获取MainActivity对象
        for (Object getter : set) {
            //获取MainActivity对象中所有订阅方法
            List<MethodManager> methodManagers = networkList.get(getter);
            if (methodManagers != null) {
                for (MethodManager methodManager : methodManagers) {
                    //参数的匹配
                    if(methodManager.getType().isAssignableFrom(netType.getClass())) {
                        switch (methodManager.getNetType()) {
                            case AUTO:
                                invoke(methodManager, getter, netType);
                                break;
                            case WIFI:
                                if(netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            case CMNET:
                            case CMWAP:
                                if(netType == NetType.CMNET || netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManager, Object getter, NetType netType) {
        try {
            methodManager.getMethod().invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void register(Object object) {
        List<MethodManager> methodList = networkList.get(object);
        if (methodList == null) {
            //收集
            methodList = findAnnotationMethod(object);
            networkList.put(object, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }

            //获取方法的返回值,校验一
            //method.getGenericReturnType();
            //获取方法的参数，校验二
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法的参数有且只有一个");
            }

            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodList.add(manager);
        }
        return methodList;
    }
}
