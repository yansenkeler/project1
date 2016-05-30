package com.fruit.client.activity;

import com.fruit.client.R;
import com.fruit.client.object.event.Event;
import com.fruit.client.object.event.EventListResponse;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.VolleyManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianyx on 16-5-7.
 */
public class EventDetailActivity extends NaviActivity{
    private static final int TASK_DETAIL = 0;

    private TextView textStatus;
    private TextView textEventNo;
    private TextView textBillDate;
    private TextView textRequEndDate;
    private TextView textGrade;
    private TextView textCarNo;
    private TextView textRouteCode;
    private TextView textPileNo;
    private TextView textUserName;
    private TextView textUserPhone;
    private TextView textDirection;
    private TextView textLonLat;
    private Button btnCheckMap;
    private TextView textType1;
    private TextView textType2;
    private TextView textType3;
    private TextView textCltype;
    private TextView textCount;
    private TextView textProcessType;
    private TextView textIsProcess;
    private TextView textProcessDept;
    private TextView textMemo;
    private TextView textNoPic;
    private GridView gridUploadedImg;
    private GridView gridUploadImg;
    private Button btnUploadImg;
    private Button btnSubmit;

    private String billNoValue;
    private String userName;
    private String roleName;
    private String deptName;
    private Event event;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = DBUtil.getConfigValue("user_name");
        roleName = DBUtil.getConfigValue("role_name");
        deptName = DBUtil.getConfigValue("dept_name");
        event = new Event();
        setActivityTitle("事件详情");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        initView();
    }

    private void initView(){
        textStatus = (TextView)findViewById(R.id.text_status);
        textEventNo = (TextView)findViewById(R.id.text_event_no);
        textBillDate = (TextView)findViewById(R.id.text_bill_date);
        textRequEndDate = (TextView)findViewById(R.id.text_requ_end_date);
        textGrade = (TextView)findViewById(R.id.text_grade);
        textCarNo = (TextView)findViewById(R.id.text_car_no);
        textRouteCode = (TextView)findViewById(R.id.text_route_code);
        textPileNo = (TextView)findViewById(R.id.text_pile_no);
        textUserName= (TextView)findViewById(R.id.text_user_name);
        textUserPhone= (TextView)findViewById(R.id.text_user_phone);
        textDirection = (TextView)findViewById(R.id.text_direction);
        textLonLat = (TextView)findViewById(R.id.text_lon_lat);
        btnCheckMap = (Button)findViewById(R.id.btn_check_map);
        textType1 = (TextView)findViewById(R.id.text_type1);
        textType2 = (TextView)findViewById(R.id.text_type2);
        textType3 = (TextView)findViewById(R.id.text_type3);
        textCltype = (TextView)findViewById(R.id.text_cltype);
        textCount = (TextView)findViewById(R.id.text_count);
        textProcessType = (TextView)findViewById(R.id.text_process_type);
        textIsProcess = (TextView)findViewById(R.id.text_is_process);
        textProcessDept = (TextView)findViewById(R.id.text_process_dept);
        textMemo = (TextView)findViewById(R.id.text_memo);
        textNoPic = (TextView)findViewById(R.id.text_no_pic);
        gridUploadedImg = (GridView)findViewById(R.id.grid_uploaded_img);
        gridUploadImg= (GridView)findViewById(R.id.grid_upload_img);
        btnUploadImg = (Button)findViewById(R.id.btn_upload_img);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_DETAIL:
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_DETAIL:
                EventListResponse eventListResponse = (EventListResponse)returnObject;
                if (eventListResponse.getFlag().equals("0000")) {
                    Object object = eventListResponse.getData();
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) object;
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        JSONObject json = jsonArray.getJSONObject(0);
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

                        state = event.getState();
                        textStatus.setText("当前状态："+event.getState());
                        textEventNo.setText(event.getBillNo());
                        textBillDate.setText(event.getBillDate());
                        textRequEndDate.setText(event.getRequEndTime());
                        textGrade.setText(event.getGrade());
                        textCarNo.setText(event.getCarNo());
                        textRouteCode.setText(event.getRouteNo());
                        textPileNo.setText(event.getPileNo());
                        textUserName.setText(event.getUserName());
                        textUserPhone.setText(event.getUserPhone());
                        textDirection.setText(event.getDirection());
                        textLonLat.setText("经度："+event.getLon()+"\n纬度"+event.getLat());
                        textType1.setText(event.getType1());
                        textType2.setText(event.getType2());
                        textType3.setText(event.getType3());
                        textCltype.setText(event.getType());
                        textCount.setText(event.getNumber().split(":")[0]+" "+event.getUnit());
                        textProcessType.setText(event.getProcessType());
                        textIsProcess.setText(event.getIsProcess().equals("true")?"是":"否");
                        textProcessDept.setText(event.getProcessDept());
                        textMemo.setText(event.getMemo());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEventDetail();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_event;
    }

    private void getEventDetail(){
        Map<String, String> params = new HashMap<>();
        params.put("billno", billNoValue);
        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params,
                Urls.ROOT+Urls.EVENT_QUERY, EventListResponse.class, this, TASK_DETAIL);
    }

    private void updateView(){

    }
}
