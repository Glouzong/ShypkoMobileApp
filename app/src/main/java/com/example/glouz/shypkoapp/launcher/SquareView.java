package com.example.glouz.shypkoapp.launcher;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class SquareView extends LinearLayout {
    public SquareView(final Context context) {
        super(context);
    }

    public SquareView(final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
