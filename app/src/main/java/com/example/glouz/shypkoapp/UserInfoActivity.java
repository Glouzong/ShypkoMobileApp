package com.example.glouz.shypkoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yandex.metrica.YandexMetrica;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataSetting settings = new DataSetting(this);
        if (settings.checkTheme()) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void callPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getString(R.string.number_phone)));
        startActivity(intent);
        YandexMetrica.reportEvent("Открыто окно звонка");
    }

    public void openEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+getString(R.string.email)));
        startActivity(intent);
        YandexMetrica.reportEvent("Открыто окно почты");
    }

    public void openGithub(View view) {
        Uri address = Uri.parse(getString(R.string.linkGithub));
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
        YandexMetrica.reportEvent("Открыта страничка github'а");
    }
}
