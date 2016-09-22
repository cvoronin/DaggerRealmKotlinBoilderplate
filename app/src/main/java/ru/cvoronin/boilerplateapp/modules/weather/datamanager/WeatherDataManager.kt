package ru.cvoronin.boilerplateapp.modules.weather.datamanager

import ru.cvoronin.boilerplateapp.modules.weather.api.WeatherApi
import ru.cvoronin.boilerplateapp.modules.weather.dao.WeatherDao
import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherScope
import rx.Single
import javax.inject.Inject

@WeatherScope
class WeatherDataManager @Inject constructor(
        private val weatherApi: WeatherApi,
        private val weatherDao: WeatherDao) {

    fun getWeather() = when (weatherDao.isStoredDataExpired()) {
        true -> getFromServerAndSave()
        else -> getStored()
    }

    private fun getFromServerAndSave(): Single<WeatherResponse> =
            weatherApi
                    .loadWeather("Yekaterinburg")
                    .map { response -> WeatherResponse.checkForError(response) }
                    .flatMap { weather -> weatherDao.save(weather) }


    private fun getStored(): Single<WeatherResponse> = weatherDao.load()

    fun clear() = weatherDao.clear()

    fun isStoredDataExpired() = weatherDao.isStoredDataExpired()
    fun isCachedDataExpired(loadedAt: Long) = weatherDao.isCashedDataExpired(loadedAt)
}