package com.mycollection.rakesh.mycollection.permissionchecker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycollection.rakesh.mycollection.R;


public class AlertManager {

    public Dialog successdialog;

    public void showAlertDialog(Context context, String title, String message, boolean isCancelable, int drawable,
                                       boolean isOkVisible, String okButtonName, OnClickListener okclicklistener,
                                       String msg1ButtonName, OnClickListener msg1clicklistener, String msg2ButtonName,
                                       OnClickListener msg2clicklistener) {
        if (context != null) {
            successdialog = new Dialog(context);

            successdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            successdialog.getWindow().setGravity(Gravity.CENTER);
            successdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.success_alert_dialog, null);

            ImageView iv_dialog_icon = (ImageView) view.findViewById(R.id.iv_dialog_icon);
            TextView alerttitle = (TextView) view.findViewById(R.id.tv_alert_title);
            TextView alertlongtitle = (TextView) view.findViewById(R.id.tv_alert_longtitle);
            TextView alertmessage = (TextView) view.findViewById(R.id.tv_alert_mesage);
            Button btnok = (Button) view.findViewById(R.id.btn_ok);
            Button btnmsg1 = (Button) view.findViewById(R.id.btn_msg1);
            Button btnmsg2 = (Button) view.findViewById(R.id.btn_msg2);
            Button btnlongmsg1 = (Button) view.findViewById(R.id.btn_longmsg1);
            Button btnlongmsg2 = (Button) view.findViewById(R.id.btn_longmsg2);

            iv_dialog_icon.setImageDrawable(context.getResources().getDrawable(drawable));
    /*        alerttitle.setTypeface(FontManager.getOpenSansBoldFont(context));
            alertlongtitle.setTypeface(FontManager.getOpenSansBoldFont(context));
            alertmessage.setTypeface(FontManager.getOpenSansRegularFont(context));
            btnok.setTypeface(FontManager.getOpenSansBoldFont(context));
            btnmsg1.setTypeface(FontManager.getOpenSansBoldFont(context));
            btnmsg2.setTypeface(FontManager.getOpenSansBoldFont(context));
            btnlongmsg1.setTypeface(FontManager.getOpenSansBoldFont(context));
            btnlongmsg2.setTypeface(FontManager.getOpenSansBoldFont(context));*/

            alertmessage.setText(message);

            if (isOkVisible) {
                btnok.setVisibility(View.VISIBLE);
                alerttitle.setVisibility(View.VISIBLE);
                view.findViewById(R.id.ll_button).setVisibility(View.GONE);
                view.findViewById(R.id.ll_longbutton).setVisibility(View.GONE);
                alertlongtitle.setVisibility(View.GONE);
                alerttitle.setText(title);
                btnok.setText(okButtonName);
            } else {
                btnok.setVisibility(View.GONE);
                alerttitle.setVisibility(View.GONE);
                alertlongtitle.setVisibility(View.VISIBLE);
                alertlongtitle.setText(title);

                if (msg2ButtonName.equalsIgnoreCase("OK")) {
                    view.findViewById(R.id.ll_button).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ll_longbutton).setVisibility(View.GONE);
                    btnmsg1.setText(msg1ButtonName);
                    btnmsg2.setText(msg2ButtonName);
                } else {
                    view.findViewById(R.id.ll_longbutton).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.ll_button).setVisibility(View.GONE);
                    btnlongmsg1.setText(msg1ButtonName);
                    btnlongmsg2.setText(msg2ButtonName);
                }
            }

            successdialog.setContentView(view);
            successdialog.setCanceledOnTouchOutside(isCancelable);
            successdialog.setCancelable(isCancelable);

            successdialog.show();
            btnok.setOnClickListener(okclicklistener);
            btnmsg1.setOnClickListener(msg1clicklistener);
            btnmsg2.setOnClickListener(msg2clicklistener);
            btnlongmsg1.setOnClickListener(msg1clicklistener);
            btnlongmsg2.setOnClickListener(msg2clicklistener);
        }
    }
}
