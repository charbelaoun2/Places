package com.example.places

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Manager.init(this)
    }
}