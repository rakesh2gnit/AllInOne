package com.mycollection.rakesh.mycollection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gleecus on 1/9/17.
 */

public class MaterialDesignDateTime extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView txt_date;
    private String strDate;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mdtp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txt_date = (TextView) findViewById(R.id.txt_date);

        ((Button) findViewById(R.id.btn_pickdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setMinDate(Calendar.getInstance());
        now.add(Calendar.DAY_OF_MONTH, 30);
        dpd.setMaxDate(now);
        dpd.setOkText("SET");
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        openTime();
        //System.out.println("after date:=" + year + "," + monthOfYear + "," + dayOfMonth);//2017,0,9
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MONTH, monthOfYear);
        String strMonth = new SimpleDateFormat("LLL", Locale.getDefault()).format(now.getTime());
        strDate = dayOfMonth + " " + strMonth;
        //txt_date.setText(dayOfMonth + " " + strMonth);
    }

    private void openTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY) + 1,
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setMinTime(now.get(Calendar.HOUR_OF_DAY) + 1, now.get(Calendar.MINUTE), 5);
        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
        tpd.setOkText("SET");
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        System.out.println("after time:=" + hourOfDay + "," + minute + "," + second);//16,5,0
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        txt_date.setText(strDate + " " + hourString + ":" + minuteString);
    }
}
