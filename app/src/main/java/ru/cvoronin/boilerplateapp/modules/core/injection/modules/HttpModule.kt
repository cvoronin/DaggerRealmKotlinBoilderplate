package ru.cvoronin.boilerplateapp.modules.core.injection.modules

import android.content.Context
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope
import timber.log.Timber
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@ApplicationScope
class HttpModule {

    val CACHE_SIZE = 100L * 1024 * 1024

    //.............................................................................................

    @Provides
    @ApplicationScope
    fun providePicasso(@ApplicationContext context: Context,
                       okHttpClient: OkHttpClient): Picasso =
            Picasso
                    .Builder(context)
                    .downloader(OkHttp3Downloader(okHttpClient))
                    .build()

    //.............................................................................................

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            appProperties: AppProperties): OkHttpClient {
        return with(OkHttpClient.Builder()) {
            cache(Cache(context.cacheDir, CACHE_SIZE))

            val httpProxy = createHttpProxy(appProperties)
            if (httpProxy != null) proxy(httpProxy)

            val loggerInterceptor = createHttpLoggingInterceptor(appProperties)
            if (loggerInterceptor != null) networkInterceptors().add(loggerInterceptor)

            if (!appProperties.HTTP_SSL_VALIDATE) disableSslCheck(this)

            connectTimeout(appProperties.HTTP_TIMEOUT_CONNECT_SEC ?: 15, TimeUnit.SECONDS)
            readTimeout(appProperties.HTTP_TIMEOUT_READ_SEC ?: 60, TimeUnit.SECONDS)

            build()
        }
    }

    private fun createHttpProxy(appProperties: AppProperties): Proxy? {
        val useProxy = appProperties.HTTP_PROXY
                && appProperties.HTTP_PROXY_HOST != null
                && appProperties.HTTP_PROXY_PORT != null

        return when (useProxy) {
            true -> Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(
                    appProperties.HTTP_PROXY_HOST,
                    appProperties.HTTP_PROXY_PORT!!))
            else -> null
        }
    }

    private fun disableSslCheck(builder: OkHttpClient.Builder) {
        // http://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllManager, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory)

        builder.hostnameVerifier { hostname, session -> true }
    }

    private fun createHttpLoggingInterceptor(appProperties: AppProperties): HttpLoggingInterceptor? =
            when {
                appProperties.HTTP_LOG_LEVEL != HttpLoggingInterceptor.Level.NONE -> {
                    val httpLoggingInterceptor = HttpLoggingInterceptor()
                    httpLoggingInterceptor.level = appProperties.HTTP_LOG_LEVEL
                    httpLoggingInterceptor
                }
                else -> null
            }
}

private val trustAllManager = arrayOf<TrustManager>(object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return arrayOf()
    }
})