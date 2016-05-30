package com.fruit.widget.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by user on 2016/3/11.
 */
public class CustomScrollView extends ScrollView {
    private boolean scrollable = true;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollable){
            return super.onTouchEvent(ev);
        }else {
            return false;
        }
    }

    public void setScrollable(boolean mScrollable){
        scrollable = mScrollable;
    }

    public boolean isScrollable() {
        return scrollable;
    }
}
