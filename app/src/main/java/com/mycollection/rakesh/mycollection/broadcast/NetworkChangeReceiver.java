package com.mycollection.rakesh.mycollection.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.networkconnection.NetworkUtil;

/**
 * Created by gleecus on 12/5/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private OnNetworkChangeListener listener = null;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);

        if (listener != null) {
            listener.onNetworkChanged(status);
        }
        //Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }

    public interface OnNetworkChangeListener {
        public void onNetworkChanged(String status);
    }

    public void setOnLocationChangeListener(Context context) {
        this.listener = (OnNetworkChangeListener) context;
    }
}
