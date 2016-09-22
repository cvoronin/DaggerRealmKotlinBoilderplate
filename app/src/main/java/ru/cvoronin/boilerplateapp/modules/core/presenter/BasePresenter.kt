package ru.simpls.brs2.commons.modules.core.mvp.presenters

import android.os.Handler
import ru.simpls.brs2.commons.functions.now
import ru.simpls.brs2.commons.modules.core.stateengine.StateEngine
import ru.simpls.brs2.commons.modules.core.stateengine.ViewState
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*

abstract class BasePresenter<V> {

    val id = UUID.randomUUID().toString()

    protected val stateEngine = StateEngine()
    protected val instanceName = "${this.javaClass.simpleName} @${Integer.toHexString(hashCode())}"

    protected var viewRef: WeakReference<V>? = null
    var view: V?
        set(value) {
            if (value == null) {
                viewRef = null
            } else {
                viewRef = WeakReference<V>(value)
            }
        }
        get() {
            if (viewRef != null) return viewRef!!.get()
            else return null
        }

    protected var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    protected var viewAttachMoment: Long = 0
    protected var viewResumeMoment: Long = 0
    protected var isViewResumed: Boolean = false

    protected var lastShownEventId: UUID = UUID.randomUUID()

    protected val handler = Handler()

    var isExitEnabled: Boolean = true
    var exitDisabledMessage: String = "Пожалуйста дождитесь окончания выполнения запроса"

    init {
        Timber.v("*** create presenter: ${this.javaClass.simpleName} @${Integer.toHexString(hashCode())}")
    }

    open fun onViewCreate(view: V) {
        Timber.v("*** $instanceName onViewCreate")
        this.view = view
        viewAttachMoment = now()
        resetLastShownId()
    }

    open fun onResume() {
        Timber.v("*** $instanceName onResume")

        viewResumeMoment = now()
        isViewResumed = true

        stateEngine.currentState?.onResume()
        stateEngine.currentState?.updateView()
    }

    open fun onPause() {
        Timber.v("*** $instanceName onPause")

        isViewResumed = false
        stateEngine.currentState?.onPause()
    }

    open fun onViewDestroy(mayBeRecreated: Boolean) {
        Timber.d("onViewDestroy: $instanceName, mayBeRecreated: $mayBeRecreated")

        view = null

        if (!mayBeRecreated) {
            unsubscribeAll()

            stateEngine.currentState?.onExit()
        }
    }

    protected fun addSubscription(subscription: Subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }

        compositeSubscription!!.add(subscription)
    }

    protected fun unsubscribeAll() {
        if (compositeSubscription != null) {
            compositeSubscription!!.unsubscribe()
            compositeSubscription = null
        }
    }

    /**
     * Используется при обработке событий с учётом того, что активити может находиться в фоне - и тогда
     * при поднятии из фона событие нужно показать.
     * Более того, если событие уже было показано, второй раз его показывать не надо: например,
     * мы загрузили длинный список данных и показали их. Пользователь пролистал его и свернул приложение.
     * После того, как пользователь развернул приложение сообщать о загруженных данных уже не надо -
     * если мы это сделаем, список будет пересоздан и предыдущее местоположение будет утеряно.
     */
    protected fun doIfResumedAndWasNotShown(eventId: UUID, action: () -> Unit) {
        if (isViewResumed) {
            if (lastShownEventId != eventId) {
                lastShownEventId = eventId
                action()
            }
        }
    }

    protected fun resetLastShownId() {
        lastShownEventId = UUID.randomUUID()
    }

    protected fun setState(state: ViewState?) {
        stateEngine.currentState = state
    }

    protected fun currentState(): ViewState? = stateEngine.currentState

    var currentState: ViewState?
        get() = stateEngine.currentState
        set(value) {
            stateEngine.currentState = value
        }

    open fun onBack() {
        if (!(currentState?.goBack() ?: false)) {
            exitCurrentView()
        }
    }

    open fun onUp() {
        if (!(currentState?.onUp() ?: false)) {
            exitCurrentView()
        }
    }

    abstract fun exitView()

    private fun exitCurrentView(): Boolean {
        exitView()
        return true
    }
}