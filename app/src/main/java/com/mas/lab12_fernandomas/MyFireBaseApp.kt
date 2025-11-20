package com.mas.lab12_fernandomas

import android.app.Application
import com.google.firebase.FirebaseApp

class MyFireBaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this);
    }
}