package com.example.submitmanagement

import android.annotation.SuppressLint
import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.File

class RealmInitClass : Application(){
    @SuppressLint("SdCardPath")
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().allowWritesOnUiThread(true)
            //.deleteRealmIfMigrationNeeded()
            .directory(File("/data/data/com.example.submitmanagement/files/"))
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
