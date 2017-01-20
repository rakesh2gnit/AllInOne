package com.mycollection.rakesh.mycollection;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gleecus on 1/16/17.
 */

public class HashTagActivity extends BaseActivity {

    TextView txtHash1, txtHash2;
    Button btn1;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_hashtag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtHash1 = (TextView) findViewById(R.id.txt_hash1);
        txtHash2 = (TextView) findViewById(R.id.txt_hash2);
        btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = txtHash1.getText().toString().trim();
                /*String[] arr = s.split(" ");
                for ( String ss : arr) {
                    System.out.println(ss);
                }*/
                String str1 = str.replaceAll("(#[A-Za-z0-9_-]+)",
                        "<font color='#1ABC9C'>" + "$0" + "</font>");
                txtHash2.setText(Html.fromHtml(str1));
                Pattern MY_PATTERN = Pattern.compile("(#[A-Za-z0-9_-]+)");
                Matcher mat = MY_PATTERN.matcher(str);
                List<String> strs = new ArrayList<String>();
                if (mat.find()) {
                    strs.add(mat.group(1));
                }
                Toast.makeText(HashTagActivity.this, "" + strs.size()+","+strs.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        while (mat.find()) {
            //System.out.println(mat.group(1));
            strs.add(mat.group(1));
        }*/
    }
}
