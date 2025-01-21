package com.anime.application.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

public abstract class BaseDialog extends Dialog {

    View mAnchorView;

    public BaseDialog(@NonNull Context context, View anchorView) {
        super(context);
        mAnchorView = anchorView;
    }
  public BaseDialog(@NonNull Context context) {
        super(context);

    }

    protected void setDimBlur(Window window) {
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();

            if (mAnchorView != null) {
                lp.copyFrom(this.getWindow().getAttributes());
                lp.gravity = Gravity.BOTTOM;
                lp.y = mAnchorView.getHeight();
            }else{
                lp.dimAmount = 0.5f;
            }
            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}