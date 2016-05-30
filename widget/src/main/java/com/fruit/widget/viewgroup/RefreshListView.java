package com.fruit.widget.viewgroup;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.demievil.library.RefreshLayout;
import com.fruit.widget.R;

/**
 * Created by user on 2016/1/21.
 */
public class RefreshListView extends LinearLayout {
    public static final int STATE_MORE = 0;
    public static final int STATE_NOMORE = 1;
    public static final int STATE_REFRESH = 2;
    private LayoutInflater inflater;
    private LinearLayout rootView;
    private RefreshLayout refreshLayout;
    private ListView listView;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RefreshLayout.OnLoadListener loadListener;
    private AbsListView.OnScrollListener scrollListener;
    private PullDownListener pullDownListener;
    private PullUpListener pullUpListener;
    private FrameLayout footerView;
    private FrameLayout defaultFooterView;
    private FrameLayout refreshFooterView;
    private FrameLayout nomoreFooterView;
    private View defFooterView;
    private View refFooterView;
    private View nomFooterView;
    private ListAdapter adapter;
    private boolean canLoadMore = true;

    public RefreshListView(Context context) {
        super(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        rootView = (LinearLayout)inflater.inflate(R.layout.listview_refresh, null, false);
        refreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshContainer);
        listView = (ListView)rootView.findViewById(R.id.listView);
        footerView = (FrameLayout)inflater.inflate(R.layout.footer_activity_refresh, null, false);
        defaultFooterView = (FrameLayout)footerView.findViewById(R.id.defaultContainer);
        refreshFooterView = (FrameLayout)footerView.findViewById(R.id.refreshContainer);
        nomoreFooterView = (FrameLayout)footerView.findViewById(R.id.nomoreContainer);
        defFooterView = inflater.inflate(R.layout.layout_default_footerview, null, false);
        refFooterView = inflater.inflate(R.layout.layout_refresh_footerview, null, false);
        nomFooterView = inflater.inflate(R.layout.layout_nomore_footerview, null, false);
        defaultFooterView.addView(defFooterView);
        refreshFooterView.addView(refFooterView);
        nomoreFooterView.addView(nomFooterView);

        defaultFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListener.onLoad();
            }
        });

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownListener.onRefresh();
            }
        };
        loadListener = new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                setFooterViewState(STATE_REFRESH);
                pullUpListener.onLoad();
            }
        };
        scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
                            loadListener.onLoad();
                        } else if (listView.getFirstVisiblePosition() == 0) {

                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };

        listView.setOnScrollListener(scrollListener);
        listView.addFooterView(footerView);
        refreshLayout.setOnRefreshListener(refreshListener);
        refreshLayout.setOnLoadListener(loadListener);
        refreshLayout.setChildView(listView);
        this.addView(rootView);
    }

    public void setDefaultFooterView(int resourceId){
        View v = inflater.inflate(resourceId, null, false);
        defaultFooterView.removeAllViews();
        defaultFooterView.addView(v);
    }

    public void setRefreshFooterView(int resourceId){
        View v = inflater.inflate(resourceId, null, false);
        refreshFooterView.removeAllViews();
        refreshFooterView.addView(v);
    }

    public void setNomoreFooterView(int resourceId){
        View v = inflater.inflate(resourceId, null, false);
        nomoreFooterView.removeAllViews();
        nomoreFooterView.addView(v);
    }

    public void setAdapter(ListAdapter adapter){
        this.adapter = adapter;
        listView.setAdapter(this.adapter);
    }

    public void setPullDownListener(PullDownListener pullDownListener) {
        if (pullDownListener!=null){
            this.pullDownListener = pullDownListener;
        }else {
            this.pullDownListener = null;
            refreshLayout.setOnRefreshListener(null);
        }

    }

    public void setPullUpListener(PullUpListener pullUpListener) {
        if (pullUpListener!=null){
            this.pullUpListener = pullUpListener;
        }else {
            footerView.setVisibility(GONE);
            refreshLayout.setOnLoadListener(null);
            listView.setOnScrollListener(null);
        }
    }

    public void setLoad(boolean isLoad){
        if (isLoad){
            setFooterViewState(STATE_REFRESH);
            refreshLayout.setLoading(true);
        }else {
            setFooterViewState(STATE_MORE);
            refreshLayout.setLoading(false);
        }
    }

    public void setRefresh(boolean isRefresh){
        if (isRefresh){
            refreshLayout.setRefreshing(true);
        }else {
            refreshLayout.setRefreshing(false);
        }
    }

    public void setFooterViewState(int state){
        switch (state){
            case STATE_NOMORE:
                nomoreFooterView.setVisibility(VISIBLE);
                defaultFooterView.setVisibility(GONE);
                refreshFooterView.setVisibility(GONE);
                break;
            case STATE_MORE:
                nomoreFooterView.setVisibility(GONE);
                defaultFooterView.setVisibility(VISIBLE);
                refreshFooterView.setVisibility(GONE);
                break;
            case STATE_REFRESH:
                nomoreFooterView.setVisibility(GONE);
                defaultFooterView.setVisibility(GONE);
                refreshFooterView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    public void disableLoadMore(){
        if (canLoadMore){
            listView.setOnScrollListener(null);
            refreshLayout.setOnLoadListener(null);
            canLoadMore = false;
        }
    }

    public void enableLoadMore(){
        if (!canLoadMore){
            listView.setOnScrollListener(scrollListener);
            refreshLayout.setOnLoadListener(loadListener);
            canLoadMore = true;
        }

    }

    public AbsListView.OnScrollListener getScrollListener() {
        return scrollListener;
    }

    public interface PullDownListener{
        void onRefresh();
    }

    public interface PullUpListener{
        void onLoad();
    }

}
