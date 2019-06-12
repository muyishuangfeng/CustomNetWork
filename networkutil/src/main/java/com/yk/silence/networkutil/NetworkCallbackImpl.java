package com.yk.silence.networkutil;

import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yk.silence.networkutil.receiver.NetStatusReceiver;
import com.yk.silence.networkutil.util.NetworkUtils;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends NetworkCallback {


    private NetStatusReceiver mReceiver;

    public NetworkCallbackImpl(NetStatusReceiver receiver) {
        mReceiver = receiver;
    }


    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        mReceiver.post(NetworkUtils.getNetType());
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        mReceiver.post(NetworkUtils.getNetType());
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
    }

}
