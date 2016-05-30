package com.fruit.client.fragment;

import android.content.Intent;
import com.fruit.client.R;
import com.fruit.client.activity.EventDetailEditActivity;
import com.fruit.client.adapter.EventAdapter;
import com.fruit.client.object.event.Event;
import com.fruit.client.object.event.EventListResponse;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.fragment.FruitFragment;
import com.fruit.core.http.VolleyManager;
import com.fruit.widget.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/4/23.
 */
public class EventFragment extends FruitFragment {
    private static final int TASK_QUERY = 0;

    private MultiStateView multiStateView;
    private SwipeRefreshLayout mRefreshLayout;
    private ListView eventList;
    private Button retry;

    private EventAdapter mAdapter;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private EventRefreshListener mRefreshListener;
    private String roleName;
    private String deptName;
    private String userName;

    private boolean requestTwice = false;

    public EventFragment(){}

    public static synchronized EventFragment getInstance(){
        EventFragment mEventFragment = new EventFragment();
        Bundle mBundle = new Bundle();
        mEventFragment.setArguments(mBundle);
        return mEventFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        mAdapter = new EventAdapter(mEvents, getActivity());
        mRefreshListener = new EventRefreshListener();
    }

    private void initData(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, null, false);
        multiStateView = (MultiStateView)v.findViewById(R.id.multistateview);
        multiStateView.setViewForState(R.layout.layout_content_list, MultiStateView.ViewState.CONTENT);
        multiStateView.setViewForState(R.layout.layout_loading, MultiStateView.ViewState.LOADING);
        multiStateView.setViewForState(R.layout.layout_error, MultiStateView.ViewState.ERROR);
        multiStateView.setViewForState(R.layout.layout_empty, MultiStateView.ViewState.EMPTY);

        mRefreshLayout = (SwipeRefreshLayout)multiStateView.getView(MultiStateView.ViewState.CONTENT).findViewById(R.id.refresh_container);
        eventList = (ListView)multiStateView.getView(MultiStateView.ViewState.CONTENT).findViewById(R.id.list_view);
        retry = (Button)multiStateView.getView(MultiStateView.ViewState.ERROR).findViewById(R.id.retry);

