//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruit.widget.tab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.fruit.widget.R;

public class FruitTabHost extends TabHost implements OnPageChangeListener {
    private static final int APP_TAB_ELEVATION = 4;
    private final LayoutInflater inflater;
    private final TabWidget tabWidget;
    private final ShapeDrawable indicator;
    private final int indicatorHeight;
    private final int leftOffset;
    private final int colorControlActivated;
    private Type type;
    private OnTabChangeListener listener;
    private int maxTabWidth;
    private int scrollingState;
    private int position;
    private float positionOffset;
    private int textColor;
    private int textColorSelected;
    private int textColorRes;

    public FruitTabHost(Context context) {
        this(context, (AttributeSet)null);
    }

    public FruitTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.type = Type.FullScreenWidth;
        this.maxTabWidth = -2147483648;
        this.scrollingState = 0;
        this.position = 0;
        this.positionOffset = 0.0F;
        this.inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FruitTabHost, 0, 0);
        int indicatorColor = a.getColor(R.styleable.FruitTabHost_colorTab, getResources().getColor(R.color.default_tab_color));
        int bgActivedColor = a.getColor(R.styleable.FruitTabHost_colorBgActived, getResources().getColor(R.color.default_background_actived_color));
        int tabBgColor = a.getColor(R.styleable.FruitTabHost_colorTabBackground, getResources().getColor(R.color.default_tab_background_color));
        textColor = a.getColor(R.styleable.FruitTabHost_colorText, getResources().getColor(R.color.default_text_color));
        textColorSelected = a.getColor(R.styleable.FruitTabHost_colorTextSelected, getResources().getColor(R.color.default_text_color_selected));
//        TypedValue outValue = new TypedValue();
//        Theme theme = context.getTheme();
//        theme.resolveAttribute(attr.colorPrimary, outValue, true);
        this.setBackgroundColor(tabBgColor);
