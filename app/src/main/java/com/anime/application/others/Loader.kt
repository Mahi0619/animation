package com.anime.application.others;
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.anime.application.R
import com.anime.application.base.BaseDialog


class Loader(context: Context) : BaseDialog(context,null) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loader_dialog)
        setDimBlur(window)
    }
}