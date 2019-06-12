package com.yk.silence.networkutil.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.yk.silence.networkutil.bus.NetStatusBus;
import com.yk.silence.networkutil.type.NetType;

@SuppressLint("MissingPermission")
public class NetworkUtils {
    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetStatusBus.getInstance()
                .getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }


        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取当前的网络类型
     */
    public static NetType getNetType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) NetStatusBus.getInstance()
                .getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return NetType.TYPE_NONE;
        }
//        获取当前激活的网络连接信息
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return NetType.TYPE_NONE;
        }
        int type = info.getType();

        if (type == ConnectivityManager.TYPE_MOBILE) {

            return NetType.TYPE_MOBILE;

        } else if (type == ConnectivityManager.TYPE_WIFI) {
            return NetType.TYPE_WIFI;
        }
        return NetType.TYPE_NONE;
    }


    /**
     * 打开网络设置界面
     */
    public static void openSetting(Context context, int requestCode) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

}
