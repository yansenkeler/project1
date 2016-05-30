package com.fruit.client.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import com.fruit.client.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.VolleyError;
import com.fruit.core.api.VolleyResponse;
import com.fruit.widget.MultiStateView;
import com.fruit.widget.navi.TopBarNormal;
import com.fruit.widget.progressDialog.SimpleProgressDialog;

/**
 * Created by user on 2016/2/25.
 */
public abstract class BaseActivity extends AppCompatActivity implements TopBarNormal.TopbarClickListener, VolleyResponse{
    private TopBarNormal mTopBarNormal;
    private MultiStateView mMultiStateView;
    private FrameLayout mContent;
    private SimpleProgressDialog mDialog;
    private boolean useStateView;
    private boolean useErrorView, useEmptyView, useLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mTopBarNormal = (TopBarNormal)findViewById(R.id.topbar);
        mMultiStateView = (MultiStateView)findViewById(R.id.multistateview);
        mContent = (FrameLayout)findViewById(R.id.content_layout);

        setUseStateView(true);
        mMultiStateView.setViewForState(setErrorLayout() == 0 ? R.layout.default_layout_error : setErrorLayout(), MultiStateView.ViewState.ERROR);
        mMultiStateView.setViewForState(setEmptyLayout()==0?R.layout.default_layout_empty:setEmptyLayout(), MultiStateView.ViewState.EMPTY);
        mMultiStateView.setViewForState(setLoadingLayout()==0?R.layout.default_layout_loading:setLoadingLayout(), MultiStateView.ViewState.LOADING);
        mContent.addView(LayoutInflater.from(this).inflate(setContentLayout(), null, false),
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mTopBarNormal.setOnTopbarClickListener(this);
        getStateView(MultiStateView.ViewState.ERROR).findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorRetry();
            }
        });
        getStateView(MultiStateView.ViewState.EMPTY).findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyRetry();
            }
        });
    }

    public void showLoadingDialog(String msg)
    {
        if (mDialog==null)
        {
            mDialog = SimpleProgressDialog.createLoadingDialog(this);
        }
        if (!mDialog.isShowing())
        {
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDialog.dismiss();
                }
            });
            mDialog.setMsg(msg);
            mDialog.show();
        }
    }

    public void hideLoadingDialog()
    {
        if (mDialog!=null && mDialog.isShowing())
        {
            mDialog.dismiss();
        }
    }

    /**
     * 设置使用了MultiStateView里面的哪几个View
     * @param useErrorView
     * @param useEmptyView
     * @param useLoadingView
     */
    public void setUseView(boolean useErrorView, boolean useEmptyView, boolean useLoadingView){
        this.useErrorView = useErrorView;
        this.useEmptyView = useEmptyView;
        this.useLoadingView = useLoadingView;
    }

    public abstract int setContentLayout();

    public boolean isUseStateView() {
        return useStateView;
    }

    /**
     * 设置是否使用multiStateView
     * @param useStateView
     */
    public void setUseStateView(boolean useStateView) {
        this.useStateView = useStateView;
    }

    /**
     * 如果使用了自定义的errorView,emptyView,loadingView,返回资源id
     * @return
     */
    public int setErrorLayout(){
        return 0;
    }

    public int setEmptyLayout(){
        return 0;
    }

    public int setLoadingLayout(){
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void leftClick() {
        onLeftImageClick();
    }

    @Override
    public void rightClick() {
        onRightImageClick();
    }

    @Override
    public void leftTextClick() {
        onLeftTextClick();
    }

    @Override
    public void rightTextClick() {
        onRightTextClick();
    }

    /**
     * topbar左边图片，文字，右边图片，文字的点击接口
     */
    public void onLeftImageClick(){}
    public void onLeftTextClick(){}
    public void onRightTextClick(){}
    public void onRightImageClick(){}

    /**
     * 设置topbar上面的图片，文字
     * @param drawable
     */
    public void setLeftImage(Drawable drawable){
        mTopBarNormal.setLeftImage(drawable);
    }

    public void setLeftText(String text){
        mTopBarNormal.setLeftText(text);
    }

    public void setRightText(String text){
        mTopBarNormal.setRightText(text);
    }

    public void setRightImage(Drawable drawable){
        mTopBarNormal.setRightImage(drawable);
    }

    public void setTopBarTitle(String title)
    {
        mTopBarNormal.setTitle(title);
    }

    public void setTopBarBackgroundColor(int color){
        mTopBarNormal.setBarBackground(color);
    }

    public void setTopBarTextColor(int color){
        mTopBarNormal.setTopBarTextColor(color);
    }

    /**
     * 请求成功获得数据返回接口
     * @param response
     * @param taskid
     */
    @Override
    public void onResponse(Object response, int taskid) {
        if (isUseStateView()){
            mMultiStateView.setViewState(MultiStateView.ViewState.CONTENT);
        }
    }

    /**
     * 请求失败返回接口
     * @param error
     * @param taskid
     */
    @Override
    public void onErrorResponse(VolleyError error, int taskid) {
        if (isUseStateView() && useErrorView){
            mMultiStateView.setViewState(MultiStateView.ViewState.ERROR);
        }
    }

    /**
     * 设置是否显示topbar
     * @param shown
     */
    public void setTopBarNormal(boolean shown){
        if (shown){
            mTopBarNormal.setVisibility(View.VISIBLE);
        }else {
            mTopBarNormal.setVisibility(View.GONE);
        }
    }

    /**
     * 获取multiViewState中的ErrorView,EmptyView,LoadingView视图
     * @param viewState 获取视图状态
     * @return
     */
    public View getStateView(MultiStateView.ViewState viewState){
        if (viewState == MultiStateView.ViewState.ERROR){
            return mMultiStateView.getView(MultiStateView.ViewState.ERROR);
        }else if (viewState == MultiStateView.ViewState.EMPTY){
            return mMultiStateView.getView(MultiStateView.ViewState.EMPTY);
        }else if (viewState == MultiStateView.ViewState.LOADING){
            return mMultiStateView.getView(MultiStateView.ViewState.LOADING);
        }else {
            return null;
        }
    }

    /**
     * 切换MultiStateView的视图状态
     * @param viewState
     */
    public void setState(MultiStateView.ViewState viewState){
        mMultiStateView.setViewState(viewState);
    }

    /**
     * 默认的ErrorView,EmptyView中点击重试接口
     */
    public void errorRetry(){}

    public void emptyRetry(){}
}
