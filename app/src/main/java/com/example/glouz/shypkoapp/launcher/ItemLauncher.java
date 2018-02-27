package com.example.glouz.shypkoapp.launcher;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

public class ItemLauncher {
    private String nameApp, packageName;
    private Drawable icon;
    private int frequency = 0;
    private long firstInstallTime = 0;

    public ItemLauncher(ResolveInfo resolveInfo, PackageManager packageManager) {
        packageName = resolveInfo.activityInfo.packageName;
        icon = resolveInfo.loadIcon(packageManager);
        nameApp = String.valueOf(resolveInfo.loadLabel(packageManager));
        try {
            firstInstallTime = packageManager.getPackageInfo(packageName, 0).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void addFrequency() {
        this.frequency += 1;
    }

    public long getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(long firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }
}
