package com.yk.silence.networkutil.bus;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.yk.silence.networkutil.NetworkCallbackImpl;
import com.yk.silence.networkutil.receiver.NetStatusReceiver;

public class NetStatusBus {

    private Application application;
    private NetStatusReceiver receiver;

    public NetStatusBus() {
        receiver = new NetStatusReceiver();
    }

    private static class HolderClass {
        private static final NetStatusBus instance = new NetStatusBus();
    }

    public static NetStatusBus getInstance() {
        return HolderClass.instance;
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("application is empty");
        }
        return application;
    }


    @SuppressLint("MissingPermission")
    public void init(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("application is empty");
        }
        this.application = application;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl(receiver);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager manager = (ConnectivityManager) NetStatusBus
                    .getInstance().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                manager.registerNetworkCallback(request, networkCallback);
            }
        }

    }

    /**
     * 注册
     */
    public void register(Object mContext) {
        if (application == null) {
            throw new IllegalArgumentException("you must NetStatusBus.getInstance().init(getApplication) first");
        }
        receiver.registerObserver(mContext);
    }

    /**
     * 反注册
     */
    public void unregister(Object mContext) {
        receiver.unRegisterObserver(mContext);
    }

    public void unregisterAllObserver() {
        receiver.unRegisterAllObserver();
    }
}
