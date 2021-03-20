package com.example.sellit.common.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.sellit.R;


public class FullScreenProgressBar extends FrameLayout {
    public FullScreenProgressBar(@NonNull Context context) {
        super(context);
        init();
    }

    public FullScreenProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullScreenProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        View progressBar = new ProgressBar(getContext());
        this.addView(progressBar);

        LayoutParams params = (LayoutParams) progressBar.getLayoutParams();

        params.gravity = Gravity.CENTER;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        progressBar.setLayoutParams(params);
        this.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorOverlay, getContext().getTheme()));
    }
}
