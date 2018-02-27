package com.example.glouz.shypkoapp.launcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.glouz.shypkoapp.R;

class Holder {

    static class GridHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final View imageView;
        private final TextView textView;

        GridHolder(final View view) {
            super(view);
            mView = view.findViewById(R.id.launcher);
            imageView = mView.findViewById(R.id.launcher_image);
            textView = mView.findViewById(R.id.launcher_name);
        }

        View getImageView() {
            return imageView;
        }

        View getView() {
            return mView;
        }

        TextView getTextView() {
            return textView;
        }
    }
}
