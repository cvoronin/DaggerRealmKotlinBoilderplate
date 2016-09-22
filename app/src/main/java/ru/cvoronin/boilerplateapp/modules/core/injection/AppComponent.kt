package ru.cvoronin.boilerplateapp.modules.core.injection

import android.content.Context
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.Component
import io.realm.Realm
import ru.cvoronin.boilerplateapp.App
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.core.injection.modules.AppModule
import ru.cvoronin.boilerplateapp.modules.core.injection.modules.GsonModule
import ru.cvoronin.boilerplateapp.modules.core.injection.modules.HttpModule
import ru.cvoronin.boilerplateapp.modules.core.injection.modules.RealmModule
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherComponent
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherModule

@Component(modules = arrayOf(AppModule::class, GsonModule::class, HttpModule::class, RealmModule::class))
@ApplicationScope
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    fun app(): App
    fun gson(): Gson
    fun appProperties(): AppProperties

    fun picasso(): Picasso
    fun realm(): Realm

    //.............................................................................................

    fun plusWeatherComponent(weatherModule : WeatherModule) : WeatherComponent
}