package edu.ucb.cashin

import android.app.Application
import edu.ucb.cashin.di.initKoinAndroid

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
}
