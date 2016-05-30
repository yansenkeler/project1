package com.fruit.core.activity.templet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.fruit.core.R;
import com.fruit.core.activity.FruitActivity;
import com.fruit.widget.tab.LabelIndicatorStrategy;

import java.util.HashMap;

/**
 * Created by liangchen on 15/3/27.
 * 父tab页面，基于fragment的tab，父activity。
 */
public class FragmentTabActivity extends FruitActivity {
    static final String LOG_TAG = FragmentTabActivity.class.getSimpleName();
    protected TabHost mTabHost;
    protected TabManager mTabManager;

    public void addTab(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle)
    {
        if (paramString == null)
            throw new IllegalArgumentException("title cann't be null!");
        this.mTabManager.addTab(this.mTabHost.newTabSpec(paramString).setIndicator(new LabelIndicatorStrategy(this, paramString, paramInt).createIndicatorView(this.mTabHost)), paramClass, paramBundle);
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setOnContentView();
        this.mTabHost = ((TabHost)findViewById(R.id.tabhost));
        this.mTabHost.setup();
        this.mTabManager = new TabManager(this, this.mTabHost, R.id.realtabcontent);
        setTabWidgetBackground(0);
    }

    protected void onRestoreInstanceState(Bundle paramBundle)
    {
        super.onRestoreInstanceState(paramBundle);
        this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
    }

    protected void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("tab", this.mTabHost.getCurrentTabTag());
    }

    public void setCurrentTab(int index){
        mTabHost.setCurrentTab(index);
    }

    protected void setOnContentView()
    {
        super.setContentView(R.layout.fragment_tabs);
    }

    protected void setTabWidgetBackground(int paramInt)
    {
        if (paramInt > 0)
            this.mTabHost.getTabWidget().setBackgroundResource(paramInt);
    }

    public static class TabManager
            implements TabHost.OnTabChangeListener
    {
        private final FragmentTabActivity mActivity;
        private final int mContainerId;
        TabInfo mLastTab;
        private final TabHost mTabHost;
        private final HashMap<String, TabInfo> mTabs = new HashMap();
        private boolean removeExtraGA = false;

        public TabManager(FragmentTabActivity paramFragmentTabActivity, TabHost paramTabHost, int paramInt)
        {
            this.mActivity = paramFragmentTabActivity;
            this.mTabHost = paramTabHost;
            this.mContainerId = paramInt;
            this.mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec paramTabSpec, Class<?> paramClass, Bundle paramBundle)
        {
            paramTabSpec.setContent(new DummyTabFactory(this.mActivity));
            String str = paramTabSpec.getTag();
            TabInfo localTabInfo = new TabInfo(str, paramClass, paramBundle);
            localTabInfo.fragment = this.mActivity.getSupportFragmentManager().findFragmentByTag(str);
            if ((localTabInfo.fragment != null) && (!localTabInfo.fragment.isHidden()))
            {
                FragmentTransaction localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction();
                localFragmentTransaction.hide(localTabInfo.fragment);
                localFragmentTransaction.commitAllowingStateLoss();
            }
            this.mTabs.put(str, localTabInfo);
            this.mTabHost.addTab(paramTabSpec);
        }

        @Override
        public void onTabChanged(String paramString)
        {
            TabInfo localTabInfo = (TabInfo)this.mTabs.get(paramString);
            FragmentTransaction localFragmentTransaction = null;
            if (this.mLastTab != localTabInfo)
            {
                localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction();
                if ((this.mLastTab != null) && (this.mLastTab.fragment != null))
                    localFragmentTransaction.hide(this.mLastTab.fragment);
                if (localTabInfo!=null){
                    if (localTabInfo.fragment == null){
                        localTabInfo.fragment = Fragment.instantiate(this.mActivity, localTabInfo.clss.getName(), localTabInfo.args);
                        localFragmentTransaction.add(this.mContainerId, localTabInfo.fragment, localTabInfo.tag);
                    }
                }
                Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", newTab.fragment is null, newTab.tag is " + localTabInfo.tag);
            }
                this.mLastTab = localTabInfo;
                localFragmentTransaction.show(localTabInfo.fragment);
                localFragmentTransaction.commitAllowingStateLoss();
                this.mActivity.getSupportFragmentManager().executePendingTransactions();
                this.mActivity.onTabChanged(paramString);
                Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", show fragment success");
        }

        static class DummyTabFactory
                implements TabHost.TabContentFactory
        {
            private final Context mContext;

            public DummyTabFactory(Context paramContext)
            {
                this.mContext = paramContext;
            }

            public View createTabContent(String paramString)
            {
                View localView = new View(this.mContext);
                localView.setMinimumWidth(0);
                localView.setMinimumHeight(0);
                return localView;
            }
        }

        static final class TabInfo
        {
            final Bundle args;
            final Class<?> clss;
            Fragment fragment;
            final String tag;

            TabInfo(String paramString, Class<?> paramClass, Bundle paramBundle)
            {
                this.tag = paramString;
                this.clss = paramClass;
                this.args = paramBundle;
            }
        }
    }

    public void onTabChanged(String paramString) {

    }
}

