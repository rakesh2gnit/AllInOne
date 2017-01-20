package com.mycollection.rakesh.mycollection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by rakesh on 11/11/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView toolbar_title;
    public ProgressDialog mProgressDialog;

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        /*toolbar_menu = (TextView) toolbar.findViewById(R.id.toolbar_menu);
        toolbar_subtitle = (TextView) toolbar.findViewById(R.id.toolbar_subtitle);
        toolbar_search = (EditText) toolbar.findViewById(R.id.toolbar_search);
        toolbar_iv_search = (ImageView) toolbar.findViewById(R.id.toolbar_iv_search);*/
        /*toolbar_title.setTypeface(FontManager.getOpenSansLightFont(this));
        toolbar_subtitle.setTypeface(FontManager.getOpenSansLightFont(this));
        toolbar_menu.setTypeface(FontManager.getOpenSansSemiBoldFont(this));*/
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        //toolbar.setTitle(getTitle());

        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}