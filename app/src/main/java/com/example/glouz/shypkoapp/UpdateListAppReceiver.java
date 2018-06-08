package com.example.glouz.shypkoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.glouz.shypkoapp.launcherScreens.ScreenFragmentsAdapter;

public class UpdateListAppReceiver extends BroadcastReceiver {
    ScreenFragmentsAdapter adapter;

    @Override
    public void onReceive(Context context, Intent intent) {
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

    public UpdateListAppReceiver(ScreenFragmentsAdapter screenFragmentsAdapter) {
        super();
        adapter = screenFragmentsAdapter;
    }
}
