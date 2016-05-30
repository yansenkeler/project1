package com.fruit.widget.navi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fruit.widget.R;


/**
 * Created by liangchen on 15/3/29.
 * 左右按钮是图片的topbar
 */
public class TopBarNormal extends RelativeLayout {

    private ImageView leftImageView,rightImageView;
    private TextView titleTextView;
    private TextView rightText, leftText;
    private RelativeLayout mLayout;

    private Drawable leftimage;

    private Drawable rightimage;

    private String title;
    private int titleTextColor;

    private int barBackground;

    private TopbarClickListener topbarClickListener;

    public interface TopbarClickListener{
        abstract void leftClick();
        abstract void rightClick();
        abstract void leftTextClick();
        abstract void rightTextClick();
    }

    public void setOnTopbarClickListener(TopbarClickListener listener){
        this.topbarClickListener = listener;
    }

    private LayoutParams leftParams , rightParams , titleParams;
    public TopBarNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopbarWithImage);

        leftimage = ta.getDrawable(R.styleable.TopbarWithImage_leftImage);
        rightimage = ta.getDrawable(R.styleable.TopbarWithImage_rightImage);

        title = ta.getString(R.styleable.TopbarWithImage_topText);
        titleTextColor = ta.getColor(R.styleable.TopbarWithImage_topTextColor, 0);

        barBackground = ta.getColor(R.styleable.TopbarWithImage_topbarBackground,0);

        ta.recycle();

        View.inflate(context, R.layout.topbarwithimage, this);

        leftImageView = (ImageView)this.findViewById(R.id.leftbutton);
        rightImageView =(ImageView)this.findViewById(R.id.rightbutton);
        titleTextView = (TextView)this.findViewById(R.id.title);
        rightText = (TextView)this.findViewById(R.id.rb);
        leftText = (TextView)this.findViewById(R.id.lt);
        mLayout = (RelativeLayout)this.findViewById(R.id.topbar);
        rightText.setTextColor(titleTextColor);
        leftText.setTextColor(titleTextColor);
        leftImageView.setImageDrawable(leftimage);

        rightImageView.setImageDrawable(rightimage);

        titleTextView.setText(title);
        titleTextView.setTextColor(titleTextColor);

        setBackgroundColor(barBackground);

        leftImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topbarClickListener != null) {
                    topbarClickListener.leftClick();
                }
            }
        });

        rightImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topbarClickListener!=null) {
                    topbarClickListener.rightClick();
                }
            }
        });

        rightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topbarClickListener!=null) {
                    topbarClickListener.rightTextClick();
                }
            }
        });

        leftText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topbarClickListener!=null){
                    topbarClickListener.leftTextClick();
                }
            }
        });
    }

    //设置title
    public void setTitleTextSize(float size){
        titleTextView.setTextSize(size);
    }

    //设置右边图片
    public void setRightImage(Drawable drawable){
        rightImageView.setImageDrawable(drawable);
        rightImageView.setVisibility(VISIBLE);
    }

    //设置左边图片
    public void setLeftImage(Drawable drawable){
        leftImageView.setImageDrawable(drawable);
        leftImageView.setVisibility(VISIBLE);
    }

    public void setRightText(String text){
        rightText.setText(text);
        rightText.setVisibility(VISIBLE);
    }

    public void setLeftText(String text){
        leftText.setText(text);
        leftText.setVisibility(VISIBLE);
    }

    public void setTitle(String text){
        titleTextView.setText(text);
    }

    //设置背景颜色
    public void setBarBackground(int color){
        mLayout.setBackgroundColor(color);
    }

    public void setTopBarTextColor(int color)
    {
        leftText.setTextColor(color);
        rightText.setTextColor(color);
        titleTextView.setTextColor(color);
    }
}
