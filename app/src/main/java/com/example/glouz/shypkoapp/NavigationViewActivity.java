package com.example.glouz.shypkoapp;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.glouz.shypkoapp.launcher.LauncherAdapter;
import com.example.glouz.shypkoapp.launcher.OffsetItemDecoration;

import java.util.List;

public class NavigationViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DataSetting settings;
    boolean lastFlagMaket = false, lastFlagTheme = false;
    LauncherAdapter launcherAdapter;

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

        if (settings.checkLayout()) {
            createGridLayout();
        } else {
            createListLayout();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (settings.checkTheme() != lastFlagTheme) {
            this.recreate();
        } else if ((settings.checkMaket() != lastFlagMaket) && (settings.checkLayout())) {
            createGridLayout();
        }
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
        launcherAdapter = new LauncherAdapter(getPackageManager(), true);
        recyclerView.setAdapter(launcherAdapter);
        settings.setTypeLayout(true);
    }

    private void createListLayout() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        launcherAdapter = new LauncherAdapter(getPackageManager(), false);
        recyclerView.setAdapter(launcherAdapter);
        settings.setTypeLayout(false);
    }
}
