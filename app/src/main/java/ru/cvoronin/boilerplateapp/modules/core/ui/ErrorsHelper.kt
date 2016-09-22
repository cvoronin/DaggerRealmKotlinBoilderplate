package ru.simpls.brs2.mobbank.modules.common.utils

import android.view.View
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import ru.cvoronin.boilerplateapp.R
import ru.cvoronin.boilerplateapp.functions.fadeIn
import ru.cvoronin.boilerplateapp.functions.show
import ru.simpls.brs2.commons.modules.core.Action
import timber.log.Timber
import java.io.IOException

object ErrorsHelper {

    @Suppress("unused")
    fun showErrorPanel(errorPanel: View, t: Throwable, defaultAction: Action? = null, secondaryAction: Action? = null, showDebugInfo: Boolean = false) =
            showErrorPanel(errorPanel, buildErrorDescription(t, showDebugInfo), defaultAction, secondaryAction)

    @Suppress("unused")
    fun showErrorPanel(errorPanel: View, errorMessage: String, defaultAction: Action? = null, secondaryAction: Action? = null, useFade: Boolean = true) {
        errorPanel.find<TextView>(R.id.errorMessage).text = errorMessage
        errorPanel.find<View>(R.id.errorActionsPanel).visibility = View.GONE

        with(errorPanel.find<Button>(R.id.errorActionRight)) {
            when (defaultAction) {
                null -> visibility = View.GONE
                else -> {
                    text = defaultAction.name
                    onClick { defaultAction.action() }
                    visibility = View.VISIBLE
                    errorPanel.find<View>(R.id.errorActionsPanel).visibility = View.VISIBLE
                }
            }
        }

        with(errorPanel.find<Button>(R.id.errorActionLeft)) {
            when (secondaryAction) {
                null -> visibility = View.GONE
                else -> {
                    text = secondaryAction.name
                    onClick { secondaryAction.action() }
                    visibility = View.VISIBLE
                    errorPanel.find<View>(R.id.errorActionsPanel).visibility = View.VISIBLE
                }
            }
        }

        when (useFade) {
            true -> errorPanel.fadeIn()
            else -> errorPanel.show()
        }
    }

    fun buildErrorDescription(t: Throwable, showDebugInfo: Boolean = false): String {
        Timber.d("... buildErrorDescription: ${t.javaClass} ${t.message}")

        val sb = StringBuilder()

        if (t is IOException) {
            sb.append("При выполнении запроса произошла ошибка.\nПожалуйста, проверьте соединение с Интернетом.")
        } else {
            sb.append("При выполнении запроса произошла ошибка.\nПожалуйста, повторите попытку позднее.")
        }


        if (showDebugInfo) {
            sb.append("\n\n(Видно только при отладке):")
            sb.append("\n${t.javaClass}")
            sb.append("\n${t.message}")
        }

        return sb.toString()
    }
}