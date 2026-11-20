package edu.ucb.cashin

import android.app.Application
import edu.ucb.cashin.di.initKoinAndroid
import edu.ucb.cashin.scheduler.LogScheduler

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
        LogScheduler(this).schedulePeriodicalUpload()
    }
}
