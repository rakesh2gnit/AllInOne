package com.mycollection.rakesh.mycollection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by gleecus on 11/22/16.
 */

public class MyCollectionApp extends Application {

    //add this variable declaration:
    public static String somevalue = "Hello from application singleton!";
    private String pref_key = "MyCollectionApp";
    private int data = 200;
    private String name;
    private String email;
    private static MyCollectionApp singleton;
    SharedPreferences mPref;

    protected String userAgent;//For Exo Player

    public static MyCollectionApp getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        singleton.initializeInstance();
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        //onAppCreated();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void initializeInstance() {
        // Do your application wise initialization task
        screenConfiguration();
        // set application wise preference
        mPref = this.getApplicationContext().getSharedPreferences(pref_key, MODE_PRIVATE);
    }

    // particularly applicable in library projects
    //public abstract void onAppCreated();

    public void screenConfiguration() {
        Configuration config = getResources().getConfiguration();
        boolean isTab = (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        Point size = new Point();
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int deviceScreenWidth;
        int deviceScreenHeight;

        try {
            display.getSize(size);
            deviceScreenWidth = size.x;
            deviceScreenHeight = size.y;
        } catch (NoSuchMethodError e) {
            deviceScreenWidth = display.getWidth();
            deviceScreenHeight = display.getHeight();
        }
    }

    public boolean isFirstRun() {
        // return true if the app is running for the first time
        return mPref.getBoolean("is_first_run", true);
    }

    public void setRunned() {
        // after a successful run, call this method to set first run false
        SharedPreferences.Editor edit = mPref.edit();
        edit.putBoolean("is_first_run", false);
        edit.commit();
    }

    @Override
    public void onTerminate() {
        // Do your application wise Termination task
        super.onTerminate();
    }

    public int getData() {
        return this.data;
    }

    public void setData(int d) {
        this.data = d;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String aEmail) {
        email = aEmail;
    }

    // Calling Application class (see application tag in AndroidManifest.xml)
    //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

    //Set name and email in global/application context
    //globalVariable.setName("Android Example context variable");
    //globalVariable.setEmail("xxxxxx@aaaa.com");

    //For ExoPlayer
    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    //For ExoPlayer
    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    //For ExoPlayer
    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }
}