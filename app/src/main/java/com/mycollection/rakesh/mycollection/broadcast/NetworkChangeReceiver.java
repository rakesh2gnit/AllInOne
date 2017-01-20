package com.mycollection.rakesh.mycollection.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.util.NetworkUtil;

/**
 * Created by gleecus on 12/5/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);

        Intent intentStatusChanged = new Intent("iNetStatusChanged");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentStatusChanged);

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
