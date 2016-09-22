package ru.simpls.brs2.commons.functions

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import ru.cvoronin.boilerplateapp.modules.core.AppProperties

fun logAnswersErrorEvent(appProperties: AppProperties, event: String, error: Throwable) {
    if (appProperties.USE_ANSWERS) {

        var message = error.message ?: error.javaClass.name
        if (message.length > 100) {
            message = message.substring(100)
        }

        Answers.getInstance().logCustom(
                CustomEvent("BankProducts Load Error")
                        .putCustomAttribute("message", message)
                        .putCustomAttribute("errorClass", error.javaClass.name)
        )
    }
}
