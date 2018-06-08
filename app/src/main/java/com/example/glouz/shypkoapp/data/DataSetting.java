package com.example.glouz.shypkoapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.glouz.shypkoapp.R;

public class DataSetting {
    private Context context;
    private SharedPreferences appSettings;

    public DataSetting(Context newContext) {
        context = newContext;
        appSettings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean checkFirstStart() {
        return appSettings.getBoolean(context.getString(R.string.keyFirstStart), true);
    }

    public void setFirstStart() {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyFirstStart), false);
        editor.apply();
    }

    public String getTypeSort(){
        return appSettings.getString(context.getString(R.string.keySortApp),"keyNoSort");
    }

    public boolean checkTheme() {
        return appSettings.getBoolean(context.getString(R.string.keyTheme), false);
    }

    public void setTheme(boolean whiteTheme) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyTheme), whiteTheme);
        editor.apply();
    }

    public boolean checkMaket() {
        return appSettings.getBoolean(context.getString(R.string.keyMaket), false);
    }

    public void setMaket(boolean typeMaket) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyMaket), typeMaket);
        editor.apply();
    }

    public boolean checkClickSort() {
        return appSettings.getBoolean(context.getString(R.string.keyClickSort), false);
    }

    public void setClickSort(boolean checkClickSort) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyClickSort), checkClickSort);
        editor.apply();
    }

    public void setFlagNewInfo(boolean flagNewInfo) {
        SharedPreferences.Editor editor = appSettings.edit();
        editor.putBoolean(context.getString(R.string.keyFlagNewInfo), flagNewInfo);
        editor.apply();
    }


    public boolean checkFlagNewInfo() {
        return appSettings.getBoolean(context.getString(R.string.keyFlagNewInfo), false);
    }
}
