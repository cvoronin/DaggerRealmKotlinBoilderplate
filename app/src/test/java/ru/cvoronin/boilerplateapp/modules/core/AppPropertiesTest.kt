package ru.cvoronin.boilerplateapp.modules.core

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.cvoronin.boilerplateapp.App
import ru.cvoronin.boilerplateapp.modules.core.injection.AppComponent
import kotlin.properties.Delegates

@RunWith(RobolectricTestRunner::class)
class AppPropertiesTest {

    var app : App by Delegates.notNull()
    var appComponent : AppComponent? = null
    var appProperties : AppProperties? = null

    @Before
    fun setUp() {
        app = RuntimeEnvironment.application as App
        appComponent = app.appComponent
        appProperties = appComponent?.appProperties()
    }

    @Test
    fun assertIsNotNull() {
        assertNotNull(appComponent)
        assertNotNull(appProperties)
    }

    @Test
    fun checkValues() {
        assertEquals("One", appProperties?.TEST_VALUE_STR)
        assertEquals(1, appProperties?.TEST_VALUE_INT)
        assertEquals(true, appProperties?.TEST_VALUE_BOOL)
    }
}