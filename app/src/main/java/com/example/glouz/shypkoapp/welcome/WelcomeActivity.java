package com.example.glouz.shypkoapp.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.glouz.shypkoapp.DataSetting;
import com.example.glouz.shypkoapp.NavigationViewActivity;
import com.example.glouz.shypkoapp.R;
import com.yandex.metrica.YandexMetrica;

import io.fabric.sdk.android.Fabric;

public class WelcomeActivity extends AppCompatActivity {

    private DataSetting settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = new DataSetting(this);
        if (!settings.checkFirstStart()) {
            finalizeSetting(null);
        }
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_welcome);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        WelcomePageAdapter mSectionsPagerAdapter = new WelcomePageAdapter(fragmentManager);
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public void onRadioButtonClickedWhite(View view) {
        settings.setTheme(false);
        YandexMetrica.reportEvent("Настройка с начальной страницы", "{\"Выбор тема\":\"Светлый\"}");
    }

    public void onRadioButtonClickedBlack(View view) {
        settings.setTheme(true);
        YandexMetrica.reportEvent("Настройка с начальной страницы", "{\"Выбор тема\":\"Тёмный\"}");
    }

    public void onRadioButtonClickedStandard(View view) {
        if (view.getId() != R.id.radio_standart) {
            RadioButton standart = view.getRootView().findViewById(R.id.radio_standart);
            standart.setChecked(true);
        }
        RadioButton dense = view.getRootView().findViewById(R.id.radio_dense);
        dense.setChecked(false);
        YandexMetrica.reportEvent("Настройка с начальной страницы", "{\"Выбор макета\":\"Стандартный\"}");
        settings.setMaket(false);
    }

    public void onRadioButtonClickedDense(View view) {
        if (view.getId() != R.id.radio_dense) {
            RadioButton dense = view.getRootView().findViewById(R.id.radio_dense);
            dense.setChecked(true);
        }
        RadioButton standart = view.getRootView().findViewById(R.id.radio_standart);
        standart.setChecked(false);
        YandexMetrica.reportEvent("Настройка с начальной страницы", "\"Выбор макета\":\"Плотный\"}");
        settings.setMaket(true);
    }

    public void finalizeSetting(View view) {
        settings.setFirstStart();
        Intent intent = new Intent(this, NavigationViewActivity.class);
        startActivity(intent);
        finish();
        YandexMetrica.reportEvent("Закончили начальную настройку");
    }
}
