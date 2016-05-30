package com.fruit.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fruit.widget.R;

/**
 * Created by user on 2015/12/28.
 */
public class ScrollView extends View{
    private static final int radius = 100;
    //可滑动区域宽度
    private int scrollWidth;
    //可滑动区域高度
    private int scrollHeight;
    //初始圆心的x坐标
    private int startX;
    //初始圆心的y坐标
    private int startY;
    //x方向偏移量
    private int offsetX;
    //y方向偏移量
    private int offsetY;
    //上一次回调x坐标
    private int lastX;
    //上一次回调y坐标
    private int lastY;
    //view与viewParent左，上，右，下的距离
    private int left, top, right, bottom;
    //view的宽度
    private int viewWidth;
    //view的高度
    private int viewHeight;
    //触摸时是否在view的区域内
    private boolean isArea = false;

    private int viewColor;
    private Paint mPaint;

    public ScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollView, 0, 0);
        try {
            viewColor = ta.getColor(R.styleable.ScrollView_viewColor, getResources().getColor(android.R.color.holo_blue_light));
        }finally {
            ta.recycle();
        }

    }

    public ScrollView(Context context) {
        super(context, null);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollView, 0, 0);
        try {
            viewColor = ta.getColor(R.styleable.ScrollView_viewColor, getResources().getColor(android.R.color.holo_green_light));
        }finally {
            ta.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        scrollWidth = widthSize;
        scrollHeight = heightSize;
        setMeasuredDimension(measureSize(widthMode, widthSize), measureSize(heightMode, heightSize));
    }

    private int measureSize(int mode, int sizeSpec){
        int resultSize = 0;
        if (mode == MeasureSpec.EXACTLY){
            resultSize = sizeSpec;
        }else if(mode == MeasureSpec.AT_MOST){
            resultSize = Math.min(sizeSpec, radius*2);
        }else if (mode == MeasureSpec.UNSPECIFIED){
            resultSize = radius*2;
        }
        return resultSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startX = getLeft()+radius;
        startY = getTop()+radius;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(viewColor);
        canvas.drawCircle(radius, radius, radius, mPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX()+getLeft();
        int y = (int) event.getY()+getTop();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("onTouchEvent", "ACTION_DOWN:"+x+";"+y);
                int dis = (int) Math.sqrt((x-startX)*(x-startX)+(y-startY)*(y-startY));
                if (dis>radius){
                    isArea = false;
                }else {
                    isArea = true;
                }
                if (isArea){
                    lastX = x;
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("onTouchEvent", "ACTION_MOVE:"+x+";"+y);
                offsetX = x - lastX;
                offsetY = y - lastY;
                if (isArea){
                    if (getLeft()+offsetX<0){
                        left = 0;
                    }else if (getLeft()+offsetX+radius*2>scrollWidth){
                        left = scrollWidth-radius*2;
                    }else {
                        left = getLeft()+offsetX;
                    }
                    if (getTop()+offsetY<0){
                        top = 0;
                    }else if (getTop()+offsetY+radius*2>scrollHeight){
                        top = scrollHeight-radius*2;
                    }else {
                        top = getTop()+offsetY;
                    }
                    layout(left, top, left+radius*2, top+radius*2);
                    lastX = x;
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                startX = getLeft()+radius;
                startY = getTop()+radius;
                break;
            default:
                break;
        }
        return true;
    }
}
