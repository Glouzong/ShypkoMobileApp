package com.example.glouz.shypkoapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.crashlytics.android.Crashlytics;

import com.crashlytics.android.ndk.CrashlyticsNdk;

import io.fabric.sdk.android.Fabric;

import net.hockeyapp.android.CrashManager;

public class MainActivity extends AppCompatActivity {

    private ViewPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionsPagerAdapter = new ViewPagerAdapter(fragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        checkForCrashes();

    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    public void onRadioButtonClickedWhite(View view) {
        //TODO
    }

    public void onRadioButtonClickedBlack(View view) {
        //TODO
    }

    public void onRadioButtonClickedStandart(View view) {
        if (view.getId() != R.id.radio_standart) {
            RadioButton standart = view.getRootView().findViewById(R.id.radio_standart);
            standart.setChecked(true);
        }
        RadioButton dense = view.getRootView().findViewById(R.id.radio_dense);
        dense.setChecked(false);
    }

    public void onRadioButtonClickedDense(View view) {
        if (view.getId() != R.id.radio_dense) {
            RadioButton dense = view.getRootView().findViewById(R.id.radio_dense);
            dense.setChecked(true);
        }
        RadioButton standart = view.getRootView().findViewById(R.id.radio_standart);
        standart.setChecked(false);
    }

    public void setXmlInfoApp(View view) {
        setContentView(R.layout.activity_main);
    }
}
