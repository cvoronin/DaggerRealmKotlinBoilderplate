package ru.cvoronin.boilerplateapp.modules.weather.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import rx.Single

interface WeatherApi {

    // http://api.openweathermap.org/data/2.5/weather?q=Yekaterinburg&units=metric&APPID=7926b7b23fe2761fcf3cd172bfc6bfc0

    @GET("weather")
    fun loadWeather(
            @Query("q") city: String,
            @Query("units") units: String = "metric",
            @Query("APPID") appId: String = "7926b7b23fe2761fcf3cd172bfc6bfc0"): Single<WeatherResponse>

}