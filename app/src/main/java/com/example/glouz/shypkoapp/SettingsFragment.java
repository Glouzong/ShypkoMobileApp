package com.example.glouz.shypkoapp;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Preference theme = this.findPreference(this.getString(R.string.keyTheme));
        theme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                getActivity().recreate();
                return true;
            }
        });
    }
}
