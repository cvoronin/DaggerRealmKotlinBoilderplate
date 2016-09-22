package ru.cvoronin.boilerplateapp

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Kit
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.core.injection.AppComponent
import ru.cvoronin.boilerplateapp.modules.core.injection.DaggerAppComponent
import ru.cvoronin.boilerplateapp.modules.core.injection.modules.AppModule
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherComponent
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherModule
import timber.log.Timber
import kotlin.properties.Delegates

//................................................................................................

fun Activity.app() = this.application as App
fun Fragment.app() = this.activity.app()

//................................................................................................

class App : Application() {

    private val CRASHLYTICS_MIN_LOG_LEVEL = Log.INFO

    var appComponent : AppComponent by Delegates.notNull()
    var weatherComponent : WeatherComponent? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        val appProperties = appComponent.appProperties()

        initStrictMode(appProperties)
        initLogs(appProperties)
        initFabric(appProperties)
    }

    //.............................................................................................

    fun createWeatherComponent() : WeatherComponent {
        if (weatherComponent == null) {
            weatherComponent = appComponent.plusWeatherComponent(WeatherModule())
        }

        return weatherComponent!!
    }

    fun clearWeatherComponent() {
        weatherComponent = null
    }

    //.............................................................................................

    private fun initStrictMode(appProperties : AppProperties) {
        if (appProperties.IS_DEBUG) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            )

            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build()
            )
        }
    }

    private fun initLogs(appProperties : AppProperties) {
        if (appProperties.IS_DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        if (appProperties.USE_CRASHLYTICS) {
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {

                    if (priority >= CRASHLYTICS_MIN_LOG_LEVEL) {
                        Crashlytics.log(priority, tag, message)
                    }

                    if (t != null) {
                        Crashlytics.logException(t)
                    }
                }
            })
        }
    }

    private fun initFabric(appProperties : AppProperties) {
        with(appProperties) {
            val kits = java.util.ArrayList<Kit<*>>()

            if (USE_CRASHLYTICS) kits.add(com.crashlytics.android.Crashlytics())
            if (USE_ANSWERS) kits.add(com.crashlytics.android.answers.Answers())

            // * is for varArgs
            val kitsArray = kotlin.Array(kits.size) { kits[it] }
            if (kits.size > 0) {
                io.fabric.sdk.android.Fabric.with(context, *kitsArray)
            }
        }
    }
}