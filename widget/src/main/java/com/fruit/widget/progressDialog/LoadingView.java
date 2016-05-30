package com.fruit.widget.progressDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fruit.widget.R;

/**
 * Created by user on 2015/12/16.
 */
public class LoadingView extends ProgressBar {

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FrameLayout layout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_loading, null, false);
        ImageView progress = (ImageView)layout.findViewById(R.id.progress);
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        progress.startAnimation(animation);
    }
}
