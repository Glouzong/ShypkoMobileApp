package com.example.glouz.shypkoapp.launcherScreens;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.glouz.shypkoapp.R;
import com.example.glouz.shypkoapp.data.DataSetting;
import com.example.glouz.shypkoapp.launcher.ItemLauncher;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class ScreenFragmentsAdapter extends FragmentPagerAdapter {
    private Context context;
    private DataSetting dataSetting;
    private final ArrayList<LauncherFragment> launcherFragments;
    private final ArrayList<ItemLauncher> mData;
    private final int COUNT_FRAGMENT = 2;

    public ScreenFragmentsAdapter(FragmentManager fm, final ArrayList<ItemLauncher> mData, Context context) {
        super(fm);

        this.context = context;
        dataSetting = new DataSetting(context);

        this.mData = mData;
        Log.d("adapter", (this.mData != null) ? this.mData.size() + "" : "null");
        Log.d("adapter", "create fragments");
        launcherFragments = new ArrayList<LauncherFragment>();
        launcherFragments.add(LauncherFragment.newInstance());
        launcherFragments.add(LauncherFragment.newInstance());
        launcherFragments.get(0).setInfoAboutFragment(mData, true, dataSetting.checkMaket());
        launcherFragments.get(1).setInfoAboutFragment(mData, false,dataSetting.checkMaket());
    }

    @Override
    public Fragment getItem(int position) {
        return launcherFragments.get(position);
    }

    @Override
    public int getCount() {
        return COUNT_FRAGMENT;
    }

    public void removeApp(String nameApp) {
        for (int i = 0; i < mData.size(); ++i) {
            if (nameApp.equals(mData.get(i).getPackageName())) {
                Log.d("remove", nameApp + " " + mData.get(i).getPackageName());
                mData.remove(i);
                launcherFragments.get(0).removeApp(i);
                launcherFragments.get(1).removeApp(i);
                YandexMetrica.reportEvent("Удалено приложение приложение");
                break;
            }
        }
    }

    public void installApp(String nameApp) {
        Log.d("install", nameApp);
        Intent intent = new Intent();
        intent.setPackage(nameApp);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ItemLauncher result =
                new ItemLauncher(context.getPackageManager().resolveActivity(intent, 0), context.getPackageManager());
        mData.add(result);
        YandexMetrica.reportEvent("Установлено приложение");
        launcherFragments.get(0).addApp(mData.size() - 1);
        launcherFragments.get(1).addApp(mData.size() - 1);
    }

    public void updateApps() {
        launcherFragments.get(0).updateApps();
        launcherFragments.get(1).updateApps();
    }

    public void updateMaket() {
        Log.d("type maket", String.valueOf(dataSetting.checkMaket()));
        launcherFragments.get(0).updateGridLayoutManager(dataSetting.checkMaket());
    }
}
