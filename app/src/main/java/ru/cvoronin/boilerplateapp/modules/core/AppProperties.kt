package ru.cvoronin.boilerplateapp.modules.core

import android.content.Context
import android.graphics.Bitmap
import okhttp3.logging.HttpLoggingInterceptor
import ru.cvoronin.boilerplateapp.R
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

fun Map<String, String>.getBool(key: String, default: Boolean = false): Boolean {
    return this[key]?.toBoolean() ?: default
}

fun Map<String, String>.getLong(key: String, default: Long? = null): Long? {
    return this[key]?.toLong() ?: default
}

fun Map<String, String>.getInt(key: String, default: Int? = null): Int? {
    return this[key]?.toInt() ?: default
}

@ApplicationScope
class AppProperties @Inject constructor(@ApplicationContext var context: Context) {

    companion object {
        // Bitmap.Config.ARGB_8888 - для полного цвета
        val IMAGE_FORMAT = Bitmap.Config.RGB_565
    }

    //.............................................................................................

    var IS_DEBUG = OFF
    var USE_CRASHLYTICS = OFF
    var USE_ANSWERS = OFF

    var HTTP_LOG_LEVEL: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.NONE
    var HTTP_TIMEOUT_READ_SEC: Long? = null
    var HTTP_TIMEOUT_CONNECT_SEC: Long? = null
    var HTTP_PROXY = OFF
    var HTTP_PROXY_HOST: String? = null
    var HTTP_PROXY_PORT: Int? = null
    var HTTP_SSL_VALIDATE: Boolean = true

    var REALM_USE_MIGRATION: Boolean = true

    var WEATHER_SERVER_API_URL: String = ""
    var WEATHER_SERVER_IMAGE_URL: String = ""

    var REALM_DB_NAME : String? = null

    var TEST_VALUE_STR : String? = null
    var TEST_VALUE_INT : Int? = null
    var TEST_VALUE_BOOL : Boolean? = null

    init {
        loadProperties()
    }

    private fun loadFromRawFile(context: Context, fileId: Int): Map<String, String> {
        return context.resources.openRawResource(fileId).use {
            val properties = Properties()
            properties.load(it)
            properties as Map<String, String>
        }
    }

    private fun loadProperties() {
        with(loadFromRawFile(context, R.raw.test)) {
            TEST_VALUE_STR = get("testvalue.string")
            TEST_VALUE_INT = getInt("testvalue.int")
            TEST_VALUE_BOOL = getBool("testvalue.boolean")
        }

        with(loadFromRawFile(context, R.raw.allconfigs)) {
            HTTP_TIMEOUT_CONNECT_SEC = getLong("http.timeout.connect", 15L)
            HTTP_TIMEOUT_READ_SEC = getLong("http.timeout.read", 60L)
            REALM_DB_NAME = get("realm.name")
        }

        with(loadFromRawFile(context, R.raw.config)) {
            IS_DEBUG = getBool("app.debug")

            // Утилиты и инструменты
            USE_CRASHLYTICS = IS_DEBUG and getBool("app.use_crashlytics")
            USE_ANSWERS = IS_DEBUG and getBool("app.use_answers")

            // Обращение к серверу
            val logLevel = get("http.log.level")
            HTTP_LOG_LEVEL = when (logLevel != null) {
                true -> HttpLoggingInterceptor.Level.valueOf(logLevel!!)
                false -> HttpLoggingInterceptor.Level.NONE
            }

            HTTP_PROXY = IS_DEBUG and getBool("http.proxy")
            HTTP_PROXY_HOST = get("http.proxy.host")
            HTTP_PROXY_PORT = getInt("http.proxy.port", null)
            HTTP_SSL_VALIDATE = getBool("http.ssl.validate", true)

            REALM_USE_MIGRATION = getBool("realm.use_migration", true)

            // Сервер погоды
            WEATHER_SERVER_API_URL = get("weather.server.api_url") ?: ""
            WEATHER_SERVER_IMAGE_URL = get("weather.server.image_url") ?: ""
        }
    }
}