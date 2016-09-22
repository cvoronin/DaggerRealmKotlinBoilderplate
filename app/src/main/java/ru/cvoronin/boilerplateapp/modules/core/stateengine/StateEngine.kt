package ru.simpls.brs2.commons.modules.core.stateengine

import timber.log.Timber

class StateEngine {

    var currentState : ViewState? = null
        set(value) {
            Timber.v("*** setState: ${value?.javaClass}")
            field?.onExit()

            field = value
            field?.enter()
        }

    fun clear() {
        currentState?.onExit()
        currentState = null
    }

    fun updateView() {
        currentState?.updateView()
    }
}