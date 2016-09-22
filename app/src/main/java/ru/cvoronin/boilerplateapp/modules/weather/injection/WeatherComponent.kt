package ru.cvoronin.boilerplateapp.modules.weather.injection

import dagger.Subcomponent
import ru.cvoronin.boilerplateapp.modules.weather.api.WeatherApi
import ru.cvoronin.boilerplateapp.modules.weather.ui.WeatherActivity
import javax.inject.Scope

//.................................................................................................

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class WeatherScope

//.................................................................................................

@Subcomponent(modules = arrayOf(WeatherModule::class))
@WeatherScope
interface WeatherComponent {

    fun weatherApi() : WeatherApi

    fun inject(activity : WeatherActivity)
}