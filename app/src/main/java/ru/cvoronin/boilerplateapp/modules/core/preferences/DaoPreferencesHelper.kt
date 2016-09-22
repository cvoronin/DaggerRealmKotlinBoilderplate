package ru.simpls.brs2.commons.modules.core.preferenses

import android.content.Context
import android.content.SharedPreferences
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope
import javax.inject.Inject

@ApplicationScope
open class DaoPreferencesHelper {

    private val PREF_FILE_NAME = "dao.preferences"

    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    constructor(@ApplicationContext context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    open fun saveLoadMoment(className: String, loadMoment: Long = System.currentTimeMillis(), login: String = "any") {
        sharedPreferences.edit()
                .putLong("$className/$login", loadMoment)
                .apply()
    }

    open fun clearLoadMoment(className: String, login: String = "any") {
        saveLoadMoment(className, 0L, login)
    }

    open fun getSavedMoment(className: String, login: String = "any"): Long {
        return sharedPreferences.getLong("$className/$login", 0L)
    }

    /**
     * Определяет факт устаревания данных, которые были сохранены в БД для пользователя login
     * Испольузует значение periodMinutes - период, в течении которого данные считаются актуальными
     * с момента их загрузки
     */
    open fun isStoredDataExpired(className: String, periodMinutes: Int, login: String = "any"): Boolean {
        val loadMoment = getSavedMoment(className, login)

        // Данные устарели, если с момента загрузки до декущего момента времени прошло больше времени,
        // чем указанное значение
        return System.currentTimeMillis() - loadMoment > periodMinutes * 60 * 1000
    }

    /**
     * Определяет факт устаревания ранее загруженного в память набора данных.
     * Набор считается устаревшим, если:
     * - с момента его формирования до текущего момента времени прошло более, чем periodMinutes минут
     * - данные в БД были сохранены позднее, чем момент времени создания проверяемого набора данных
     *   (то есть уже после его создания)
     */
    open fun isDataExpired(className: String, createdAt: Long, periodMinutes: Int, login: String = "any"): Boolean {
        val savedMoment = getSavedMoment(className, login)

        // Данные устарели, если с момента загрузки до декущего момента времени прошло больше времени,
        // чем указанное значение
        return when {
        // С момента загрузки до текущего момента времени прошло больше указанного количества минут
            System.currentTimeMillis() - savedMoment > periodMinutes * 60 * 1000 -> true

        // Данные были загружены поздее, чем создан указанный набор данных
            savedMoment > createdAt -> true

        // Иначе данные являются актуальными
            else -> false
        }
    }
}

