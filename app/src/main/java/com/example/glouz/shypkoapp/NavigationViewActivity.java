package com.example.glouz.shypkoapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
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

import com.example.glouz.shypkoapp.database.DataBase;
import com.example.glouz.shypkoapp.launcher.ItemLauncher;
import com.example.glouz.shypkoapp.launcher.LauncherAdapter;
import com.example.glouz.shypkoapp.launcher.OffsetItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavigationViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        dataBase = new DataBase(this);
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        lastFlagTheme = settings.checkTheme();
        super.onCreate(savedInstanceState);

        setDate();  //TODO в отделльный поток

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
        final View profileImage = navigationHeaderView.findViewById(R.id.imageView);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivity(new Intent(v.getContext(), com.example.glouz.shypkoapp.ScrollingActivity.class));
            }
        });

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
        initReceiver();
    }

    private void initReceiver() {
        receiver = new UpdateListAppReceiver(launcherAdapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);
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
    protected void onPause() {
        Log.d("TEST", "PAUSE");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("TEST", "DESTROY");
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

        if (id == R.id.nav_grid) {
            createGridLayout();
        } else if (id == R.id.nav_list) {
            createListLayout();

        } else if (id == R.id.nav_setting) {
            createSettingLayout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createSettingLayout() {
        startActivity(new Intent(this, com.example.glouz.shypkoapp.SettingsActivity.class));
    }

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
