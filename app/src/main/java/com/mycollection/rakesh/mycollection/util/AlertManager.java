package com.mycollection.rakesh.mycollection.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mycollection.rakesh.mycollection.R;

/**
 * Created by gleecus on 11/16/16.
 */

public class AlertManager {

    public Dialog confirmationdialog;

    public void showConfirmationAlert(Context context, String title,
                                      String message, String okButtonName,
                                      View.OnClickListener yesClickListener,
                                      String exitButtonName,
                                      View.OnClickListener noClickListener, boolean isBgChanged) {
        if (context != null) {
            confirmationdialog = new Dialog(context);

            confirmationdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            confirmationdialog.getWindow().setGravity(Gravity.CENTER);
            confirmationdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            confirmationdialog.setCanceledOnTouchOutside(false);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_custom_alert, null);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_confirmation_alert);
            if (isBgChanged) {
                rl.setBackground(context.getResources().getDrawable(R.drawable.layout_bg_alert_green));
            }
            TextView alerttitle = (TextView) view.findViewById(R.id.tv_alert_title);
            TextView alertmessage = (TextView) view.findViewById(R.id.tv_alert_mesage);
            Button btnok = (Button) view.findViewById(R.id.btn_ok);
            Button btnno = (Button) view.findViewById(R.id.btn_no);
            alerttitle.setTypeface(FontManager.getOpenSansLightFont(context));
            alertmessage.setTypeface(FontManager.getOpenSansRegularFont(context));
            btnok.setTypeface(FontManager.getOpenSansSemiBoldFont(context));
            btnno.setTypeface(FontManager.getOpenSansSemiBoldFont(context));
            alerttitle.setText(title);
            alertmessage.setText(message);
            btnok.setText(okButtonName);
            btnno.setText(exitButtonName);
            confirmationdialog.setContentView(view);
            confirmationdialog.setCanceledOnTouchOutside(false);
            confirmationdialog.setCancelable(false);
            confirmationdialog.show();
            btnok.setOnClickListener(yesClickListener);
            btnno.setOnClickListener(noClickListener);
        }
    }
}