        eventList.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = mEvents.get(position);
                Intent intent = new Intent(getActivity(), EventDetailEditActivity.class);
                intent.putExtra("bill_no", event.getBillNo());
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiStateView.setViewState(MultiStateView.ViewState.LOADING);
                mRefreshLayout.setRefreshing(true);
                mRefreshListener.onRefresh();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestTwice = false;
        mEvents.clear();
        multiStateView.setViewState(MultiStateView.ViewState.LOADING);
        mRefreshLayout.setRefreshing(true);
        mRefreshListener.onRefresh();
    }

    @Override
    public void onFruitClick(int id) {

    }

    private void getEventList(){
        Map<String, String> params = new HashMap<>();
        roleName = DBUtil.getConfigValue("role_name");
        deptName = DBUtil.getConfigValue("dept_name");
        userName = DBUtil.getConfigValue("user_name");
        //施工，路政确认只需要获取处理中状态的事件
        if (roleName.equals(Constant.RoleName.CONSTRUCTION) || roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)){
            params.put("state", "处理中");
            params.put("rolename", roleName);
            params.put("deptname", deptName);
        }else {
            //系统管理员，路政，路网，养护需要先获取草稿状态的事件
            params.put("state", "草稿");
            params.put("rolename", roleName);
            params.put("deptname", deptName);
            params.put("username", userName);
        }
        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                 params, Urls.ROOT+Urls.EVENT_QUERY, EventListResponse.class, this, TASK_QUERY);
    }

    @Override
    public void dealSuccessResult(Object data, int taskid) {
        super.dealSuccessResult(data, taskid);
        switch (taskid){
            case TASK_QUERY:
                EventListResponse eventListResponse = (EventListResponse)data;
                if (eventListResponse.getFlag().equals("0000")){
                    Object object = eventListResponse.getData();
                    if (object instanceof JSONObject){
                        JSONObject jsonObject = (JSONObject)object;
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject json = jsonArray.getJSONObject(i);
                            Event event = new Event();
                            event.setBillDate(json.getString("BillDate"));
                            event.setBillNo(json.getString("BillNo"));
                            event.setBillPk(json.getString("BillPk"));
                            event.setCarNo(json.getString("CarNo"));
                            event.setDirection(json.getString("Direction"));
                            event.setFacilityPk(json.getString("FacilityPk"));
                            event.setGrade(json.getString("Grade"));
                            event.setIsProcess(json.getString("IsProcess"));
                            event.setLat(json.getString("Lat"));
                            event.setLon(json.getString("Lon"));
                            event.setMemo(json.getString("Memo"));
                            event.setNumber(json.getString("Number"));
                            event.setPileNo(json.getString("PileNo"));
                            event.setProcessDept(json.getString("ProcessDept"));
                            event.setProcessType(json.getString("ProcessType"));
                            event.setRequEndTime(json.getString("RequEndTime"));
                            event.setRealName(json.getString("RealName"));
                            event.setRouteNo(json.getString("RouteNo"));
                            event.setState(json.getString("State"));
                            event.setType(json.getString("Type"));
                            event.setType1(json.getString("Type1"));
                            event.setType2(json.getString("Type2"));
                            event.setType3(json.getString("Type3"));
                            event.setUnit(json.getString("unit"));
                            event.setUserName(json.getString("userName"));
                            event.setUserPhone(json.getString("userPhone"));
                            mEvents.add(event);
                        }
                    }
                    if (roleName.equals(Constant.RoleName.CONSTRUCTION)||roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)
                            ||roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)||requestTwice){
                        mAdapter.notifyDataSetChanged();
                        if (mEvents.size()>0){
                            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
                        }else {
                            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
                        }
                    }else {
                        //继续获取处理中的事件
                        Map<String, String> params = new HashMap<>();
                        params.put("state", "处理中");
                        params.put("rolename", roleName);
                        params.put("deptname", deptName);
                        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
                        VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                                params, Urls.ROOT+Urls.EVENT_QUERY, EventListResponse.class, this, TASK_QUERY);
                        requestTwice = true;
                    }
                }else if (eventListResponse.getFlag().equals("0001")){
                    if (requestTwice){
                        mAdapter.notifyDataSetChanged();
                        if (mEvents.size()>0){
                            multiStateView.setViewState(MultiStateView.ViewState.CONTENT);
                        }else {
                            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
                        }
                    }else {
                        if (roleName.equals(Constant.RoleName.CONSTRUCTION)||roleName.equals(Constant.RoleName.LUZHENG_CONFIRM)
                                ||roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)){
                            multiStateView.setViewState(MultiStateView.ViewState.EMPTY);
                        }else {
                            Map<String, String> params = new HashMap<>();
                            params.put("state", "处理中");
                            params.put("rolename", roleName);
                            params.put("deptname", deptName);
                            String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
                            VolleyManager.newInstance(getActivity()).JsonPostRequest(null, "utf-8", requestBody,
                                    params, Urls.ROOT+Urls.EVENT_QUERY, EventListResponse.class, this, TASK_QUERY);
                            requestTwice = true;
                        }
                    }
                }else {
                    multiStateView.setViewState(MultiStateView.ViewState.ERROR);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void dealFailResult(VolleyError returnString, int taskid) {
        super.dealFailResult(returnString, taskid);
        switch (taskid){
            case TASK_QUERY:
                ToastUtil.showShort(getActivity(), "获取事件失败");
                multiStateView.setViewState(MultiStateView.ViewState.ERROR);
                break;
            default:
                break;
        }
    }

    class EventRefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            getEventList();
        }
    }
}
