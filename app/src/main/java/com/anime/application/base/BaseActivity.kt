package com.anime.application.base
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anime.application.others.Loader
import com.anime.application.others.MyUtils


@SuppressLint("Registered")
abstract class BaseActivity() : AppCompatActivity(){
    lateinit var dialogLoader: Dialog
    lateinit var progressView: View
    lateinit var main: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* RetrofitProvider.setOnEventListener {
            if(it){
                LogoutFromApp.logout(this)
            }
        }*/

    }

    protected var tvTitleView: TextView? = null

    var loader: Loader? = null
    protected fun showLoader() {
        loader = null
        loader = this?.let { Loader(it) }
        if (loader != null && !loader?.isShowing!!) {
            loader?.show()
        }

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun setStatusBarColor(id: Int, context: Activity) {
        context.window?.statusBarColor = ContextCompat.getColor(context, id)

    }
    protected fun hideLoader() {
        if (loader != null && loader?.isShowing!!) {
            loader?.dismiss()
        }
        loader = null

    }

    protected fun initProgressView(view: View) {
        progressView = view
    }

    protected fun initMainView(view: View) {
        main = view
    }

    protected fun showProgressView() {
        MyUtils.viewVisible(progressView)
        MyUtils.viewGone(main)
    }

    protected fun hideProgressView() {
        MyUtils.viewVisible(main)
        MyUtils.viewGone(progressView)
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

    protected fun onBackButton(ivBack: ImageView?){
        ivBack?.setOnClickListener { onBackPressed() }
    }

}