package ru.cvoronin.boilerplateapp.modules.core

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.cvoronin.boilerplateapp.App
import ru.cvoronin.boilerplateapp.modules.weather.api.WeatherResponseErrorException
import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(RobolectricTestRunner::class)
class WeatherComponentTest {

    var app: App by Delegates.notNull()

    @Before
    fun setUp() {
        app = RuntimeEnvironment.application as App
    }

    @After
    fun tearDown() {
        app.clearWeatherComponent()
    }

    @Test
    fun createAndFree() {
        app.createWeatherComponent()
        assertNotNull(app.weatherComponent)

        app.clearWeatherComponent()
        assertNull(app.weatherComponent)
    }

    @Test
    fun checkCreation() {
        app.createWeatherComponent()
        assertNotNull(app.weatherComponent!!.weatherApi())
    }

    @Test
    fun getCorrectWeather() {
        println("*** getCorrectWeather")

        app.createWeatherComponent()

        val latch = CountDownLatch(1)

        val api = app.weatherComponent!!.weatherApi()
        api.loadWeather("Yekaterinburg")
                .subscribeOn(Schedulers.io())
                .map { WeatherResponse.checkForError(it) }
                .subscribe(
                        { response -> latch.countDown() },
                        { error -> fail("Error: ${error.message}") })

        assertTrue { latch.await(5, TimeUnit.SECONDS) }
    }

    @Test
    fun getWrongCityWeather() {
        println("*** getWrongCityWeather")

        app.createWeatherComponent()

        val latch = CountDownLatch(1)

        val api = app.weatherComponent!!.weatherApi()
        api.loadWeather("")
                .subscribeOn(Schedulers.io())
                .map { WeatherResponse.checkForError(it) }
                .subscribe(
                        { response -> fail("Error should be generated") },
                        { error ->
                            when (error) {
                                is WeatherResponseErrorException -> latch.countDown()
                                else -> fail("WeatherResponseErrorException is expected")
                            }
                        })

        assertTrue { latch.await(5, TimeUnit.SECONDS) }
    }
}