package com.fruit.widget.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.fruit.widget.R;


/**
 * Created by liangchen on 15/3/27.
 */
public class LabelIndicatorStrategy {
    private Context mContext;
    private int mIndicatorView;
    private final CharSequence mLabel;

    public LabelIndicatorStrategy(Context paramContext, CharSequence paramCharSequence)
    {
        this.mContext = paramContext;
        this.mLabel = paramCharSequence;
    }

    public LabelIndicatorStrategy(Context paramContext, CharSequence paramCharSequence, int paramInt)
    {
        this(paramContext, paramCharSequence);
        this.mIndicatorView = paramInt;
    }

    public View createIndicatorView(TabHost paramTabHost)
    {
        if (this.mIndicatorView == 0)
            this.mIndicatorView = R.layout.tab_indicator_holo;
        View localView = ((LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(this.mIndicatorView, paramTabHost.getTabWidget(), false);
        TextView tx = ((TextView)localView.findViewById(R.id.title));
        tx.setText(this.mLabel);
        return localView;
    }
}
