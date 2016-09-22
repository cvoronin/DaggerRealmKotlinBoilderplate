package ru.cvoronin.boilerplateapp.modules.weather.ui

import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.weather.datamanager.WeatherDataManager
import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import ru.simpls.brs2.commons.functions.now
import ru.simpls.brs2.commons.modules.core.Action
import ru.simpls.brs2.commons.modules.core.mvp.presenters.BasePresenter
import ru.simpls.brs2.commons.modules.core.stateengine.ErrorState
import ru.simpls.brs2.commons.modules.core.stateengine.ViewState
import ru.simpls.brs2.mobbank.modules.common.utils.ErrorsHelper
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class WeatherPresenter @Inject constructor(
        private val weatherDataManager: WeatherDataManager,
        private val appProperties: AppProperties) : BasePresenter<WeatherView>() {

    private var weather: WeatherResponse? = null
    private var loadedAt : Long = 0L

    override fun exitView() {
        view?.exit()
    }

    override fun onViewCreate(view: WeatherView) {
        super.onViewCreate(view)
    }

    override fun onResume() {
        super.onResume()

        if (currentState == null) {
            currentState = State_01_LoadData()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onViewDestroy(mayBeRecreated: Boolean) {
        super.onViewDestroy(mayBeRecreated)
    }

    //.............................................................................................

    fun onReload() {
        weatherDataManager.clear()
        currentState = State_01_LoadData()
    }

    //.............................................................................................

    private fun onError(error: Throwable) {
        Timber.e(error)

        currentState = object : ErrorState(
                ErrorsHelper.buildErrorDescription(error, appProperties.IS_DEBUG),
                Action("Закрыть") { view?.exit() },
                Action("Повторить") { currentState = State_01_LoadData() }) {
            override fun enter() {
                view?.showError(message, closeAction, retryAction)
            }
        }
    }

    //.............................................................................................

    inner class State_01_LoadData : ViewState() {

        var request: Observable<WeatherResponse> by Delegates.notNull()
        var subscription: Subscription? = null

        override fun enter() {
            view?.showProgress()
            request = weatherDataManager.getWeather().toObservable().cache()
            doSubscribe()
        }

        private fun doSubscribe() {
            if (subscription == null) {
                subscription = request
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { weather ->
                                    this@WeatherPresenter.weather = weather
                                    this@WeatherPresenter.loadedAt = now()

                                    // Запрос (обычно )выполняется очень быстро - добавлена небольшая задержка,
                                    // чтобы дать показаться индикатору загрузки.
                                    handler.postDelayed({ goNext() }, 300)

                                },
                                { error -> handler.postDelayed({ onError(error) }, 300) }
                        )
            }
        }

        private fun doUnsubscribe() {
            subscription?.unsubscribe()
            subscription = null
        }

        override fun onResume() {
            super.onResume()
            doSubscribe()
        }

        override fun onPause() {
            doUnsubscribe()
            super.onPause()
        }

        override fun onGoNext() {
            currentState = State_02_ShowWeather()
        }

        override fun onExit() {
            doUnsubscribe()
            super.onExit()
        }

        override fun updateView() {
            view?.showProgress()
        }
    }

    //.............................................................................................

    inner class State_02_ShowWeather : ViewState() {
        override fun enter() {
            view?.showWeather(weather!!)
        }

        override fun onGoNext() {

        }

        override fun updateView() {
            if (weatherDataManager.isCachedDataExpired(loadedAt)) {
                currentState == State_01_LoadData()
            } else {
                view?.showWeather(weather!!)
            }
        }
    }
}