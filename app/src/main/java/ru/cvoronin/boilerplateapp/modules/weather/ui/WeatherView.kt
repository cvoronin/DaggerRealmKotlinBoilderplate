package ru.cvoronin.boilerplateapp.modules.weather.ui

import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import ru.simpls.brs2.commons.modules.core.Action

interface WeatherView {

    fun showProgress()
    fun showError(message: String, defaultAction: Action, secondaryAction: Action? = null)
    fun showWeather(weather : WeatherResponse)

    fun exit()

}