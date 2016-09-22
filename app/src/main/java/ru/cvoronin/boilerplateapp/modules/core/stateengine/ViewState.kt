package ru.simpls.brs2.commons.modules.core.stateengine

import ru.simpls.brs2.commons.modules.core.validation.ValidationResult
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import java.util.*

abstract class ViewState {

    protected val stateId = UUID.randomUUID()

    protected var validationResult = ValidationResult()

    protected var compositeSubscription : CompositeSubscription? = null

    init {
        initState()
    }

    /** Выполняется только один раз - при создании экземпляра состояния, этим отличается от enter,
     * который может вызываться несколько раз
     * */
    protected fun initState() {
    }

    abstract fun enter()

    fun goNext() {
        clearErrors()
        beforeGoNext()
        validate()

        if (hasErrors()) {
            showValidationErrors()
        } else {
            onGoNext()
        }
    }

    abstract protected fun onGoNext()

    /** Возвращает true если событие обработано внутри состояния и не должно больше обрабатываться */
    open fun goBack(): Boolean = false

    protected fun beforeGoNext() {
    }

    open protected fun validate() {
    }

    private fun clearErrors() {
        validationResult = ValidationResult()
    }

    open protected fun showValidationErrors() {

    }

    protected fun hasErrors(): Boolean = !validationResult.isValid

    open fun onExit() {
        unsubscribe()
    }

    open fun onUp(): Boolean = false

    open fun updateView() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    protected fun addSubscription(subscription : Subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }

        compositeSubscription!!.add(subscription)
    }

    protected fun unsubscribe() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
    }
}