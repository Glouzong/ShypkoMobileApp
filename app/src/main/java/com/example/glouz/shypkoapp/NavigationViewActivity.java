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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.glouz.shypkoapp.launcher.LauncherAdapter;
import com.example.glouz.shypkoapp.launcher.OffsetItemDecoration;
import com.example.glouz.shypkoapp.settings.SettingsActivity;
import com.example.glouz.shypkoapp.userInfo.UserInfoActivity;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavigationViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DataSetting settings;
    private boolean lastFlagMaket = false, lastFlagTheme = false;
    private String typeSort;
    private LauncherAdapter launcherAdapter;
    private ArrayList<ItemLauncher> mData;
    private UpdateListAppReceiver receiver;
    private DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = new DataSetting(this);
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        lastFlagTheme = settings.checkTheme();
        super.onCreate(savedInstanceState);
        dataBase = new DataBase(this);
        initNavigationView();
        initRecyclerView();
        initReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settings.checkTheme() != lastFlagTheme) {
            this.recreate();
        } else if ((settings.checkMaket() != lastFlagMaket) && (settings.checkLayout())) {
            createGridLayout();
        } else if (!typeSort.equals(settings.getTypeSort())) {
            launcherAdapter.sortData();
            typeSort = settings.getTypeSort();
        }
    }

    @Override
    protected void onDestroy() {
        dataBase.replace(mData);
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String event = "{\"Выбранная вкладка\":";
        if (id == R.id.nav_grid) {
            createGridLayout();
            event += "\"Отображение сеткой\"}";
        } else if (id == R.id.nav_list) {
            createListLayout();
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
        setContentView(R.layout.activity_navigation_view);
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
        receiver = new UpdateListAppReceiver(launcherAdapter);
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
    public void setDate() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> data = getPackageManager().queryIntentActivities(mainIntent, 0);
        mData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            mData.add(new ItemLauncher(data.get(i), getPackageManager()));
        }

        HashMap<String, Integer> hashMap = dataBase.getFrequencies();
        for (int i = 0; i < mData.size(); i++) {
            Integer temp = hashMap.get(mData.get(i).getPackageName());
            if (temp != null) {
                mData.get(i).setFrequency(temp);
            }
        }
    }

    private void initRecyclerView() {//TODO переделать

        setDate();
        recyclerView = findViewById(R.id.louncher_content);
        recyclerView.setHasFixedSize(false);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
        typeSort = settings.getTypeSort();
        if (settings.checkLayout()) {
            createGridLayout();
        } else {
            createListLayout();
        }
    }

    private void createGridLayout() {
        final int spanCount;
        lastFlagMaket = settings.checkMaket();
        if (!settings.checkMaket()) {
            spanCount = getResources().getInteger(R.integer.span_count);
        } else {
            spanCount = getResources().getInteger(R.integer.span_count_dense);
        }
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        launcherAdapter = new LauncherAdapter(this, mData, true);
        recyclerView.setAdapter(launcherAdapter);
        settings.setTypeLayout(true);
    }

    private void createListLayout() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        launcherAdapter = new LauncherAdapter(this, mData, false);
        recyclerView.setAdapter(launcherAdapter);
        settings.setTypeLayout(false);
    }
}
