package ru.simpls.brs2.commons.modules.core.stateengine

abstract class NoBackState() : ViewState() {

    companion object {
        val DEFAULT_MESSAGE = "Пожалуйста, дождитесь окончания выполнения запроса"
    }

    abstract fun showNoBackMessage(noBackMessage : String = DEFAULT_MESSAGE)

    override fun goBack(): Boolean {
        showNoBackMessage()
        return true
    }

    override fun onUp(): Boolean {
        showNoBackMessage()
        return true
    }
}