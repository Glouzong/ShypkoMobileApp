package com.example.glouz.shypkoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class WelcomePageFragments extends Fragment {

    private static final String PAGE_NUMBER_KEY = "page_number";
    DataSetting settings;

    static WelcomePageFragments newInstance(int page) {
        WelcomePageFragments welcomePageFragments = new WelcomePageFragments();
        Bundle arguments = new Bundle();
        arguments.putInt(PAGE_NUMBER_KEY, page);
        welcomePageFragments.setArguments(arguments);
        return welcomePageFragments;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final int numberPage = getArguments().getInt(PAGE_NUMBER_KEY);
        int idPage;
        switch (numberPage) {
            case 0:
                idPage = R.layout.welcome_page;
                break;
            case 1:
                idPage = R.layout.info_app_page;
                break;
            case 2:
                idPage = R.layout.set_theme_page;
                break;
            case 3:
                idPage = R.layout.set_maket_page;
                break;
            default:
                idPage = R.layout.welcome_page;
        }

        View view = inflater.inflate(idPage, container, false);
        settings = new DataSetting(view.getContext());
        switch (numberPage) {
            case 2:
                if (settings.checkTheme()) {
                    RadioButton button = view.findViewById(R.id.radio_black);
                    button.setChecked(true);
                }
                break;
            case 3:
                if (settings.checkMaket()) {
                    RadioButton button = view.findViewById(R.id.radio_dense);
                    button.setChecked(true);
                    button = view.findViewById(R.id.radio_standart);
                    button.setChecked(false);
                }
                break;
        }
        return view;
    }
}
