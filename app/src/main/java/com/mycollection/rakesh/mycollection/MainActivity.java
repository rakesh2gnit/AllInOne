package com.mycollection.rakesh.mycollection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.adapter.CollectionAdapter;

import com.mycollection.rakesh.mycollection.model.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakesh on 11/11/16.
 */

public class MainActivity extends BaseActivity implements CollectionAdapter.OnClickListener {

    private List<Collections> collectionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CollectionAdapter mAdapter;
    private BroadcastReceiver iNetChangedReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar_title.setText("List Of Collections");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new CollectionAdapter(collectionList, this, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareCollectionData();

        iNetChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //filterNetworkPostList();
                Toast.makeText(MainActivity.this, "Stop Services", Toast.LENGTH_SHORT).show();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(iNetChangedReceiver, new IntentFilter("iNetStatusChanged"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    private void prepareCollectionData() {
        Collections collection = new Collections("Circular Profile Pics");
        collectionList.add(collection);

        collection = new Collections("Get CountryActivity List");
        collectionList.add(collection);

        collection = new Collections("Login");
        collectionList.add(collection);

        collection = new Collections("Android Files Management in Disk");
        collectionList.add(collection);

        collection = new Collections("Exo Player");
        collectionList.add(collection);

        collection = new Collections("Firebase  Oauth");
        collectionList.add(collection);

        collection = new Collections("Drawer Fragment");
        collectionList.add(collection);

        collection = new Collections("MDTP");
        collectionList.add(collection);

        collection = new Collections("Hash tag");
        collectionList.add(collection);

        collection = new Collections("Permission Checker");
        collectionList.add(collection);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick() {
        //Intent intentChooser = new Intent(PermissionCheckerActivity.this, TabPagerMainActivity.class);
        //startActivity(intentChooser);
        recyclerView.setVisibility(View.GONE);
        /*Fragment fragment = new TabPagerMainActivity();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();*/
        /*fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.slidein, R.anim.slideout).replace(R.id.container, fragment).commit();*/
    }
}
