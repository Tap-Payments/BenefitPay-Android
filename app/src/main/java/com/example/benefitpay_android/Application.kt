package com.example.benefitpay_android

import android.app.Application
import company.tap.tapbenefitpay.open.AppLifecycleObserver


class MyApp : Application() {
        override fun onCreate() {
            super.onCreate()
        }

        companion object {
            private val TAG = MyApp::class.java.name
        }
    }
