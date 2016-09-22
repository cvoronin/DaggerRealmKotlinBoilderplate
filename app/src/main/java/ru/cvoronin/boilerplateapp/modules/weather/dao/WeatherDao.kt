package ru.cvoronin.boilerplateapp.modules.weather.dao

import io.realm.Realm
import ru.cvoronin.boilerplateapp.modules.weather.domain.*
import ru.cvoronin.boilerplateapp.modules.weather.injection.WeatherScope
import ru.simpls.brs2.commons.functions.now
import ru.simpls.brs2.commons.modules.core.preferenses.DaoPreferencesHelper
import rx.Single
import javax.inject.Inject
import javax.inject.Provider

@WeatherScope
class WeatherDao @Inject constructor(
        private val daoPreferencesHelper: DaoPreferencesHelper,
        private val realmProvider: Provider<Realm>) {

    companion object {
        val WEATHER_EXPIRATION_PERIOD_MIN = 10
    }

    fun isStoredDataExpired() = daoPreferencesHelper.isStoredDataExpired(WeatherResponse::class.java.simpleName, WEATHER_EXPIRATION_PERIOD_MIN)
    fun isCashedDataExpired(loadedAt: Long) = daoPreferencesHelper.isDataExpired(WeatherResponse::class.java.simpleName, loadedAt, WEATHER_EXPIRATION_PERIOD_MIN)

    fun save(weather: WeatherResponse): Single<WeatherResponse> =
            Single.fromCallable {
                with(realmProvider.get()) {
                    use { realm ->
                        executeTransaction {
                            deleteWeather(realm)
                            realm.insert(weather)
                            daoPreferencesHelper.saveLoadMoment(WeatherResponse::class.java.simpleName, now())
                        }
                    }
                }
                
                weather
            }

    private fun deleteWeather(realm : Realm) {
        realm.delete(WeatherResponse::class.java)
        realm.delete(Coord::class.java)
        realm.delete(Weather::class.java)
        realm.delete(MainInfo::class.java)
        realm.delete(Wind::class.java)
    }

    fun load(): Single<WeatherResponse> =
            Single.fromCallable {
                with(realmProvider.get()) {
                    use { realm ->
                        realm.copyFromRealm(
                                realm.where(WeatherResponse::class.java).findFirst())
                    }
                }
            }

    fun clear() {
        with(realmProvider.get()) {
            use { realm ->
                executeTransaction {
                    realm.delete(WeatherResponse::class.java)
                    daoPreferencesHelper.clearLoadMoment(WeatherResponse::class.java.simpleName)
                }
            }
        }
    }
}