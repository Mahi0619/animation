package com.anime.application.base

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

import java.io.File

@HiltAndroidApp
class BaseApp : MultiDexApplication(){
    companion object {
        lateinit var instance: BaseApp
        @JvmStatic
        fun get(): BaseApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this);
        instance = this
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

    }

}
