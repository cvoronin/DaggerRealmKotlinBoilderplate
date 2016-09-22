package ru.cvoronin.boilerplateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import ru.cvoronin.boilerplateapp.modules.weather.ui.WeatherActivity;
import ru.simpls.brs2.commons.modules.core.Action;
import ru.simpls.brs2.commons.modules.core.stateengine.StateEngine;
import ru.simpls.brs2.commons.modules.core.stateengine.ViewState;

public class MainActivity extends AppCompatActivity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Boilerplate Application");

        Button btnShowWeather = (Button) findViewById(R.id.btnWeather);
        btnShowWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeather();
            }
        });
    }

    private void showWeather() {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }

}
