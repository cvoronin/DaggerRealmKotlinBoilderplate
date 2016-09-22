package ru.cvoronin.boilerplateapp.modules.weather.injection

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.weather.api.WeatherApi
import rx.schedulers.Schedulers
import javax.inject.Named

@Module
class WeatherModule {

    @Provides
    @WeatherScope
    fun provideWeatherApi(@Named("weatherApi") retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @WeatherScope
    @Named("weatherApi")
    fun provideWeatherApiRetrofit(appProperties: AppProperties,
                                  gson: Gson,
                                  okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(appProperties.WEATHER_SERVER_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .callFactory(okHttpClient)
                    .build()
}