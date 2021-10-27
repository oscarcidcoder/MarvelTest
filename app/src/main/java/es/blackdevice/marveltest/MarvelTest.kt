package es.blackdevice.marveltest

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import es.blackdevice.marveltest.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Entry point application
 * Created by ocid on 10/26/21.
 */
class MarvelTest : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        startKoin {
            androidContext(this@MarvelTest)
            modules(networkModule)
        }
    }

}