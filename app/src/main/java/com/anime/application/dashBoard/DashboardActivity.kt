package com.anime.application.dashBoard


import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anime.application.R
import com.anime.application.dashBoard.fragment.AnimeFragment
import com.anime.application.others.NeTWorkChange
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val neTWorkChange = NeTWorkChange()
    private val fragmentStack = Stack<Fragment>()

    companion object {
        private const val BACK_PRESS_INTERVAL = 2000 // 2 seconds
        private var lastBackPressTime: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.activity_dashboard)
        addToBackStackAndOpenFragment(AnimeFragment())
        setupOnBackPressedDispatcher()

    }

    /** TO register the network..... */
    override fun onStart() {
        registerReceiver(neTWorkChange, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        super.onStart()
    }

    /** TO un-Register the network calll */
    override fun onStop() {
        unregisterReceiver(neTWorkChange)
        super.onStop()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            handleBackPress()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private var backPressTime: Long = 0
    private var toast: Toast? = null
    fun handleBackPress() {
        Log.e("Stack_Size", "TotalFragments: ${fragmentStack.size}")
        if (fragmentStack.size > 1) {
            fragmentStack.pop()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentStack.peek())
                .commit()
        } else {

            if (System.currentTimeMillis() - backPressTime <= 300) {
                toast?.cancel()
                finish()
                finishAffinity()
            } else {
                toast?.cancel()
                toast = Toast.makeText(this, "Press twice to exit", Toast.LENGTH_SHORT)
                toast?.show()
                backPressTime = System.currentTimeMillis()
            }

        }
    }

    private fun setupOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    fun addToBackStackAndOpenFragment(fragment: Fragment) {
        fragmentStack.push(fragment)
        openFragment(fragment)
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    private fun isBackPressedTwice(): Boolean {
        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime - lastBackPressTime
        lastBackPressTime = currentTime
        return diffTime <= BACK_PRESS_INTERVAL
    }

}
/*
Kaise kaiso ko kamraz krti hain, Kaise kaiso ko kamraz krti hain
apno ko naraz krti hai
Tere baad hmne jana ye, ghadi tik tik ki awaz krti hai*/


