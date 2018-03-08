package com.example.glouz.shypkoapp.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.glouz.shypkoapp.data.DataSetting;
import com.example.glouz.shypkoapp.R;
import com.yandex.metrica.YandexMetrica;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataSetting settings = new DataSetting(this);
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Settings_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.container_settings, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        YandexMetrica.reportEvent("Возращение с экрана настроек");
        super.onBackPressed();
    }

}
