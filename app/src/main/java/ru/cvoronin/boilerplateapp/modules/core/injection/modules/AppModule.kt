package ru.cvoronin.boilerplateapp.modules.core.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.cvoronin.boilerplateapp.App
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope

@Module
class AppModule(val app : App) {

    val appContext : Context

    init {
        appContext = app
    }

    @Provides
    @ApplicationContext
    fun provideContext() = appContext

    @Provides
    fun provideApp() : App = app

}