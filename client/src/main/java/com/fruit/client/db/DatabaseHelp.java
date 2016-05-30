package com.fruit.client.db;

import com.fruit.core.db.DBUtil;

/**
 * Created by Keler Qian on 2016/2/25.
 */
public class DatabaseHelp {
    private static final String STATE_LOGIN = "state_login";
    private static final String STATE_OFFLINE = "state_offline";
    private static final String STATE_REMEMBER_PASSWORD = "state_remember_password";
    private static final String STATE_FORGET_PASSWORD = "state_forget_password";

    private static final String TAG_LOGIN_STATE = "tag_login_state";
    private static final String TAG_REMEMBER_PASSWORD = "tag_remember_password";

    /**
     * @param isLogin 登录状态 true:login;false:offline
     */
    public static void setLoginState(boolean isLogin){
        if (isLogin){
            DBUtil.setConfigValue(TAG_LOGIN_STATE, STATE_LOGIN);
        }else {
            DBUtil.setConfigValue(TAG_LOGIN_STATE, STATE_OFFLINE);
        }
    }

    /**
     *
     * @return 是否登录
     */
    public static boolean isLogin(){
        String string = DBUtil.getConfigValue(TAG_LOGIN_STATE);
        if (string!=null && string.equals(STATE_LOGIN)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 设置是否记住密码
     * @param rememPassword
     */
    public static void setRememPassword(boolean rememPassword){
        if (rememPassword){
            DBUtil.setConfigValue(TAG_REMEMBER_PASSWORD, STATE_REMEMBER_PASSWORD);
        }else {
            DBUtil.setConfigValue(TAG_REMEMBER_PASSWORD, STATE_FORGET_PASSWORD);
        }
    }

    /**
     * 是否记住密码
     * @return
     */
    public static boolean isRememberPassword(){
        String string =DBUtil.getConfigValue(TAG_REMEMBER_PASSWORD);
        if (string!=null && string.equals(STATE_REMEMBER_PASSWORD)){
            return true;
        }else {
            return false;
        }
    }

}
