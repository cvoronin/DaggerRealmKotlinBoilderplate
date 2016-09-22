package ru.cvoronin.boilerplateapp.modules.weather.domain

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import ru.cvoronin.boilerplateapp.modules.weather.api.WeatherResponseErrorException

open class WeatherResponse : RealmObject() {

    companion object {
        fun checkForError(response: WeatherResponse): WeatherResponse {
            if (response.cod != 200) {
                throw WeatherResponseErrorException("${response.cod} ${response.message}", response)
            }

            return response
        }
    }

    @Ignore
    var cod: Int? = null

    @Ignore
    var message: String? = null

    var id: Int? = null

    var name: String? = null

    var country: String? = null

    var coord: Coord? = null

    @SerializedName("weather")
    var weatherList: RealmList<Weather>? = null

    @SerializedName("main")
    var mainInfo: MainInfo? = null

    var wind: Wind? = null
}

open class Coord(var lon: Double? = null, var lat: Double? = null) : RealmObject()

open class Weather(var id: Long? = null, var main: String? = null, var description: String? = null, var icon: String? = null) : RealmObject()

open class MainInfo(
        var temp: Double? = null, var pressure: Double? = null, var humidity: Double? = null,
        @SerializedName("temp_min") var tempMin: Double? = null,
        @SerializedName("temp_max") var tempMax: Double? = null,
        @SerializedName("sea_level") var seaLevel: Double? = null,
        @SerializedName("grnd_level") var groundLevel: Double? = null) : RealmObject()

open class Wind(var speed: Double? = null, var deg: Double? = null) : RealmObject()