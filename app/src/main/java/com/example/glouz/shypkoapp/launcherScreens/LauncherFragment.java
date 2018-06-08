package com.example.glouz.shypkoapp.launcherScreens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.glouz.shypkoapp.R;
import com.example.glouz.shypkoapp.launcher.ItemLauncher;
import com.example.glouz.shypkoapp.launcher.LauncherAdapter;
import com.example.glouz.shypkoapp.launcher.OffsetItemDecoration;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class LauncherFragment extends Fragment {
    private boolean typeGrid, typeMaket;
    private ArrayList<ItemLauncher> mData;
    private LauncherAdapter launcherAdapter;
    private RecyclerView recyclerView;
    private Context context;

    public static LauncherFragment newInstance() {
        return new LauncherFragment();
    }

    public void setInfoAboutFragment(ArrayList<ItemLauncher> mData, boolean typeGrid, boolean typeMaket) {
        this.mData = mData;
        this.typeGrid = typeGrid;
        this.typeMaket = typeMaket;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.screen_apps, container, false);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.screenApps);
        recyclerView.setHasFixedSize(false);
        final int offset = getResources().getDimensionPixelSize(R.dimen.item_offset);
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));
        if (typeGrid) {
            createGridLayout();
        } else {
            createListLayout();
        }
        return view;
    }

    private void createListLayout() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        launcherAdapter = new LauncherAdapter(context, mData, false);
        recyclerView.setAdapter(launcherAdapter);
    }

    private void createGridLayout() {
        updateGridLayoutManager(typeMaket);
        launcherAdapter = new LauncherAdapter(context, mData, true);
        recyclerView.setAdapter(launcherAdapter);
    }

    public void updateGridLayoutManager(boolean newTypeMaket) {
        if (!typeGrid) {
            Log.d("Fragment", "не тот фрагмент для макета");
            return;
        }
        typeMaket = newTypeMaket;
        final int spanCount;
        try {
            if (!typeMaket) {
                spanCount = this.getResources().getInteger(R.integer.span_count);
            } else {
                spanCount = this.getResources().getInteger(R.integer.span_count_dense);
            }
            final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
            recyclerView.setLayoutManager(layoutManager);
        } catch (RuntimeException exeption) {
            Log.d("error", "not found content in fragment");
        }

    }

    public void addApp(int i) {
        launcherAdapter.notifyItemInserted(i);
    }

    public void removeApp(int i) {
        launcherAdapter.notifyItemRemoved(i);
        launcherAdapter.notifyItemRangeChanged(i, mData.size());
    }

    public void updateApps() {
        launcherAdapter.notifyDataSetChanged();
    }

}
