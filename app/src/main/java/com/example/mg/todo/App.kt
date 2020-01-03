package com.example.mg.todo

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher


class App : Application() {
    private lateinit var mRefWatcher: RefWatcher

    companion object {

        fun getRefWatcher(context: Context): RefWatcher {
            val application = context.applicationContext as App
            return application.mRefWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        initLeakCanary()

    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        mRefWatcher = LeakCanary.install(this)
    }
}