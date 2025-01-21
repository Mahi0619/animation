package com.anime.application.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anime.application.R;


public class ErrorDialogHelper {

    public void ErrorMessage(Activity activity, String message, String ttl) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout rlTitle = dialog.findViewById(R.id.rlTitle);
        TextView title = dialog.findViewById(R.id.tvTitle);
        TextView btnOk = dialog.findViewById(R.id.btnOk);
          TextView msg = dialog.findViewById(R.id.tvMsg);

        if (ttl!=null && !ttl.isEmpty()) {
            title.setText(ttl);
        } else {
            title.setText("Error");
        }
        if (!message.isEmpty()) {
            msg.setText(message);
        } else {
            msg.setText("Somethgin went wrong!");
        }
        btnOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}