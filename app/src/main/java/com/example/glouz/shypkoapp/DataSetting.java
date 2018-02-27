package com.example.glouz.shypkoapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataSetting {
    private Context context;
    private SharedPreferences appSettings;

    public DataSetting(Context newContext) {
        context = newContext;
        appSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    boolean checkFirstStart() {
        return appSettings.getBoolean(context.getString(R.string.keyFirstStart), true);
    }

    void setFirstStart() {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyFirstStart), false);
        editor.apply();
    }

    public String getTypeSort(){
        return appSettings.getString(context.getString(R.string.keySortApp),"keyNoSort");
    }

    boolean checkTheme() {
        return appSettings.getBoolean(context.getString(R.string.keyTheme), false);
    }

    public void setTheme(boolean whiteTheme) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyTheme), whiteTheme);
        editor.apply();
    }

    boolean checkMaket() {
        return appSettings.getBoolean(context.getString(R.string.keyMaket), false);
    }

    void setMaket(boolean typeMaket) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyMaket), typeMaket);
        editor.apply();
    }

    boolean checkLayout() {
        return appSettings.getBoolean(context.getString(R.string.keyTypeLayout), true);
    }

    void setTypeLayout(boolean gridType) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyTypeLayout), gridType);
        editor.apply();
    }

    boolean checkUpdateSettings() {
        return appSettings.getBoolean("keyUpdateSetttings", true);
    }
}
