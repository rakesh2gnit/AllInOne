package com.mycollection.rakesh.mycollection.networkconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Network;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.BaseActivity;
import com.mycollection.rakesh.mycollection.R;
import com.mycollection.rakesh.mycollection.broadcast.NetworkChangeReceiver;

/**
 * Created by Admin on 22-01-2017.
 */

public class NetworkCheckerActivity extends BaseActivity implements NetworkChangeReceiver.OnNetworkChangeListener {

    private Button button1, button2;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_network;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.isInternetAvailable(NetworkCheckerActivity.this)) {
                    Log.e("connection type:", NetworkUtil.getConnectivityStatusString(NetworkCheckerActivity.this));
                    Log.e("is reachable:", "" + NetworkUtil.isReachable(NetworkCheckerActivity.this));
                    if (NetworkUtil.getConnectivityStatusString(NetworkCheckerActivity.this).equalsIgnoreCase("Wifi enabled") ||
                            NetworkUtil.getConnectivityStatusString(NetworkCheckerActivity.this).equalsIgnoreCase("4g")) {
                        Toast.makeText(NetworkCheckerActivity.this, NetworkUtil.getConnectivityStatusString(NetworkCheckerActivity.this), Toast.LENGTH_SHORT).show();
                        if (NetworkUtil.isReachable(NetworkCheckerActivity.this)) {
                            Log.e("reachable:", "Do your code here.");
                        } else {
                            Log.e("not reachable:", "Check your connection." + NetworkUtil.isReachable(NetworkCheckerActivity.this));
                        }
                    } else {
                        Toast.makeText(NetworkCheckerActivity.this, "Poor network connection.", Toast.LENGTH_SHORT).show();
                        if (NetworkUtil.isReachable(NetworkCheckerActivity.this)) {
                            Log.e("reachable:", "Do your code here.");
                        } else {
                            Log.e("not reachable:", "Check your connection." + NetworkUtil.isReachable(NetworkCheckerActivity.this));
                        }
                    }
                } else
                    Toast.makeText(NetworkCheckerActivity.this, "internet not available", Toast.LENGTH_SHORT).show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NetworkCheckerActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        networkChangeReceiver = new NetworkChangeReceiver();
        networkChangeReceiver.setOnLocationChangeListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onNetworkChanged(String status) {
        Log.e("onNetworkChanged:", status);
        Toast.makeText(NetworkCheckerActivity.this, status, Toast.LENGTH_LONG).show();
    }
}
