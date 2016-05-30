package com.fruit.client.fragment;

import android.content.Intent;
import com.fruit.client.R;
import com.fruit.client.activity.FinishedEventActivity;
import com.fruit.client.activity.LoginActivity;
import com.fruit.client.activity.ModifyPwdActivity;
import com.fruit.client.activity.MyMsgActivity;
import com.fruit.client.activity.UnfinishEventActivity;
import com.fruit.client.db.DatabaseHelp;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fruit.common.ui.ToastUtil;
import com.fruit.core.db.DBUtil;
import com.fruit.core.fragment.FruitFragment;

/**
 * Created by user on 2016/4/23.
 */
public class PersonalFragment extends FruitFragment {
    private RelativeLayout usr, myMsg, finishedEvent, unfinishEvent, modifyPwd;
    private ImageView usrAvatar;
    private TextView usrName;
    private Button logout;

    public PersonalFragment(){}

    public static synchronized PersonalFragment getInstance(){
        PersonalFragment mFragment = new PersonalFragment();
        Bundle mBundle = new Bundle();
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personal, null, false);
        usr = (RelativeLayout)v.findViewById(R.id.usr);
        myMsg = (RelativeLayout)v.findViewById(R.id.my_msg);
        finishedEvent = (RelativeLayout)v.findViewById(R.id.finished_event);
        unfinishEvent = (RelativeLayout)v.findViewById(R.id.unfinish_event);
        modifyPwd = (RelativeLayout)v.findViewById(R.id.modify_pwd);
        usrAvatar = (ImageView)v.findViewById(R.id.usr_avatar);
        usrName = (TextView)v.findViewById(R.id.usr_name);
        logout = (Button)v.findViewById(R.id.logout);

        String userName = DBUtil.getConfigValue("real_name");
        usrName.setText(userName);

        myMsg.setOnClickListener(this);
        finishedEvent.setOnClickListener(this);
        unfinishEvent.setOnClickListener(this);
        modifyPwd.setOnClickListener(this);
        logout.setOnClickListener(this);
        return v;
    }

    @Override
    public void onFruitClick(int id) {
        switch (id){
            case R.id.my_msg:
                Intent intent1 = new Intent(getActivity(), MyMsgActivity.class);
                startActivity(intent1);
                break;
            case R.id.finished_event:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent2 = new Intent(getActivity(), FinishedEventActivity.class);
                    startActivity(intent2);
                }else {
                    ToastUtil.showShort(getActivity(), "请先初始化数据");
                }
                break;
            case R.id.unfinish_event:
                if (DBUtil.getConfigValue("initial").equals("1")){
                    Intent intent3 = new Intent(getActivity(), UnfinishEventActivity.class);
                    startActivity(intent3);
                }else{
                    ToastUtil.showShort(getActivity(), "请先初始化数据");
                }
                break;
            case R.id.modify_pwd:
                Intent intent4 = new Intent(getActivity(), ModifyPwdActivity.class);
                startActivity(intent4);
                break;
            case R.id.logout:
                DatabaseHelp.setLoginState(false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
