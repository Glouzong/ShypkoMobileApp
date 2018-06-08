package com.example.glouz.shypkoapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.glouz.shypkoapp.data.DataBase;
import com.example.glouz.shypkoapp.data.DataImages;
import com.example.glouz.shypkoapp.data.DataSetting;
import com.example.glouz.shypkoapp.launcher.ItemLauncher;
import com.example.glouz.shypkoapp.launcherScreens.ScreenFragmentsAdapter;
import com.example.glouz.shypkoapp.settings.SettingsActivity;
import com.example.glouz.shypkoapp.userInfo.UserInfoActivity;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class NavigationViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DataSetting dataSetting;
    private DataBase dataBase;

    private final ArrayList<ItemLauncher> mData = new ArrayList<>();

    private ScreenFragmentsAdapter screenFragmentsAdapter;
    private ViewPager viewScreen;
    private UpdateListAppReceiver receiver;
    private boolean lastFlagTheme, lastFlagMaket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataSetting = new DataSetting(this);
        if (dataSetting.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
            lastFlagTheme = true;
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
            lastFlagTheme = false;
        }
        lastFlagMaket = dataSetting.checkMaket();
        super.onCreate(savedInstanceState);

        dataBase = new DataBase(this);
        setData();
        setContentView(R.layout.activity_navigation_view);
        initNavigationView();
        initRecyclerView();
        initReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dataSetting.checkFlagNewInfo()) {
            dataSetting.setFlagNewInfo(false);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            final View navigationHeaderView = navigationView.getHeaderView(0);
            setDataFromUserInfo(navigationHeaderView);
            return;
        }
        if (dataSetting.checkTheme() != lastFlagTheme) {
            this.recreate();
            return;
        }
        if (dataSetting.checkMaket() != lastFlagMaket) {
            lastFlagMaket = dataSetting.checkMaket();
            screenFragmentsAdapter.updateMaket();
            return;
        }
        if (dataSetting.checkClickSort()) {
            sortData();
            screenFragmentsAdapter.updateApps();
        }
    }

    @Override
    protected void onDestroy() {
        dataBase.replace(mData);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        Log.d("destroy", "activity");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String event = "{\"Выбранная вкладка\":";
        if (id == R.id.nav_grid) {
            viewScreen.setCurrentItem(0);
            event += "\"Отображение сеткой\"}";
        } else if (id == R.id.nav_list) {
            viewScreen.setCurrentItem(1);
            event += "\"Отображение списком\"}";
        } else if (id == R.id.nav_setting) {
            createSettingLayout();
            event += "\"Отображение настроек\"}";
        }
        YandexMetrica.reportEvent("Шторка навигации", event);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initNavigationView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);
        final ImageView profileImage = navigationHeaderView.findViewById(R.id.navImageAvatar);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(v.getContext(), UserInfoActivity.class));
                YandexMetrica.reportEvent("Открыто окно информации о профиле");
            }
        });

        setDataFromUserInfo(navigationHeaderView);
    }

    //TODO использовать в потоке
    private void setDataFromUserInfo(View root) {
        final ImageView profileImage = root.findViewById(R.id.navImageAvatar);
        Bitmap avatar = DataImages.getAvatar(true, this);
        if (avatar != null) {
            profileImage.setImageBitmap(avatar);
        }
        SharedPreferences preferences = getSharedPreferences(getString(R.string.userInfoSP), MODE_PRIVATE);
        final TextView name = root.findViewById(R.id.navTextName);
        if (name != null) {
            name.setText(preferences.getString(getString(R.string.keyName), getString(R.string.myName)));
        } else {
            Log.d("NotFound", "name");
        }
        final TextView email = root.findViewById(R.id.navTextEmail);
        if (name != null) {
            email.setText(preferences.getString(getString(R.string.keyEmail), getString(R.string.email)));
        } else {
            Log.d("NotFound", "email");
        }
    }

    private void initReceiver() {
        receiver = new UpdateListAppReceiver(screenFragmentsAdapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
    }

    private void createSettingLayout() {
        startActivity(new Intent(this, SettingsActivity.class));
        YandexMetrica.reportEvent("Открыта страница настроек");
    }

    //TODO использовать в потоке
    private void initRecyclerView() {
        screenFragmentsAdapter = new ScreenFragmentsAdapter(getSupportFragmentManager(), mData, this);
        viewScreen = findViewById(R.id.screens);
        viewScreen.setAdapter(screenFragmentsAdapter);
        viewScreen.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                if (position == 0) {
                    navigationView.setCheckedItem(R.id.nav_grid);
                } else {
                    navigationView.setCheckedItem(R.id.nav_list);
                }
            }

            @Override
            public void onPageSelected(int position) {
                NavigationView navigationView = findViewById(R.id.nav_view);
                if (position == 0) {
                    navigationView.setCheckedItem(R.id.nav_grid);
                } else {
                    navigationView.setCheckedItem(R.id.nav_list);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void setData() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> data = getPackageManager().queryIntentActivities(mainIntent, 0);
        mData.clear();
        for (int i = 0; i < data.size(); ++i) {
            mData.add(new ItemLauncher(data.get(i), getPackageManager()));
        }
        HashMap<String, Integer> hashMap = dataBase.getFrequencies();
        for (int i = 0; i < mData.size(); ++i) {
            Integer temp = hashMap.get(mData.get(i).getPackageName());
            if (temp != null) {
                mData.get(i).setFrequency(temp);
            }
        }
        sortData();
    }

    public void sortData() {
        dataSetting.setClickSort(false);
        switch (dataSetting.getTypeSort()) {
            case "keySortNameAZ":
                sortDataAboutName(true);
                break;
            case "keySortNameZA":
                sortDataAboutName(false);
                break;
            case "keySortFrequency":
                sortDataAboutFrequency();
                break;
            case "keySortDateInstall":
                sortDataAboutDataInstall();
                break;
            default:
                break;
        }

    }

    private void sortDataAboutDataInstall() {
        Comparator<ItemLauncher> comparator = new Comparator<ItemLauncher>() {
            @Override
            public int compare(ItemLauncher o1, ItemLauncher o2) {
                Long item1 = o1.getFirstInstallTime();
                Long item2 = o2.getFirstInstallTime();
                return item2.compareTo(item1);
            }
        };
        Collections.sort(mData, comparator);
    }

    private void sortDataAboutFrequency() {
        Comparator<ItemLauncher> comparator = new Comparator<ItemLauncher>() {
            @Override
            public int compare(ItemLauncher o1, ItemLauncher o2) {
                Integer item1 = o1.getFrequency();
                Integer item2 = o2.getFrequency();
                return item2.compareTo(item1);
            }
        };
        Collections.sort(mData, comparator);
    }

    private void sortDataAboutName(final boolean flag) {
        Comparator<ItemLauncher> comparator = new Comparator<ItemLauncher>() {
            @Override
            public int compare(ItemLauncher o1, ItemLauncher o2) {
                if (flag) {
                    return o1.getNameApp().compareTo(o2.getNameApp());
                } else {
                    return o2.getNameApp().compareTo(o1.getNameApp());
                }
            }
        };
        Collections.sort(mData, comparator);
    }
}
