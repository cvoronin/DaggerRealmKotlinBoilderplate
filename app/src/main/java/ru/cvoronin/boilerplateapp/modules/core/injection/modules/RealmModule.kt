package ru.cvoronin.boilerplateapp.modules.core.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope

@Module
class RealmModule {

    companion object {
        val REALM_SCHEMA_VERSION: Long = 1
    }

    @Provides
            // @ApplicationScope НЕ указывается - нам понадобится много экземпляров
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm = Realm.getInstance(realmConfiguration)


    @Provides
    @ApplicationScope
    fun provideRealmConfig(@ApplicationContext context: Context,
                           appProperties: AppProperties,
                           realmMigration: RealmMigration): RealmConfiguration {

        if (appProperties.REALM_DB_NAME == null) {
            throw IllegalStateException("allconfig.properties должен содержать параметр realm.name с значением имени файла realm")
        }

        val realmConfig = RealmConfiguration.Builder(context)
                .name(appProperties.REALM_DB_NAME)
                .schemaVersion(REALM_SCHEMA_VERSION)

        when {
        // При миграции, в случае, если удаляются какие-то данные, нужно
        // не забыть сбросить и значение момента времени загрузки этих данных (sharedPrefs)
        // иначе алгоритм получения хэшированных данных сломается
            appProperties.REALM_USE_MIGRATION ->
                realmConfig.migration(realmMigration)

            else ->
                realmConfig.deleteRealmIfMigrationNeeded()
        }

        return realmConfig.build()
    }

    @Provides
    @ApplicationScope
    fun provideRealmMigration(): RealmMigration =
            RealmMigration { realm, oldVersion, newVersion ->

            }
}
