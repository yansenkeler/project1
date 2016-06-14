package com.fruit.client.activity;

import android.content.Intent;
import android.graphics.Color;
import com.fruit.client.R;
import com.fruit.client.db.DatabaseHelp;
import com.fruit.client.util.Constant;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.db.models.gen.accountPassword;
import com.fruit.core.http.VolleyManager;
import com.fruit.widget.MultiStateView;

import java.util.Map;

/**
 * Created by user on 2016/2/24.
 */
public class LoginActivity extends BaseActivity {
    private String url = "AJAXReturnData/Login.ashx";
    private static final int TASK_LOGIN = 0;

    private EditText mEditText1, mEditText2;
    private Button login;
    private CheckBox mRememberPassword;
    private RelativeLayout root;
    private String channelIdValue;
    private boolean pushServiceInitialSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        if (DatabaseHelp.isLogin()){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        mEditText1 = (EditText)findViewById(R.id.input_account);
        mEditText2 = (EditText)findViewById(R.id.input_password);
        login = (Button)findViewById(R.id.login);
        mRememberPassword = (CheckBox)findViewById(R.id.remember_password);
        root = (RelativeLayout)findViewById(R.id.root);

        setTopBarNormal(false);
        setUseView(false, false, true);
        setInputText();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //查询数据库中有没有保存该账号密码
                String mPassword = DBUtil.getPassword(s.toString());
                if (mPassword!=null && mPassword.length()>0){
                    mEditText2.setText(mPassword);
                }else {
                    mEditText2.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        channelIdValue = DBUtil.getConfigValue("channelId");
        Log.d("channelid", "in login oncreate channelid: "+channelIdValue);
        if (channelIdValue!=null && channelIdValue.length()>0){
            pushServiceInitialSuccess = true;
        }else {
            pushServiceInitialSuccess = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化输入框和记住密码勾选框
     * 如果从数据库中有读取到上次登录是记住密码的账号
     * 则在输入框中自动填入该账号密码，同时记住密码打勾
     * 如果上次登录时没有勾选记住密码，则进入界面时不会自动填入
     */
    private void setInputText(){
        accountPassword mAccountPassword = DBUtil.getLastLoginAccount();
        if (mAccountPassword!=null && mAccountPassword.getAccount()!=null && mAccountPassword.getPassword()!=null)
        {
            if (DatabaseHelp.isRememberPassword())
            {
                mEditText1.setText(mAccountPassword.getAccount());
                mEditText2.setText(mAccountPassword.getPassword());
                mRememberPassword.setChecked(true);
            }
            else
            {
                mRememberPassword.setChecked(false);
            }
        }
        else
        {
            mRememberPassword.setChecked(false);
        }
    }

    @Override
    public int setContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResponse(Object response, int taskid) {
        super.onResponse(response, taskid);
        switch (taskid){
            case TASK_LOGIN:
                dealSuccessLogin(response);
                break;
            default:
                break;
        }
    }

    /**
     * 处理登录成功
     * @param response
     */
    private void dealSuccessLogin(Object response){
        Map mMap = (Map)response;
        if (mMap.get("flag").equals(Constant.LoginStatus.STATUS_SUCCESS)){
            DatabaseHelp.setLoginState(true);
            saveRemPwdState();
            JSONObject jsonObject = (JSONObject)response;
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            JSONArray jsonArray = jsonObject1.getJSONArray("result");
            JSONObject jsonObject2 = jsonArray.getJSONObject(0);
            String userName = jsonObject2.getString("UserNo");
            String userPhone = jsonObject2.getString("Phone");
            String roleName = jsonObject2.getString("roleName");
            String deptName = jsonObject2.getString("deptname");
            String deptPk = jsonObject2.getString("DeptPK");
            Log.d("deptpk", deptPk);
            String realName = jsonObject2.getString("RealName");
            DBUtil.setConfigValue("user_name", userName!=null?userName:"");
            DBUtil.setConfigValue("user_phone", userPhone!=null?userPhone:"");
            DBUtil.setConfigValue("role_name", roleName!=null?roleName:"");
            DBUtil.setConfigValue("dept_name", deptName!=null?deptName:"");
            DBUtil.setConfigValue("dept_pk", deptPk!=null?deptPk:"");
            DBUtil.setConfigValue("real_name", realName!=null?realName:"");
            if (!pushServiceInitialSuccess){
                ToastUtil.showShort(LoginActivity.this, "登录成功，但是无法接收到消息推送");
            }
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }else if (mMap.get("flag").equals("0001")){
            ToastUtil.showShort(LoginActivity.this, "账户名或密码错误");
        }else if (mMap.get("flag").equals("0002")){
            ToastUtil.showShort(LoginActivity.this, "账号未激活");
        }else if (mMap.get("flag").equals("0003")){
            ToastUtil.showShort(LoginActivity.this, "账号被禁用");
        }else{
            ToastUtil.showShort(LoginActivity.this, "其他错误");
        }
        hideLoadingDialog();
    }

    /**
     * 处理登录失败
     * @param mError
     */
    private void dealFailLogin(VolleyError mError){
        setState(MultiStateView.ViewState.CONTENT);
        ToastUtil.showShort(LoginActivity.this, "网络错误");
        hideLoadingDialog();
    }

    @Override
    public void onErrorResponse(VolleyError error, int taskid) {
        super.onErrorResponse(error, taskid);
        switch (taskid){
            case TASK_LOGIN:
                dealFailLogin(error);
                break;
            default:
                break;
        }
    }

    @Override
    public int setLoadingLayout() {
        return R.layout.navi_default_layout_loading;
    }

    private void login(){
        if (checkValidate()){
            String string;
            if (pushServiceInitialSuccess){
                string = Constant.url + url + "?userno="+mEditText1.getText().toString()+"&pwd="+mEditText2.getText().toString()+"&channelid="+channelIdValue;
            }else {
                string = Constant.url + url + "?userno="+mEditText1.getText().toString()+"&pwd="+mEditText2.getText().toString();
            }
            Log.d("channelid", string);
//            ToastUtil.showLong(this, "channelid: "+channelIdValue);
            VolleyManager.newInstance(LoginActivity.this).JsonGetRequest(null, string, Object.class, LoginActivity.this, TASK_LOGIN);
            showLoadingDialog("登录中...");
        }
    }

    private void saveRemPwdState(){
        if (mRememberPassword.isChecked()){
            DatabaseHelp.setRememPassword(true);
            DBUtil.insertAccountPassword(mEditText1.getText().toString(), mEditText2.getText().toString(), true);
        }else {
            DatabaseHelp.setRememPassword(false);
        }
    }

    private boolean checkValidate(){
        boolean result = true;
        if (mEditText1.getText().toString().length()==0){
            ToastUtil.showShort(this, "账号未输入");
            result = false;
        }else {
            if (mEditText2.getText().toString().length()==0){
                ToastUtil.showShort(this, "密码未输入");
                result = false;
            }
        }
//        if (channelIdValue!=null && channelIdValue.length()>0){
//            result = true;
//        }else {
//            ToastUtil.showShort(this, "channelId无效");
//            result = false;
//        }
        return  result;
    }

    @Override
    public void errorRetry() {
        super.errorRetry();
        login();
    }
}
