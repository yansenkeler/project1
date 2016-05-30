package com.fruit.client.activity;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.client.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.VolleyManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianyx on 16-5-8.
 */
public class ModifyPwdActivity extends NaviActivity {
    private static final int TASK_MODIFY_PWD = 0;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText confirmNewPwd;
    private Button submit;
    private String userNoValue;
    private String pwdValue;
    private String newPwdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle("修改密码");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        oldPwd = (EditText)findViewById(R.id.old_pwd);
        newPwd = (EditText)findViewById(R.id.new_pwd);
        confirmNewPwd = (EditText)findViewById(R.id.confirm_new_pwd);
        submit = (Button)findViewById(R.id.submit);

        userNoValue = DBUtil.getConfigValue("user_name");
        submit.setOnClickListener(this);
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.submit:
                if (checkValue()){
                    modifyPwd();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkValue(){
        pwdValue = oldPwd.getText().toString();
        if (newPwd.getText().toString().equals(confirmNewPwd.getText().toString())){
            newPwdValue = newPwd.getText().toString();
        }else {
            ToastUtil.showShort(this, "输入的两个新密码不一致");
            return false;
        }
        if (pwdValue.length()==0){
            ToastUtil.showShort(this, "输入的旧的密码不能为空");
            return false;
        }
        if (newPwdValue.length()==0){
            ToastUtil.showShort(this, "输入的新的密码不能为空");
            return false;
        }
        return true;
    }

    private void modifyPwd(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "ModifyPwd");
        params.put("userno", userNoValue);
        params.put("pwd", pwdValue);
        params.put("newpwd", newPwdValue);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.PERSONAL_CENTER,
                Object.class, this, TASK_MODIFY_PWD);
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_MODIFY_PWD:
                ToastUtil.showShort(this, "修改密码失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_MODIFY_PWD:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "修改密码成功");
                    }else {
                        ToastUtil.showShort(this, "修改密码失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_pwd;
    }
}
