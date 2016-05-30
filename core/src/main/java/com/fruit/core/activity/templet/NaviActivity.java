package com.fruit.core.activity.templet;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.VolleyError;
import com.fruit.core.R;
import com.fruit.core.activity.FruitActivity;
import com.fruit.widget.MultiStateView;
import com.fruit.widget.navi.TopBarNormal;

import org.simple.eventbus.EventBus;

import tr.xip.errorview.ErrorView;

/**
 * Created by liangchen on 15/4/27.
 * 此导航activity设计，是导航栏左右两边是一个imageview，中间是titleview组成，内容容器是MultiStateView来管理4个状态
 * 分别是1：加载中，2：加载数据空，3：加载失败，4：有加载内容这四个状态
 * EventBus注解
 */
public abstract class NaviActivity extends FruitActivity implements TopBarNormal.TopbarClickListener {

    TopBarNormal topbar;
    FrameLayout flContent;
    MultiStateView multistateview;
    private LayoutInflater inflater;
    boolean useMutiStateView;//刷新时是否使用多状态view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_base_navi_activity);

        inflater = LayoutInflater.from(this);
        topbar = (TopBarNormal) findViewById(R.id.topbar);
        flContent = (FrameLayout) findViewById(R.id.flContent);
        multistateview = (MultiStateView) findViewById(R.id.multistateview);

        setUseMutiStateView(true);//默认使用多状态view
        //设置多状态view的布局文件start
        if (getEmptyViewLayout() == 0) {
            multistateview.setViewForState(R.layout.navi_default_layout_empty, MultiStateView.ViewState.EMPTY);
        } else {
            multistateview.setViewForState(getEmptyViewLayout(), MultiStateView.ViewState.EMPTY);
        }
        if (getLoadingViewLayout() == 0) {
            multistateview.setViewForState(R.layout.navi_default_layout_loading, MultiStateView.ViewState.LOADING);
        } else {
            multistateview.setViewForState(getLoadingViewLayout(), MultiStateView.ViewState.LOADING);
        }

        if (getErrorViewLayout() == 0) {
            multistateview.setViewForState(R.layout.navi_default_layout_error, MultiStateView.ViewState.ERROR);
            ((ErrorView)getErrorView().findViewById(R.id.error_view)).setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    Retry();
                }
            });
        } else {
            multistateview.setViewForState(getErrorViewLayout(), MultiStateView.ViewState.ERROR);
        }
        //设置多状态view的布局文件end

        addSubContentView();//添加内容view，需要重写getLayoutId方法
        topbar.setOnTopbarClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /*无数据的时候添加一个状态*/
    public void setEmptyState(int layout) {
        multistateview.setViewForState(layout, MultiStateView.ViewState.EMPTY);
        multistateview.setViewState(MultiStateView.ViewState.EMPTY);
    }

    /*############################导航栏相关设置start############################------设置title*/

    public void setActivityTitle(String title) {
        topbar.setTitle(title);
    }

    /*导航栏相关设置------设置右图标*/
    public void setRightImage(Drawable drawable) {
        topbar.setRightImage(drawable);
    }

    public void setLeftImageView(Drawable drawable){
        topbar.setLeftImage(drawable);
    }

    /*导航栏相关设置------设置右文字*/
    public void setRightText(String text) {
        topbar.setRightText(text);
    }

    public void setLeftText(String text){
        topbar.setLeftText(text);
    }

    /**
     * 设置导航栏背景
     * @param color
     */
    public void setTopbarBackground(int color){
        topbar.setBarBackground(color);
    }

    //导航栏图片文字点击调用方法
    @Override
    public void leftClick() {}
    @Override
    public void rightClick() {}
    @Override
    public void leftTextClick() {}
    @Override
    public void rightTextClick() {}
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

    /*##################################状态view相关设置start############################*/
    /*可复用此方法设置自定义的emptyview*/
    public int getEmptyViewLayout() {
        return 0;
    }

    /*可复用此方法设置自定义的loadingview*/
    public int getLoadingViewLayout() {
        return 0;
    }

    /*可复用此方法设置自定义的errorview*/
    public int getErrorViewLayout() {
        return 0;
    }

    public boolean isUseMutiStateView() {
        return useMutiStateView;
    }

    public void setUseMutiStateView(boolean useMutiStateView) {
        this.useMutiStateView = useMutiStateView;
    }

    /*设置多状态view的一个状态*/
    public void setViewState(MultiStateView.ViewState state) {
        multistateview.setViewState(state);
    }

    public View getErrorView(){
        return multistateview.getView(MultiStateView.ViewState.ERROR);
    }

    public View getEmptyView(){
        return multistateview.getView(MultiStateView.ViewState.EMPTY);
    }

    /**
     * 如果加载默认的ErrorView，注册点击监听器调用Retry方法
     */
    public void Retry(){}
    /*##################################状态view相关设置end############################*/


    /*################################异步加载get方法的相关回调处理start##############*/

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        if (isUseMutiStateView()) {
            multistateview.setViewState(MultiStateView.ViewState.CONTENT);
        }
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        if(isUseMutiStateView()){
            multistateview.setViewState(MultiStateView.ViewState.ERROR);
        }
    }

    /*##################################异步加载get方法的相关回调处理end#########################*/
}