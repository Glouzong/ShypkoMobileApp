package com.example.glouz.shypkoapp.launcher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.glouz.shypkoapp.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResolveInfo> mData;

    private PackageManager packageManager;

    private boolean typeItem;

    public void updateDate() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mData = packageManager.queryIntentActivities(mainIntent, 0);
        sortData();
        notifyDataSetChanged();
    }

    public LauncherAdapter(PackageManager manager, boolean type) {
        packageManager = manager;
        typeItem = type;
        updateDate();
    }

    private void sortData() {
        sortDataAboutName();
    }

    private void sortDataAboutName() {
        Comparator<ResolveInfo> comparator = new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                return o1.activityInfo.packageName.compareTo(o2.activityInfo.packageName);
            }
        };
        Collections.sort(mData, comparator);
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

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final View view = gridHolder.getView();
        final View image = gridHolder.getImageView();
        final TextView text = gridHolder.getTextView();
        final ResolveInfo info = this.mData.get(position);
        Drawable icon = info.loadIcon(packageManager);
        image.setBackground(icon);
        text.setText(String.valueOf(info.loadLabel(packageManager)));
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
                                intent1.setData(Uri.parse("package:" + mData.get(position).activityInfo.packageName));
                                view.getContext().startActivity(intent1);
                                return true;
                            case R.id.frequency:
                                Toast toast = Toast.makeText(view.getContext(),
                                        view.getContext().getString(R.string.frequency) + ":" + 1, Toast.LENGTH_SHORT);//TODO
                                toast.show();
                                return true;
                            case R.id.info:
                                Intent intent2 = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent2.setData(Uri.parse("package:" + mData.get(position).activityInfo.packageName));
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
                ActivityInfo activity = info.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                Intent intent = new Intent(Intent.ACTION_MAIN);

                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intent.setComponent(name);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
