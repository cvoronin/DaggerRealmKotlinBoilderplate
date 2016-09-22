package ru.simpls.brs2.commons.modules.core

data class Action(val name : String, val action: () -> Unit)