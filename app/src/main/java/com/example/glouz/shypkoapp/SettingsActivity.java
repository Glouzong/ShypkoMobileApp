package com.example.glouz.shypkoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataSetting settings = new DataSetting(this);
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.container_settings, new SettingsFragment()).commit();
    }
}
