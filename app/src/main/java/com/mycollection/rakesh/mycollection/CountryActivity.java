package com.mycollection.rakesh.mycollection;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mycollection.rakesh.mycollection.adapter.CountryCodeAdapter;
import com.mycollection.rakesh.mycollection.model.Country;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by gleecus on 12/5/16.
 */

public class CountryActivity extends BaseActivity {

    private List<Country> carsList;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_country;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Now in your TargetActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String countryListAsString = getIntent().getStringExtra("jsonCountry");
            Gson gson = new Gson();
            Type type = new TypeToken<List<Country>>() {
            }.getType();
            carsList = gson.fromJson(countryListAsString, type);
            for (Country cars : carsList) {
                Log.i("Country Data", cars.getDialCode() + "-" + cars.getName());
            }
        }
        getIntent().getSerializableExtra("countryList");

        RecyclerView recyclerView_countryDialog = (RecyclerView) findViewById(R.id.recycler_countryDialog);

        final TextView textViewTitle = (TextView) findViewById(R.id.textView_title);
        textViewTitle.setText("Select country");
        final EditText editText_search = (EditText) findViewById(R.id.editText_search);
        //editText_search.setHint(codePicker.getSearchHintText());
        TextView textView_noResult = (TextView) findViewById(R.id.textView_noresult);
        //textView_noResult.setText(codePicker.getNoResultFoundText());
        CountryCodeAdapter cca = new CountryCodeAdapter(this, carsList, editText_search, textView_noResult);
        recyclerView_countryDialog.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_countryDialog.setAdapter(cca);
    }
}
