package com.fruit.client.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by user on 2016/2/26.
 */
public class Constant {
    public static final String url = "http://www.51anys.com:91/";

    public class LoginStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_ACCOUNT_PWD_WRONG = "0001";
        public static final String STATUS_UNACTIVED = "0002";
        public static final String STATUS_REFUSED = "0003";
    }

    public class QueryRouteStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_FAIL = "0001";
    }

    public class ManagerPileStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_FAIL = "0001";
        public static final String STATUS_PARAM_WRONG = "0002";
        public static final String STATUS_DATA_EXIST = "0003";
    }

    public class ManagerFacilityStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_FAIL = "0001";
        public static final String STATUS_PARAM_ERROR = "0002";
    }

    public class ManagerOtherStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_FAIL = "0001";
        public static final String STATUS_PARAM_ERROR = "0002";
    }

    public class UploadImageStatus{
        public static final String STATUS_SUCCESS = "0000";
        public static final String STATUS_FAIL = "0001";
    }

    public class AddType{
        public static final int TYPE_PILE = 0;
        public static final int TYPE_MARK = 1;
        public static final int TYPE_FENCE = 2;
        public static final int TYPE_OTHER = 3;
        public static final int TYPE_EVENT = 4;
    }

    public class RoleName{
        public static final String SYSTEM_ADMINSTRATIONAR = "系统管理员";
        public static final String LUZHENG_CONFIRM = "路政确认";
        public static final String LUZHENG = "路政";
        public static final String LUWANG = "路网";
        public static final String YANGHU  = "养护";
        public static final String OTHER = "其他";
        public static final String CONSTRUCTION = "施工";
    }

    public class State{
        public static final String BUSI_CONFIRM = "业务部门确认";
        public static final String DRAFT = "草稿";
        public static final String CONSTR_CONFIRM = "施工确认";
        public static final String CONSTR = "施工";
        public static final String ACCEPTANCE = "竣工验收";
    }

    public static final String PARAM_NAME = "params.txt";

    public static String getStaticParamsDir(Context context){
        String s = context.getFilesDir().getAbsolutePath()+ File.separator;
        Log.d("filepath", s);
//        s = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
        return s;
    }

    public static boolean hasParamsFile(Context context){
        File file = new File(getStaticParamsDir(context)+PARAM_NAME);
        return file.exists();
    }
}
