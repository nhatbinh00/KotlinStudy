package com.example.kotlinknowledge

import android.app.Application
import androidx.room.Room
import com.example.kotlinknowledge.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import androidx.test.core.app.ApplicationProvider.getApplicationContext

@HiltAndroidApp
class MainApplication: Application(){
    companion object{
        private var mSelf: MainApplication? = null
        lateinit var db: AppDatabase
        lateinit var appContext: Context
        fun self(): MainApplication? {
            return mSelf
        }

        fun initializeDb(appContext: Context){
            db = Room.databaseBuilder(
                appContext,
                AppDatabase::class.java, "ktx_database"
            ).build()
        }

        fun initializeContext(context: Context){
            appContext = context
        }
    }

    override fun onCreate() {
        super.onCreate()
        mSelf = this
    }
}