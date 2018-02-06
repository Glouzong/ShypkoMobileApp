package com.example.glouz.shypkoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomePageFragments extends Fragment {

    private static final String PAGE_NUMBER_KEY = "page_number";

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

        return inflater.inflate(idPage, container, false);
    }
}
