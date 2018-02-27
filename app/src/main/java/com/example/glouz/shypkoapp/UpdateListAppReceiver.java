package com.example.glouz.shypkoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.glouz.shypkoapp.launcher.LauncherAdapter;

public class UpdateListAppReceiver extends BroadcastReceiver {
    LauncherAdapter adapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("reciever", intent.getAction());
        if (adapter == null) {
            return;
        }
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            adapter.installApp(intent.getData().getSchemeSpecificPart());
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            adapter.removeApp(intent.getData().getSchemeSpecificPart());
        }
    }

    public UpdateListAppReceiver() {
        super();
    }

    public UpdateListAppReceiver(LauncherAdapter launcherAdapter) {
        super();
        adapter = launcherAdapter;
    }
}
