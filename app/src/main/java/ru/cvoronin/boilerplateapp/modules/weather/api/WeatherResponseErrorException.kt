package ru.cvoronin.boilerplateapp.modules.weather.api

import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse

class WeatherResponseErrorException(message : String, var response : WeatherResponse) : RuntimeException(message)