package ru.simpls.brs2.commons.modules.core.validation

import java.util.*

class ValidationResult {

    var isValid: Boolean = true
        get() = errorMessages.isEmpty()

    var errorMessages: MutableList<String>
    var noControlKeyErrorMessages: MutableList<String>

    // Используется для привязки конкретной ошибки к конкретному элементу ввода
    // Привязка осуществляется по коду элемента ввода
    // Если ошибка добавляется в этот контейнер, то она автоматически будет добавлена и в общий
    // список ошибок errorMessages
    var controlMessages: MutableMap<String, MutableList<String>>

    init {
        errorMessages = ArrayList()
        noControlKeyErrorMessages = ArrayList()
        controlMessages = HashMap()
    }

    fun addError(message: String, controlKey: String? = null) {
        errorMessages.add(message)

        if (controlKey != null) {
            val item = controlMessages[controlKey]
            if (item == null) {
                controlMessages.put(controlKey, arrayListOf(message))
            } else {
                item.add(message)
            }

        } else {
            noControlKeyErrorMessages.add(message)
        }
    }

    fun check(condition: Boolean, message: String, controlKey: String? = null): Boolean =
            when (condition) {
                false -> {
                    addError(message, controlKey)
                    false
                }
                else -> true
            }
}