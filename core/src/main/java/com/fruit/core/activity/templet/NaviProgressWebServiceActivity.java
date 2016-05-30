package com.fruit.core.activity.templet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fruit.core.R;
import com.fruit.core.activity.FruitWebServiceActivity;
import com.fruit.widget.navi.TopBarNormal;

/**
 * Created by liangchen on 15/4/27.
 * 此导航activity设计，是导航栏左右两边是一个imageview，中间是titleview组成，内容容器是MultiStateView来管理4个状态
 * 分别是1：加载中，2：加载数据空，3：加载失败，4：有加载内容这四个状态
 */
public abstract class NaviProgressWebServiceActivity extends FruitWebServiceActivity implements TopBarNormal.TopbarClickListener {

    TopBarNormal topbar;
    FrameLayout flContent;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_base_navi_activity);
        inflater = LayoutInflater.from(this);
        topbar = (TopBarNormal) findViewById(R.id.topbar);
        flContent = (FrameLayout) findViewById(R.id.flContent);

        addSubContentView();//添加内容view，需要重写getLayoutId方法
        topbar.setOnTopbarClickListener(this);
    }

    /*############################导航栏相关设置start############################------设置title*/


    public void setTitleTextSize(float size){
        topbar.setTitleTextSize(size);
    }

    public void setActivityTitle(String title) {
        topbar.setTitle(title);
    }

    /*导航栏相关设置------设置右图标*/
    public void setRightImage(Drawable drawable) {
        topbar.setRightImage(drawable);
    }

    /*导航栏相关设置------设置右文字*/
    public void setRightText(String text) {
        topbar.setRightText(text);
    }

    /*处理导航左边的图标*/
    @Override
    public void leftClick() {
        finish();
    }

    /*处理导航右边的图标*/
    @Override
    public void rightClick() {

    }
    /*##################################导航相关设置end############################*/

    /*##################################内容布局相关start############################*/
    protected abstract int getLayoutId();

    /**
     * 设置布局和标题
     */
    public void addSubContentView(View layoutContent) {
        flContent.addView(layoutContent, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置布局和标题
     */
    public void addSubContentView() {
        View contentView = inflater.inflate(getLayoutId(), null);
        addSubContentView(contentView);
    }
    /*##################################内容布局相关end############################*/


    /*################################异步加载get方法的相关回调处理start##############*/
    @Override
    public void DealSuccessResult(Object returnObject, int taskid) {

    }
    /*##################################异步加载get方法的相关回调处理end#########################*/



}
