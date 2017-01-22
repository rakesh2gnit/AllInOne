package com.mycollection.rakesh.mycollection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mycollection.rakesh.mycollection.exoplayer.SampleChooserActivity;
import com.mycollection.rakesh.mycollection.helper.Constant;
import com.mycollection.rakesh.mycollection.networkconnection.NetworkCheckerActivity;

/**
 * Created by rakesh on 10/27/16.
 */

public class SplashActivity extends BaseActivity {

    private TextView textViewPasswordStrengthIndiactor;
    private Button button1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textViewPasswordStrengthIndiactor = (TextView) findViewById(R.id.textViewPasswordStrength);
        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPlayer = new Intent(SplashActivity.this, NetworkCheckerActivity.class);
                startActivity(intentPlayer);
            }
        });

        // Attach TextWatcher to EditText
        ((EditText) findViewById(R.id.et_pwd)).addTextChangedListener(mTextEditorWatcher);
    }

    // EditTextWacther  Implementation
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {            // When No Password Entered
            textViewPasswordStrengthIndiactor.setText("Not Entered");
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 0)
                textViewPasswordStrengthIndiactor.setText("Not Entered");
            else if (s.length() < 6)
                textViewPasswordStrengthIndiactor.setText("EASY");
            else if (s.length() < 10)
                textViewPasswordStrengthIndiactor.setText("MEDIUM");
            else if (s.length() < 15)
                textViewPasswordStrengthIndiactor.setText("STRONG");
            else
                textViewPasswordStrengthIndiactor.setText("STRONGEST");

            if (s.length() == 20)
                textViewPasswordStrengthIndiactor.setText("Password Max Length Reached");

            if (s.length() == 5 && s.toString().equalsIgnoreCase("54321")) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivityForResult(intent, Constant.MainActivity);
            } else if (s.length() == 5 && !s.toString().equalsIgnoreCase("54321")) {
                Toast.makeText(SplashActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.MainActivity) {
            if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(SplashActivity.this,"RESULT_CANCELED",Toast.LENGTH_SHORT).show();
                finish();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(SplashActivity.this, "RESULT_OK", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
