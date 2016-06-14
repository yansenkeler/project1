package com.fruit.client.activity;

import android.content.Intent;
import com.fruit.client.R;
import com.fruit.client.util.Constant;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.client.util.Urls;
import com.fruit.common.file.FileUtil;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.VolleyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/2/26.
 */
public class AddPileActivity extends NaviActivity implements View.OnClickListener{
    private static final String managerUrl = "AJAXReturnData/Pile.ashx";
    private static final int TASK_ADD = 1;
    private static final int TASK_GETROUTE = 2;
    private Spinner mSpinner;
    private EditText mMilePost, mPile, mMemo;
    private TextView mAdd;
    private TextView currentAddress;
//    private TextView routeCodeText;

    private ArrayAdapter<String> mSpinnerAdapter;
    private ArrayList<String> mParams = new ArrayList<>();
    String paramMilePost, paramPile, paramMemo;
    private double latitude, longitude;
    private String currentAddressString;
    private boolean valid = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("latitude") && intent.hasExtra("longitude")
                && intent.hasExtra("currentAddress")){
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
            currentAddressString = intent.getStringExtra("currentAddress");
        }else {
            return;
        }
        super.onCreate(savedInstanceState);
        setUseMutiStateView(false);
        initData();
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        setActivityTitle("添加路桩");
        setLeftImageView(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_48dp));

        mSpinner = (Spinner)findViewById(R.id.spin_route);
        mMilePost = (EditText)findViewById(R.id.edittext_milepost);
        mPile = (EditText)findViewById(R.id.edittext_pile);
        mMemo = (EditText)findViewById(R.id.edittext_memo);
        mAdd = (TextView)findViewById(R.id.textview_add);
        currentAddress = (TextView)findViewById(R.id.currentAddress);
//        routeCodeText = (TextView)findViewById(R.id.text_route_code);

        currentAddress.setText("当前位置："+currentAddressString);

        mSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mParams);
        mSpinner.setAdapter(mSpinnerAdapter);
        mAdd.setOnClickListener(this);

        String lastPileRoute = DBUtil.getConfigValue("LastPileRoute");
//        if (lastPileRoute!=null && lastPileRoute.length()>0){
//            for (int i=0; i<mParams.size(); i++){
//                if (mParams.get(i).equals(lastPileRoute)){
//                    mSpinner.setSelection(i);
//                    break;
//                }
//            }
//        }
    }

    private void getRouteCode(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "queryRouteCode");
        params.put("longitude", longitude+"");
        params.put("latitude", latitude + "");
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT + Urls.ADD_PILE, Object.class, this, TASK_GETROUTE);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void leftClick() {
        super.leftClick();
        onBackPressed();
    }

    private void initData() {
        String filePath  = Constant.getStaticParamsDir(this)+Constant.PARAM_NAME;
        if (new File(filePath).exists()){
            String jsonString = FileUtil.readStringFromFile(filePath);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONArray mJSONArray = jsonObject.getJSONArray("路线");
            mParams.clear();
            for (int i=0; i<mJSONArray.size(); i++){
                JSONObject jsonObject1  =mJSONArray.getJSONObject(i);
                mParams.add(jsonObject1.getString("name"));
            }
        }else {
            ToastUtil.showShort(this, "请先初始化数据");
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_pile;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        getRouteCode();
    }

    @Override
    public void onResponse(Object response, int taskid) {
        super.onResponse(response, taskid);
        switch (taskid){
            case TASK_ADD:
//                DBUtil.setConfigValue("LastPileRoute", (String) mSpinner.getSelectedItem());
                JSONObject mJSONObject = (JSONObject)response;
                ToastUtil.showShort(this, mJSONObject.getString("msg"));
                hideProgressDialog();
                setResult(RESULT_OK);
                finish();
                break;
            case TASK_GETROUTE:
                if(response!=null){
                    JSONObject jsonObject  = (JSONObject)response;
                    String flag = (String)jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        String routeCodeValue = jsonObject2.getString("RouteCode");
//                        routeCodeText.setText(routeCodeValue);
                        for (int i=0; i<mParams.size(); i++){
                            if (mParams.get(i).equals(routeCodeValue)){
                                mSpinner.setSelection(i);
                            }
                        }
                        valid = true;
                    }else if (flag.equals("0001")){
                        valid = false;
                        mSpinner.setSelection(0);
//                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加路桩");
                    }else {
                        valid = false;
                        mSpinner.setSelection(0);
//                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加路桩");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {
        super.onErrorResponse(error, taskid);
        switch (taskid){
            case TASK_ADD:
                hideProgressDialog();
                ToastUtil.showShort(this, "添加失败");
                break;
            case TASK_GETROUTE:
                valid = false;
                mSpinner.setSelection(0);
                ToastUtil.showShort(this, "你无法在此处添加路桩");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textview_add:
                if (valid){
                    addPile();
                }else{
                    ToastUtil.showShort(this, "你无法在此处添加路桩");
                }
                break;
            default:
                break;
        }
    }

    private boolean checkAddPileParams() {
        paramMilePost = mMilePost.getText().toString();
        paramPile = mPile.getText().toString();
        paramMemo = mMemo.getText().toString();
        if (paramMilePost==null || paramMilePost.length()==0){
            ToastUtil.showShort(this, "填写里程碑");
            return false;
        }
        if (paramPile==null || paramPile.length()==0){
            ToastUtil.showShort(this, "填写百米桩");
            return false;
        }
        if (latitude==0 || longitude==0){
            ToastUtil.showShort(this, "无法获得当前位置经纬度");
            return  false;
        }
        return true;
    }

    private void addPile() {
        if (checkAddPileParams()) {
            if (!isFinishing()){
                showDialog("正在添加路桩...");
            }
            String str = Constant.url + managerUrl;
            HashMap<String, String> mParams = new HashMap<>();
            mParams.put("type", "add");
            mParams.put("routecode", (String) mSpinner.getSelectedItem());
//            mParams.put("routecode", routeCodeText.getText().toString().equals("查无此路线")?"":routeCodeText.getText().toString());
            mParams.put("milepost", paramMilePost);
            mParams.put("pile", paramPile);
            mParams.put("memo", paramMemo);
            mParams.put("longitude", longitude+"");
            mParams.put("latitude", latitude+"");
            String requestBody = NetWorkUtil.appendParameter(str, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, str, Object.class, this, TASK_ADD);
        }
    }
}
