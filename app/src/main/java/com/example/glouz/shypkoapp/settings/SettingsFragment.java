package com.example.glouz.shypkoapp.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.glouz.shypkoapp.R;
import com.yandex.metrica.YandexMetrica;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setThemeClickListener();
        setFirstStartClickListener();
        setMaketClickListener();
        setTypeSortClickListener();
    }

    void setThemeClickListener() {
        final String key = this.getString(R.string.keyTheme);
        Preference theme = this.findPreference(key);
        theme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String eventSetTheme = "{\"Выбор темы\":";
                getActivity().recreate();
                if (preference.getSharedPreferences().getBoolean(key, false)) {
                    eventSetTheme += "\"Тёмная\"}";
                } else {
                    eventSetTheme += "\"Светлая\"}";
                }
                YandexMetrica.reportEvent("Настройки", eventSetTheme);
                return true;
            }
        });
    }

    void setFirstStartClickListener() {
        final String key = this.getString(R.string.keyFirstStart);
        Preference firstStart = this.findPreference(key);
        firstStart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String eventSetTheme = "{\"Установка первого старта\":";
                if (preference.getSharedPreferences().getBoolean(key, false)) {
                    eventSetTheme += "\"Включить\"}";
                } else {
                    eventSetTheme += "\"Выключить\"}";
                }
                YandexMetrica.reportEvent("Настройки", eventSetTheme);
                return true;
            }
        });
    }

    void setMaketClickListener() {
        final String key = this.getString(R.string.keyFirstStart);
        Preference firstStart = this.findPreference(key);
        firstStart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String eventSetTheme = "{\"Выбор макета\":";
                if (preference.getSharedPreferences().getBoolean(key, false)) {
                    eventSetTheme += "\"Плотный\"}";
                } else {
                    eventSetTheme += "\"Стндартный\"}";
                }
                YandexMetrica.reportEvent("Настройки", eventSetTheme);
                return true;
            }
        });
    }

    void setTypeSortClickListener() {
        final String key = getString(R.string.keySortApp);

        final Preference firstStart = findPreference(key);
        firstStart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                String eventSetTypeSort = "{\"Тип сортировки\":";
                switch (preference.getSharedPreferences().getString(key, "keyNoSort")) {
                    case "keySortNameAZ":
                        eventSetTypeSort += "\"По имени от А до Я\"}";
                        break;
                    case "keySortNameZA":
                        eventSetTypeSort += "\"По имени от Я до А\"}";
                        break;
                    case "keySortFrequency":
                        eventSetTypeSort += "\"По частоте использования\"}";
                        break;
                    case "keySortDateInstall":
                        eventSetTypeSort += "\"По дате установке\"}";
                        break;
                    default:
                        eventSetTypeSort += "\"Без сортировки\"}";
                        break;
                }

                setClickTypeSort(preference);
                YandexMetrica.reportEvent("Выбран вариант сортировки приложений", eventSetTypeSort);
                return true;
            }

            private void setClickTypeSort(Preference preferences) {//using in dataSettings
                final String keySort = getString(R.string.keyClickSort);
                SharedPreferences.Editor editor = preferences.getSharedPreferences().edit();
                editor.putBoolean(keySort, true);
                editor.apply();
            }
        });
    }
}
