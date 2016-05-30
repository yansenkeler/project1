package com.fruit.widget.navi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fruit.widget.R;


/**
 * Created by liangchen on 15/3/29.
 */
public class TopBar extends RelativeLayout {

    private Button leftButton,rightButton;
    private TextView titleTextView;

    private String leftText;
    private Drawable leftBackground;
    private int leftTextColor;

    private String rightText;
    private Drawable rightBackground;
    private int rightTextColor;

    private String title;
    private float titleTextSize;
    private int titleTextColor;

    private int barBackground;

    private TopbarClickListener topbarClickListener;

    public interface TopbarClickListener{
        public void leftClick();
        public void rightClick();
    }

    public void setOnTopbarClickListener(TopbarClickListener listener){
        this.topbarClickListener = listener;
    }

    private LayoutParams leftParams , rightParams , titleParams;
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar);


        leftText = ta.getString(R.styleable.Topbar_leftText);
        leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
        leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor,0);

        rightText = ta.getString(R.styleable.Topbar_rightText);
        rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
        rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor,0);

        title = ta.getString(R.styleable.Topbar_titleText);
        titleTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize,0);
        titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor1,0);

        barBackground = ta.getColor(R.styleable.Topbar_barBackground,0);

        ta.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        titleTextView = new TextView(context);

        leftButton.setText(leftText);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);

        rightButton.setText(rightText);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);

        titleTextView.setText(title);
        titleTextView.setTextColor(titleTextColor);
        titleTextView.setTextSize(titleTextSize);
        titleTextView.setGravity(Gravity.CENTER);

        setBackgroundColor(barBackground);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);

        addView(leftButton,leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);

        addView(rightButton,rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);

        addView(titleTextView,titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topbarClickListener!=null) {
                    topbarClickListener.leftClick();
                }
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topbarClickListener!=null) {
                    topbarClickListener.rightClick();
                }
            }
        });
    }
}
