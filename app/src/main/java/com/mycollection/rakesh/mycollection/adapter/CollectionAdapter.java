package com.mycollection.rakesh.mycollection.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mycollection.rakesh.mycollection.ChooserActivity;
import com.mycollection.rakesh.mycollection.CircularProfilePicture;
import com.mycollection.rakesh.mycollection.CountryActivity;
import com.mycollection.rakesh.mycollection.FileManagementInAndroid;
import com.mycollection.rakesh.mycollection.HashTagActivity;
import com.mycollection.rakesh.mycollection.MaterialDesignDateTime;
import com.mycollection.rakesh.mycollection.R;
import com.mycollection.rakesh.mycollection.exoplayer.SampleChooserActivity;
import com.mycollection.rakesh.mycollection.model.Collections;
import com.mycollection.rakesh.mycollection.model.Country;
import com.mycollection.rakesh.mycollection.permissionchecker.PermissionCheckerActivity;
import com.mycollection.rakesh.mycollection.retrofit.ApiClient;
import com.mycollection.rakesh.mycollection.retrofit.ApiInterface;
import com.mycollection.rakesh.mycollection.networkconnection.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gleecus on 11/16/16.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private List<Collections> collectionList;
    private Context mContext;
    private OnClickListener onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public View viewContainer;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            viewContainer = (View) view.findViewById(R.id.container);
        }
    }


    public CollectionAdapter(List<Collections> collectionList, Context mCtx, OnClickListener onClickListener) {
        this.collectionList = collectionList;
        this.mContext = mCtx;
        //this.onClickListener = onClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Collections collection = collectionList.get(position);
        final String title = collection.getName();
        holder.title.setText(title);

        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.equalsIgnoreCase("Circular Profile Pics")) {
                    if (!NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("Not connected to Internet")) {
                        //Boolean isHostReachable = NetworkUtil.isReachable("www.google.co.in", 80, 10000);
                        Boolean isHostReachable = NetworkUtil.isReachable(mContext);
                        if (isHostReachable) {
                            // host reachable
                            Toast.makeText(mContext, "host reachable", Toast.LENGTH_SHORT).show();
                            if (NetworkUtil.getConnectivityStatusString(mContext).equalsIgnoreCase("2g")) {
                                Toast.makeText(mContext, "2g", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "high speed", Toast.LENGTH_SHORT).show();
                            }
                            Intent intentCircularPic = new Intent(mContext, CircularProfilePicture.class);
                            mContext.startActivity(intentCircularPic);
                        } else {
                            // host not reachable
                            Toast.makeText(mContext, "host not reachable.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, NetworkUtil.getConnectivityStatusString(mContext), Toast.LENGTH_LONG).show();
                    }
                }
                if (title.equalsIgnoreCase("Get CountryActivity List")) {
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<List<Country>> call = apiService.getCountries();
                    call.enqueue(new Callback<List<Country>>() {
                        @Override
                        public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                            List<Country> countries = response.body();
                            Log.d("Collection Adapter", "Number of countries received: " + countries.size());
                            Gson gson = new Gson();
                            String jsonCountry = gson.toJson(countries);
                            Intent intentCountry = new Intent(mContext, CountryActivity.class);
                            intentCountry.putExtra("jsonCountry", jsonCountry);
                            mContext.startActivity(intentCountry);
                        }

                        @Override
                        public void onFailure(Call<List<Country>> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("Collection Adapter", t.toString());
                        }
                    });
                }
                if (title.equalsIgnoreCase("Login")) {

                }
                if (title.equalsIgnoreCase("Firebase  Oauth")) {
                    Intent intentChooser = new Intent(mContext, ChooserActivity.class);
                    mContext.startActivity(intentChooser);
                }
                if (title.equalsIgnoreCase("Tab Bar Fragment")) {
                    if (onClickListener != null) {
                        onClickListener.onClick();
                    }
                }
                if (title.equalsIgnoreCase("Android Files Management in Disk")) {
                    Intent intentChooser = new Intent(mContext, FileManagementInAndroid.class);
                    mContext.startActivity(intentChooser);
                }
                if (title.equalsIgnoreCase("Exo Player")) {
                    Intent intentPlayer = new Intent(mContext, SampleChooserActivity.class);
                    mContext.startActivity(intentPlayer);
                }
                if (title.equalsIgnoreCase("MDTP")) {
                    Intent intentPlayer = new Intent(mContext, MaterialDesignDateTime.class);
                    mContext.startActivity(intentPlayer);
                }
                if(title.equalsIgnoreCase("Hash tag")){
                    Intent intentPlayer = new Intent(mContext, HashTagActivity.class);
                    mContext.startActivity(intentPlayer);
                }
                if(title.equalsIgnoreCase("Permission Checker")){
                    Intent intentPlayer = new Intent(mContext, PermissionCheckerActivity.class);
                    mContext.startActivity(intentPlayer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public static interface OnClickListener {
        public void onClick();
    }

}