package com.kadence1

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.kadencelibrary.extension.debug.d

class App : Application() {

    override fun onCreate() {
        super.onCreate()
//        ANRWatchDog().start()
//        RxActivityResult.register(this)

        d()



    }
}