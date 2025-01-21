package com.anime.application.base

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.anime.application.base.BaseApp.Companion.get


object Toaster {

    fun shortToast(
        view: View,
        window: Window, @StringRes text: Int
    ) {
        val toast = makeToast(
            get().getString(text),
            Toast.LENGTH_SHORT
        )
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val viewLocation = IntArray(2)
        view.getLocationInWindow(viewLocation)
        val viewLeft = viewLocation[0] - rect.left
        val viewTop = viewLocation[1] - rect.top
        val metrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            metrics.widthPixels,
            View.MeasureSpec.UNSPECIFIED
        )
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            metrics.heightPixels,
            View.MeasureSpec.UNSPECIFIED
        )
        toast.view?.measure(widthMeasureSpec, heightMeasureSpec)
        val toastY = viewTop + view.height
        toast.setGravity(
            Gravity.TOP or Gravity.CENTER_VERTICAL,
            0,
            toastY
        )
        toast.show()
    }

    fun shortToast(@StringRes text: Int) {
        shortToast(get().getString(text))
    }

    fun shortToast(text: String) {
        show(text, Toast.LENGTH_SHORT)
    }

    fun longToast(@StringRes text: Int) {
        longToast(get().getString(text))
    }

    fun longToast(text: String) {
        show(text, Toast.LENGTH_LONG)
    }


    private fun makeToast(text: String, length: Int): Toast {
        return Toast.makeText(get(), text, length)
    }

    var toast: Toast? = null
    private fun show(text: String, length: Int) {
        toast?.cancel()
        toast = makeToast(text, length)
        toast?.show()
    }

    fun somethingWentWrong() {
        shortToast("Something went wrong.")
    }

    fun internalServerError() {
        shortToast("Internal Server Error")
    }

    @IntDef(Toast.LENGTH_LONG, Toast.LENGTH_SHORT)
    private annotation class ToastLength
}