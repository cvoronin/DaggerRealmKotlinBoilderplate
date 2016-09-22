package ru.cvoronin.boilerplateapp.modules.core

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.cvoronin.boilerplateapp.App
import ru.cvoronin.boilerplateapp.modules.core.injection.AppComponent
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
class AppComponentTest {

    var appComponent: AppComponent? = null

    @Before
    fun setUp() {
        val app = RuntimeEnvironment.application as App
        appComponent = app.appComponent
    }

    @Test
    fun checkInit() {
        assertNotNull(appComponent)

        // Robolectric does not support JNI, it is impossible to test Realm using Robolectric.
        // You can uncomment next line - exception will be thrown
        // assertNotNull(appComponent!!.realm())

        assertNotNull(appComponent!!.app())
        assertNotNull(appComponent!!.gson())
        assertNotNull(appComponent!!.context())
        assertNotNull(appComponent!!.picasso())
        assertNotNull(appComponent!!.appProperties())
    }
}