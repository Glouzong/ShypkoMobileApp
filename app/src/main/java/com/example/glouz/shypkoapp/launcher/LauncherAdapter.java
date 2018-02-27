package com.example.glouz.shypkoapp.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glouz.shypkoapp.DataSetting;
import com.example.glouz.shypkoapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ItemLauncher> mData;
    private boolean typeItem;
    private PackageManager packageManager;
    private DataSetting settings;

    public LauncherAdapter(Context contextVeiw, ArrayList<ItemLauncher> data, boolean type) {
        packageManager = contextVeiw.getPackageManager();
        typeItem = type;
        settings = new DataSetting(contextVeiw);
        updateDate(data);
    }

    public void updateDate(ArrayList<ItemLauncher> data) {
        mData = data;
        sortData();
    }

    public void sortData() {
        switch (settings.getTypeSort()) {
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
        notifyDataSetChanged();
    }

    private void sortDataAboutDataInstall() {
        Comparator<ItemLauncher> comparator = new Comparator<ItemLauncher>() {
            @Override
            public int compare(ItemLauncher o1, ItemLauncher o2) {
                return Long.compare(o2.getFirstInstallTime(), o1.getFirstInstallTime());
            }
        };
        Collections.sort(mData, comparator);
    }

    private void sortDataAboutFrequency() {
        Comparator<ItemLauncher> comparator = new Comparator<ItemLauncher>() {
            @Override
            public int compare(ItemLauncher o1, ItemLauncher o2) {
                return Integer.compare(o2.getFrequency(), o1.getFrequency());
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

    public void removeApp(String nameApp) {
        for (int i = 0; i < mData.size(); ++i) {
            if (nameApp.equals(mData.get(i).getPackageName())) {
                Log.d("remove", nameApp + " " + mData.get(i).getPackageName());
                mData.remove(i);
                notifyItemRemoved(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void installApp(String nameApp) {
        Log.d("install", nameApp);
        Intent intent = new Intent();
        intent.setPackage(nameApp);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ItemLauncher result = new ItemLauncher(packageManager.resolveActivity(intent, 0), packageManager);
        mData.add(result);
        notifyItemInserted(mData.size() - 1);
        notifyDataSetChanged();
    }

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final View view = gridHolder.getView();
        final View image = gridHolder.getImageView();
        final TextView text = gridHolder.getTextView();
        final ItemLauncher itemLauncher = mData.get(position);
        image.setBackground(itemLauncher.getIcon());
        text.setText(String.valueOf(itemLauncher.getNameApp()));
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), view);
                popupMenu.inflate(R.menu.launcher_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Intent intent1 = new Intent(Intent.ACTION_DELETE);
                                intent1.setData(Uri.parse("package:" + mData.get(position).getPackageName()));
                                view.getContext().startActivity(intent1);
                                return true;
                            case R.id.frequency:
                                Toast toast = Toast.makeText(view.getContext(),
                                        view.getContext().getString(R.string.frequency) + ": " + itemLauncher.getFrequency(), Toast.LENGTH_SHORT);
                                toast.show();
                                return true;
                            case R.id.info:
                                Intent intent2 = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent2.setData(Uri.parse("package:" + mData.get(position).getPackageName()));
                                view.getContext().startActivity(intent2);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemLauncher.addFrequency();
                Intent launchIntent = packageManager.getLaunchIntentForPackage(itemLauncher.getPackageName());
                v.getContext().startActivity(launchIntent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view;
        if (typeItem) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false);
        }
        return new Holder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        bindGridView((Holder.GridHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }
}
