package com.example.glouz.shypkoapp.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glouz.shypkoapp.R;
import com.example.glouz.shypkoapp.data.DataSetting;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;

public class LauncherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ItemLauncher> mData;
    private boolean typeItem;
    private PackageManager packageManager;

    public LauncherAdapter(Context contextVeiw, ArrayList<ItemLauncher> data, boolean type) {
        packageManager = contextVeiw.getPackageManager();
        typeItem = type;
        mData = data;
    }

    private void bindGridView(@NonNull final Holder.GridHolder gridHolder, final int position) {
        final View itemView = gridHolder.getView();
        final View image = gridHolder.getImageView();
        final TextView text = gridHolder.getTextView();
        final ItemLauncher itemLauncher = mData.get(position);

        image.setBackground(itemLauncher.getIcon());
        text.setText(String.valueOf(itemLauncher.getNameApp()));

        setMenuItem(itemView, itemLauncher);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemLauncher.addFrequency();
                YandexMetrica.reportEvent("Запустили приложение");
                Intent launchIntent = packageManager.getLaunchIntentForPackage(itemLauncher.getPackageName());
                v.getContext().startActivity(launchIntent);
            }
        });
    }

    private void setMenuItem(final View itemView, final ItemLauncher itemLauncher) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), itemView);
                popupMenu.inflate(R.menu.launcher_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String eventMenuItemClick = "{\"Нажатая кнопка\":";
                        switch (item.getItemId()) {
                            case R.id.delete:
                                Intent intent1 = new Intent(Intent.ACTION_DELETE);
                                intent1.setData(Uri.parse("package:" + itemLauncher.getPackageName()));
                                itemView.getContext().startActivity(intent1);
                                eventMenuItemClick += "\"Удаление приложения\"}";
                                break;
                            case R.id.frequency:
                                Toast toast = Toast.makeText(itemView.getContext(),
                                        itemView.getContext().getString(R.string.frequency) +
                                                ": " + itemLauncher.getFrequency(), Toast.LENGTH_SHORT);
                                toast.show();
                                eventMenuItemClick += "\"Показана частота использования\"}";
                                break;
                            case R.id.info:
                                Intent intent2 = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent2.setData(Uri.parse("package:" + itemLauncher.getPackageName()));
                                itemView.getContext().startActivity(intent2);
                                eventMenuItemClick += "\"Показана информация о приложении\"}";
                                break;
                            default:
                                eventMenuItemClick += "\"Произошло что-то крайне странее\"}";
                                break;
                        }
                        YandexMetrica.reportEvent("Использовано меню приложения", eventMenuItemClick);
                        return true;
                    }
                });
                return true;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view;
        if (typeItem) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false);
        }
        return new Holder.GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        bindGridView((Holder.GridHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }
}
