package ru.cvoronin.boilerplateapp.modules.weather.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.method.TransformationMethod
import android.view.MenuItem
import com.squareup.picasso.Picasso
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.apptoolbar.*
import kotlinx.android.synthetic.main.error_panel.*
import kotlinx.android.synthetic.main.progress_panel.*
import kotlinx.android.synthetic.main.weather_activity.*
import kotlinx.android.synthetic.main.weather_activity_datapanel.*
import org.jetbrains.anko.onClick
import ru.cvoronin.boilerplateapp.R
import ru.cvoronin.boilerplateapp.app
import ru.cvoronin.boilerplateapp.functions.gone
import ru.cvoronin.boilerplateapp.functions.show
import ru.cvoronin.boilerplateapp.modules.core.AppProperties
import ru.cvoronin.boilerplateapp.modules.weather.domain.WeatherResponse
import ru.simpls.brs2.commons.functions.wasInit
import ru.simpls.brs2.commons.modules.core.Action
import ru.simpls.brs2.commons.modules.core.mvp.PresenterManager
import ru.simpls.brs2.mobbank.modules.common.utils.ErrorsHelper
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(), WeatherView {

    @Inject
    lateinit var presenter: WeatherPresenter

    @Inject
    lateinit var appProperties: AppProperties

    @Inject
    lateinit var picasso : Picasso

    private var dataWasLoaded : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Current Weather"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createOrRestorePresenter(savedInstanceState)
        presenter.onViewCreate(this)

        btnRefresh.onClick { presenter.onReload() }
    }

    override fun onResume() {
        super.onResume()
        wasInit { presenter.onResume() }
    }

    override fun onPause() {
        wasInit { presenter.onPause() }
        super.onPause()
    }

    override fun onDestroy() {
        wasInit { presenter.onViewDestroy(!isFinishing) }

        if (isFinishing) {
            app().clearWeatherComponent()
        }

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        wasInit { PresenterManager.savePresenter(presenter, outState) }
    }

    private fun createOrRestorePresenter(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val restored = PresenterManager.restorePresenter(savedInstanceState)
            if (restored != null && restored is WeatherPresenter) {
                presenter = restored
                return
            }
        }

        app().createWeatherComponent().inject(this)
    }

    //.............................................................................................

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            presenter.onUp()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() = presenter.onBack()

    //.............................................................................................

    override fun exit() {
        finishAfterTransition()
    }

    fun hideAll() {
        progressPanel.gone()
        errorPanel.gone()
    }

    override fun showProgress() {
        hideAll()

        // Панель с индикатором прогресса частично прозрачная, если данные ещё не были загружены,
        // то будут видны заготовки без данных, некрасиво. Можно либо убрать значения из разметки
        // (но тогда не так удобно делать саму разметку), либо при первой загрузке просто спрятать.
        if (!dataWasLoaded) {
            dataPanel.gone()
        }

        progressPanel.show()
    }

    override fun showError(message: String, defaultAction: Action, secondaryAction: Action?) {
        TransitionManager.beginDelayedTransition(root, Fade())
        hideAll()
        ErrorsHelper.showErrorPanel(errorPanel, message, defaultAction, secondaryAction, false)
    }

    override fun showWeather(weather: WeatherResponse) {
        dataWasLoaded = true

        with(weather) {
            locationValue.text = "$name"

            tempValue.text = mainInfo!!.temp.toString()
            windSpeedValue.text = wind!!.speed.toString()
            windDirectionValue.text = wind!!.deg.toString()

            val current = weatherList!![0]
            conditionsValue.text = current.description
            showIcon(current.icon)
        }

        TransitionManager.beginDelayedTransition(root, Fade())
        hideAll()
        dataPanel.show()
    }

    private fun showIcon(icon: String?) {
        weatherIcon.gone()

        if (icon == null) {
            return
        }

        //http://openweathermap.org/img/w/04n.png
        val uri = "${appProperties.WEATHER_SERVER_IMAGE_URL}${icon}.png"
        picasso.load(uri).into(weatherIcon)
        weatherIcon.show()
    }
}
