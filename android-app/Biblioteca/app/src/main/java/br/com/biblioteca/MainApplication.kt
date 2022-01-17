package br.com.biblioteca

import android.app.Application
import br.com.biblioteca.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(appModules)
        }
    }
}