package ru.cvoronin.boilerplateapp.modules.core.injection.modules

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.realm.RealmObject
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope

@Module
class GsonModule {

    @Provides
    @ApplicationScope
    fun provideGson(): Gson =
            GsonBuilder()
                    .setLenient()

                    .setDateFormat("yyyy-MM-dd HH:mm:ss")

                    .setExclusionStrategies(object : ExclusionStrategy {
                        override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                            return false
                        }

                        override fun shouldSkipField(f: FieldAttributes?): Boolean =
                                when {
                                    f?.declaredClass == RealmObject::class.java -> true
                                    else -> false
                                }
                    })
                    .create()
}