//        theme.resolveAttribute(attr.colorControlActivated, outValue, true);
        this.colorControlActivated = bgActivedColor;
        a.recycle();
        this.indicator = new ShapeDrawable();
        this.indicator.setColorFilter(indicatorColor, Mode.SRC_ATOP);
        Resources res = context.getResources();
        this.indicatorHeight = res.getDimensionPixelSize(R.dimen.mth_tab_indicator_height);
        this.leftOffset = res.getDimensionPixelSize(R.dimen.mth_tab_left_offset);
        int tabHeight = res.getDimensionPixelSize(R.dimen.mth_tab_height);
        this.tabWidget = new TabWidget(context);
        this.tabWidget.setLayoutParams(new LayoutParams(-1, tabHeight));
        this.tabWidget.setId(16908307);
        this.tabWidget.setStripEnabled(false);
        if(VERSION.SDK_INT >= 11) {
            this.tabWidget.setShowDividers(0);
        }

        this.addView(this.tabWidget);
        FrameLayout fl = new FrameLayout(context);
        fl.setLayoutParams(new LayoutParams(0, 0));
        fl.setId(16908305);
        this.addView(fl);
        this.setup();
        this.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if(FruitTabHost.this.listener != null) {
                    FruitTabHost.this.listener.onTabSelected(Integer.valueOf(tabId).intValue());
                }

            }
        });
        float density = this.getResources().getDisplayMetrics().density;
        ViewCompat.setElevation(this, 4.0F * density);
    }

    protected int getLayoutId(Type type) {
        switch(type.ordinal()) {
            case 1:
                return R.layout.mth_tab_widget_full;
            case 2:
                return R.layout.mth_tab_widget;
            case 3:
                return R.layout.mth_tab_widget;
            default:
                return R.layout.mth_tab_widget_full;
        }
    }

    public void addTab(CharSequence title) {
        int layoutId = this.getLayoutId(this.type);
        TextView tv = (TextView)this.inflater.inflate(layoutId, this.tabWidget, false);
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{}
        };
        tv.setTextColor(new ColorStateList(states, new int[]{textColorSelected, textColor}));
        tv.setText(title);
        if(VERSION.SDK_INT >= 21) {
            tv.setBackgroundResource(R.drawable.mth_tab_widget_background_ripple);
        } else {
            StateListDrawable tabId = new StateListDrawable();
            tabId.addState(new int[]{16842919}, new ColorDrawable(this.colorControlActivated));
            tabId.setAlpha(180);
            if(VERSION.SDK_INT >= 16) {
                tv.setBackground(tabId);
            } else {
                tv.setBackgroundDrawable(tabId);
            }
        }

        int tabId1 = this.tabWidget.getTabCount();
        this.addTab((TabSpec)this.newTabSpec(String.valueOf(tabId1)).setIndicator(tv).setContent(16908305));
    }

    public void addTab(View view) {
        if(VERSION.SDK_INT >= 21) {
            view.setBackgroundResource(R.drawable.mth_tab_widget_background_ripple);
        } else {
            StateListDrawable tabId = new StateListDrawable();
            tabId.addState(new int[]{16842919}, new ColorDrawable(this.colorControlActivated));
            tabId.setAlpha(180);
            if(VERSION.SDK_INT >= 16) {
                view.setBackground(tabId);
            } else {
                view.setBackgroundDrawable(tabId);
            }
        }

        int tabId1 = this.tabWidget.getTabCount();
        this.addTab((TabSpec)this.newTabSpec(String.valueOf(tabId1)).setIndicator(view).setContent(16908305));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(this.type == Type.Centered && this.maxTabWidth == -2147483648) {
            int i;
            View tabView;
            for(i = 0; i < this.tabWidget.getTabCount(); ++i) {
                tabView = this.tabWidget.getChildTabViewAt(i);
                if(tabView.getMeasuredWidth() > this.maxTabWidth) {
                    this.maxTabWidth = tabView.getMeasuredWidth();
                }
            }

            if(this.maxTabWidth > 0) {
                for(i = 0; i < this.tabWidget.getTabCount(); ++i) {
                    tabView = this.tabWidget.getChildTabViewAt(i);
                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams)tabView.getLayoutParams();
                    params.width = this.maxTabWidth;
                    tabView.setLayoutParams(params);
                }
            }
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(this.getChildCount() != 0) {
            ShapeDrawable d = this.indicator;
            View tabView = this.tabWidget.getChildTabViewAt(this.position);
            if(tabView != null) {
                View nextTabView = this.position + 1 < this.tabWidget.getTabCount()?this.tabWidget.getChildTabViewAt(this.position + 1):null;
                int tabWidth = tabView.getWidth();
                int nextTabWidth = nextTabView == null?tabWidth:nextTabView.getWidth();
                int indicatorWidth = (int)((float)nextTabWidth * this.positionOffset + (float)tabWidth * (1.0F - this.positionOffset));
                int indicatorLeft = (int)((float)(this.getPaddingLeft() + tabView.getLeft()) + this.positionOffset * (float)tabWidth);
                int height = this.getHeight();
                d.setBounds(indicatorLeft, height - this.indicatorHeight, indicatorLeft + indicatorWidth, height);
                d.draw(canvas);
            }
        }
    }

    public void setOnTabChangeListener(OnTabChangeListener l) {
        this.listener = l;
    }

    public void setType(Type type) {
        this.type = type;
        switch(type.ordinal()) {
            case 1:
                this.tabWidget.setGravity(3);
                this.setPadding(0, 0, 0, 0);
                break;
            case 2:
                this.tabWidget.setGravity(1);
                this.setPadding(0, 0, 0, 0);
                break;
            case 3:
                this.tabWidget.setGravity(3);
                this.setPadding(this.leftOffset, 0, 0, 0);
                break;
            default:
                this.tabWidget.setGravity(3);
                this.setPadding(0, 0, 0, 0);
        }

    }

    public void onPageSelected(int position) {
        if(this.scrollingState == 0) {
            this.updateIndicatorPosition(position, 0.0F);
        }

        this.setCurrentTab(position);
    }

    public void onPageScrollStateChanged(int state) {
        this.scrollingState = state;
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.updateIndicatorPosition(position, positionOffset);
    }

    private void updateIndicatorPosition(int position, float positionOffset) {
        this.position = position;
        this.positionOffset = positionOffset;
        this.invalidate();
    }

    public interface OnTabChangeListener {
        void onTabSelected(int var1);
    }

    public static enum Type {
        FullScreenWidth,
        Centered,
        LeftOffset;

        private Type() {
        }
    }
}
