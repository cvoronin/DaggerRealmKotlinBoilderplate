package ru.cvoronin.boilerplateapp.modules.core

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationContext
import ru.cvoronin.boilerplateapp.modules.core.injection.ApplicationScope
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
open class AppInfo {

    val os: String = "ANDROID"
    var osVersion: String? = null
    var appVersionCode: Int? = 0
    var appVersionName: String = "UNKNOWN"
    var deviceId: String? = null
    var deviceModel: String? = null
    var sdkInt: Int = Build.VERSION.SDK_INT

    @Inject
    @Suppress("unused")
    constructor(@ApplicationContext context: Context) {
        Timber.d("AppInfo create")

        osVersion = Build.VERSION.RELEASE

        try {
            with(context.packageManager.getPackageInfo(context.packageName, 0)) {
                appVersionName = versionName
                appVersionCode = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, e.message)
        }

        deviceModel = "${Build.MANUFACTURER}_${Build.MODEL}".toUpperCase()
    }
}
