package com.example.glouz.shypkoapp;

import android.app.Application;

import com.yandex.metrica.YandexMetrica;

public class LauncherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetrica.activate(getApplicationContext(), "46d24672-a692-4213-abbb-23e8d4d29178");
        YandexMetrica.enableActivityAutoTracking(this);
    }
}
