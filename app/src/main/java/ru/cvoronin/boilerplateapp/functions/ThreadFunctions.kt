package ru.cvoronin.boilerplateapp.functions

import ru.cvoronin.boilerplateapp.BuildConfig

fun currentThreadInfo(prefix: String = ""): String {
    val thread = Thread.currentThread()
    return "$prefix[(${thread.id}) ${thread.threadGroup.name}/${thread.name} ]"
}

fun printValueWithThreadInfo(value: Any?) : Any? {
    if (BuildConfig.DEBUG) {
        println("${value?.toString()} ${currentThreadInfo(" : ")}")
    }

    return value
}
