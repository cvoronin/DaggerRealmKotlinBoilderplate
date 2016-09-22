package ru.simpls.brs2.commons.modules.core.stateengine

import ru.simpls.brs2.commons.modules.core.Action

abstract class ErrorState(val message: String,
                          val closeAction: Action,
                          val retryAction: Action? = null) : ViewState() {

    override fun onGoNext() {
        closeAction.action.invoke()
    }

    override fun goBack(): Boolean {
        closeAction.action.invoke()
        return true
    }
}