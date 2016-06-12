package com.fruit.client.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import com.fruit.client.MyApplication;
import com.fruit.client.R;
import com.fruit.client.adapter.ImageGridAdapter;
import com.fruit.client.object.ImageItem;
import com.fruit.client.object.query.Param;
import com.fruit.client.object.query.QueryParamsResponse;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Locationor;
import com.fruit.client.util.Urls;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.cocosw.bottomsheet.BottomSheet;
import com.fruit.common.date.DateUtil;
import com.fruit.common.file.FileUtil;
import com.fruit.common.img.ImageCompassUtil;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.res.DensityUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.templet.NaviActivity;
import com.fruit.core.db.DBUtil;
import com.fruit.core.http.HttpUploadManager;
import com.fruit.core.http.VolleyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/4/27.
 */
public class AddEventActivity extends NaviActivity implements HttpUploadManager.OnUploadProcessListener, MyApplication.OnReceiveData {
    private static final int TASK_QUERY = 0;
    private static final int TASK_ADD_EVENT = 1;
    private static final int TASK_CREATE_BILLNO = 2;
    private static final int TASK_DELETE_EVENT_IMAGE = 3;
    private static final int TASK_GETROUTE = 4;

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_ALBUM = 1;
    private static final String TAG = AddEventActivity.class.getSimpleName();
    private static final String uploadImageUrl = "AJAXReturnData/ImageUpload.ashx";
    private static final String quertParams = "/AJAXReturnData/GetParameter.ashx";

    private TextView billNoText;
    private Spinner rankSpinner;
    private Spinner carNoSpinner;
    private Spinner routeSpinner;
//    private TextView routeCodeText;
    private EditText userNameEt;
    private Spinner directionSpinner;
    private Spinner typeSpinner;
    private Spinner projectSpinner;
    private Spinner projectTypeSpinner;
    private Spinner dealTypeSpinner;
    private EditText countEt;
    private Spinner countUnitSpinner;
    private Spinner dealStatusSpinner;
    private Spinner constructionSpinner;
    private TextView estimateTimeText;
    private ImageView chooseTimeImg;
    private GridView mImageGrid;
    private Button addEventBtn;
    private Button submitEventBtn;
    private TextView mUserName;
    private TextView mUserPhone;
    private CheckBox mIsDeal;
    private EditText mMemo;
    private Spinner mTimeSpinner;

    private GregorianCalendar mCalendar;
    private ArrayList<ImageItem> mImageUrls = new ArrayList<>();
    private ArrayList<String> returnImageUrls = new ArrayList<>();
    private ImageGridAdapter mAdapter;
    private String mCurrentPhotoPath;
    private String mCurrentUploadPhotoPath;
    private boolean isUpload = false;
    private boolean isFromPictureSelect = false;

    private ArrayList<String> mGrades = new ArrayList<>();
    private ArrayList<String> mDirections = new ArrayList<>();
    private ArrayList<String> mRoutes = new ArrayList<>();
    private ArrayList<String> mCarNos = new ArrayList<>();
    private ArrayList<String> mTypes1 = new ArrayList<>();
    private ArrayList<String> mTypes21 = new ArrayList<>();
    private ArrayList<String> mTypes22 = new ArrayList<>();
    private ArrayList<String> mTypes23 = new ArrayList<>();
    private ArrayList<String> mTypes3 = new ArrayList<>();
    private ArrayList<String> mTypes = new ArrayList<>();
    private ArrayList<String> mUnits = new ArrayList<>();
    private ArrayList<String> mProcessTypes = new ArrayList<>();
    private ArrayList<String> mProcessDepts = new ArrayList<>();
    private ArrayList<String> mTimes = new ArrayList<>();
    private boolean isDeal = false;
    private MyApplication myApplication;
    private boolean estimateTimeIsSelected = false;
    private String billNoValue = "";
    private boolean isSubmit = false;
    private boolean isFirstSave = true;
    private double lon;
    private double lat;
    private String roleName;
    private String userName;
    private int deleteImgIndex = -1;
    private String currentAddress;
    private boolean valid = true;
    private boolean hasAddBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("latitude") &&
                intent.hasExtra("longitude") && intent.hasExtra("start_address")){
            lon = intent.getDoubleExtra("longitude", 0);
            lat = intent.getDoubleExtra("latitude", 0);
            currentAddress = intent.getStringExtra("start_address");
        }

        super.onCreate(savedInstanceState);
        initFixedData();
        initData();
        setActivityTitle("添加事件");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        setUseMutiStateView(false);

        billNoText = (TextView)findViewById(R.id.bill_no);
        rankSpinner = (Spinner)findViewById(R.id.spin_rank);
        carNoSpinner = (Spinner) findViewById(R.id.spin_car_no);
        routeSpinner = (Spinner)findViewById(R.id.spin_route);
//        routeCodeText  = (TextView)findViewById(R.id.text_route_code);
        userNameEt = (EditText)findViewById(R.id.et_user_name);
        directionSpinner = (Spinner)findViewById(R.id.spin_direction);
        typeSpinner = (Spinner)findViewById(R.id.spin_type);
        projectSpinner = (Spinner)findViewById(R.id.spin_project);
        projectTypeSpinner = (Spinner)findViewById(R.id.spin_project_type);
        dealTypeSpinner = (Spinner)findViewById(R.id.spin_deal_type);
        countEt = (EditText)findViewById(R.id.et_count);
        countUnitSpinner = (Spinner)findViewById(R.id.spin_count_unit);
        dealStatusSpinner = (Spinner)findViewById(R.id.spin_deal_status);
        constructionSpinner = (Spinner)findViewById(R.id.spin_construction);
        estimateTimeText = (TextView)findViewById(R.id.text_estimate_time);
        chooseTimeImg = (ImageView)findViewById(R.id.img_choose_time);
        addEventBtn = (Button)findViewById(R.id.add_event);
        submitEventBtn = (Button)findViewById(R.id.submit_event);
        mImageGrid = (GridView)findViewById(R.id.grid_view);
        mUserName = (TextView)findViewById(R.id.user_name);
        mUserPhone = (TextView)findViewById(R.id.user_phone);
        mIsDeal = (CheckBox)findViewById(R.id.is_deal);
        mMemo = (EditText)findViewById(R.id.memo);
        mTimeSpinner = (Spinner)findViewById(R.id.spin_time);

        mUserName.setText(DBUtil.getConfigValue("user_name"));
        mUserPhone.setText(DBUtil.getConfigValue("user_phone"));

        Application application = getApplication();
        if (application instanceof MyApplication){
            myApplication = (MyApplication)application;
        }

        chooseTimeImg.setOnClickListener(this);
        addEventBtn.setOnClickListener(this);
        submitEventBtn.setOnClickListener(this);

        mCalendar = new GregorianCalendar();
        mAdapter = new ImageGridAdapter(mImageUrls, this);
        mImageGrid.setAdapter(mAdapter);
        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isUpload){
                    if (position==mImageUrls.size()-1){
                        startPickImage();
                    }else {
                        Intent intent1 = new Intent(AddEventActivity.this, BigImageActivity.class);
                        intent1.putExtra("currentIndex", position);
                        returnImageUrls.clear();
                        for (ImageItem imageItem: mImageUrls){
                            if (imageItem.getImgUrl()!=null && imageItem.getImgUrl().length()>0){
                                returnImageUrls.add(imageItem.getImgUrl());
                            }
                        }
                        intent1.putStringArrayListExtra("imageurls", returnImageUrls);
                        AddEventActivity.this.startActivity(intent1);
                    }
                }else {
                    ToastUtil.showShort(AddEventActivity.this, "等待上传完成");
                }
            }
        });
        mImageGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int finalPosition = position;
                if (!isUpload){
                    if (position==mImageUrls.size()-1){
                    }else {
                        new AlertDialog.Builder(AddEventActivity.this)
                                .setTitle("删除图片")
                                .setMessage("是否删除该图片?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showDialog("正在删除图片");
                                        deleteEventImageRequest(mImageUrls.get(finalPosition).getFilePk(), finalPosition);
                                        deleteImgIndex = finalPosition;
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create()
                                .show();
                    }
                }else {
                    ToastUtil.showShort(AddEventActivity.this, "等待上传完成");
                }
                return true;
            }
        });

        routeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mRoutes));
        rankSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mGrades));
        directionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDirections));
        carNoSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCarNos));
        typeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes1));
        projectSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes21));
        projectTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes3));
        dealTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes));
        countUnitSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUnits));
        dealStatusSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mProcessTypes));
        constructionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mProcessDepts));
        mTimeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTimes));

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTypes21));
                        break;
                    case 1:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTypes22));
                        break;
                    case 2:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTypes23));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) projectSpinner.getSelectedItem();
                queryParam("", name, QueryParamsResponse.class, TASK_QUERY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRouteCode(){
        Log.d("location", "获取到的经纬度："+lon+","+lat);
        Map<String, String> params = new HashMap<>();
        params.put("type", "queryRouteCode");
        params.put("longitude", lon+"");
        params.put("latitude", lat+"");
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.ADD_PILE, Object.class, this, TASK_GETROUTE);
    }

    private void deleteEventImageRequest(String filePk, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "delete");
        params.put("filepk", filePk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT + Urls.EVENT_IMAGE, Object.class,
                this, TASK_DELETE_EVENT_IMAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRouteCode();
        createBillNo();
        if (!hasAddBtn){
            ImageItem imageItem = new ImageItem();
            imageItem.setLast(true);
            mImageUrls.add(imageItem);
            hasAddBtn = true;
        }

    }

    private void initFixedData(){
        mGrades.add("一般");
        mGrades.add("紧急");
        mDirections.add("上行");
        mDirections.add("下行");
        mDirections.add("中间");
        mDirections.add("双向");
        mTypes1.add("路政");
        mTypes1.add("养护");
        mTypes1.add("路网");
        mTimes.add("0:00");

    }

    private void initData(){
        roleName = DBUtil.getConfigValue("role_name");
        userName = DBUtil.getConfigValue("user_name");
        String filePath  = Constant.getStaticParamsDir(this)+Constant.PARAM_NAME;
        String jsonString = FileUtil.readStringFromFile(filePath);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        mRoutes.clear();
        mRoutes.add("空");
        mCarNos.clear();
        mCarNos.add("空");
        mTypes21.clear();
        mTypes21.add("空");
        mTypes22.clear();
        mTypes22.add("空");
        mTypes23.clear();
        mTypes23.add("空");
        mTypes3.clear();
        mTypes3.add("空");
        mTypes.clear();
        mTypes.add("空");
        mUnits.clear();
        mUnits.add("空");
        mProcessTypes.clear();
        mProcessTypes.add("空");
        mProcessDepts.clear();
        mProcessDepts.add("空");
        JSONArray mJSONArray = jsonObject.getJSONArray("路线");
        for (int i=0; i<mJSONArray.size(); i++){
            JSONObject jsonObject1  =mJSONArray.getJSONObject(i);
            mRoutes.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray1 = jsonObject.getJSONArray("车牌号");
        for (int i=0; i<mJSONArray1.size(); i++){
            JSONObject jsonObject1  =mJSONArray1.getJSONObject(i);
            mCarNos.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray2 = jsonObject.getJSONArray("路政项目");
        for (int i=0; i<mJSONArray2.size(); i++){
            JSONObject jsonObject1  =mJSONArray2.getJSONObject(i);
            mTypes21.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray3 = jsonObject.getJSONArray("养护项目");
        for (int i=0; i<mJSONArray3.size(); i++){
            JSONObject jsonObject1  =mJSONArray3.getJSONObject(i);
            mTypes22.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray4 = jsonObject.getJSONArray("路网项目");
        for (int i=0; i<mJSONArray4.size(); i++){
            JSONObject jsonObject1  =mJSONArray4.getJSONObject(i);
            mTypes23.add(jsonObject1.getString("name"));
        }
//        JSONArray mJSONArray5 = jsonObject.getJSONArray("项目类别");
//        for (int i=0; i<mJSONArray3.size(); i++){
//            JSONObject jsonObject1  =mJSONArray5.getJSONObject(i);
//            mTypes3.add(jsonObject1.getString("name"));
//        }
        JSONArray mJSONArray6 = jsonObject.getJSONArray("处理类别");
        for (int i=0; i<mJSONArray6.size(); i++){
            JSONObject jsonObject1  =mJSONArray6.getJSONObject(i);
            mTypes.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray7 = jsonObject.getJSONArray("计量单位");
        for (int i=0; i<mJSONArray7.size(); i++){
            JSONObject jsonObject1  =mJSONArray7.getJSONObject(i);
            mUnits.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray8 = jsonObject.getJSONArray("处理情况");
        for (int i=0; i<mJSONArray8.size(); i++){
            JSONObject jsonObject1  =mJSONArray8.getJSONObject(i);
            mProcessTypes.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray9 = jsonObject.getJSONArray("养护单位");
        for (int i=0; i<mJSONArray9.size(); i++){
            JSONObject jsonObject1  =mJSONArray9.getJSONObject(i);
            mProcessDepts.add(jsonObject1.getString("name"));
        }
    }

    private void queryParam(String tag, String type, Class clz, int taskid)
    {
        String mStr = Urls.ROOT + quertParams + "?type="+ type;
        VolleyManager.newInstance(this).JsonGetRequest(tag, mStr, clz, this, taskid);
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_QUERY:
                if (returnObject!=null){
                    QueryParamsResponse mr = (QueryParamsResponse)returnObject;
                    if (mr.getFlag().equals(Constant.QueryRouteStatus.STATUS_SUCCESS)){
                        ArrayList<Param> mList = mr.getData().getList();
                        mTypes3.clear();
                        mTypes3.add("空");
                        for (Param mParam : mList) {
                            mTypes3.add(mParam.getName());
                        }
                        projectTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes3));
                    }else if (mr.getFlag().equals("0001")){
                        mTypes3.clear();
                        mTypes3.add("空");
                        projectTypeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTypes3));
                    }
                }

                break;
            case TASK_ADD_EVENT:
                if (returnObject!=null){
                    hideProgressDialog();
                    JSONObject mObject = (JSONObject)returnObject;
                    String flag = mObject.getString("flag");
                    String msg = mObject.getString("msg");
                    JSONObject mObject1 = mObject.getJSONObject("data");
                    if (isFirstSave){
                        ToastUtil.showShort(this, "保存为草稿："+msg);
                    }else {
                        if (!isSubmit){
                            ToastUtil.showShort(this, "更新草稿："+msg);
                        }else {
                            ToastUtil.showShort(this, "提交事件：："+msg);
                        }
                    }
                    if (flag.equals("0000")){
                        isFirstSave = false;
                        if (isSubmit){
                            finish();
                        }
                    }
                }
                break;
            case TASK_CREATE_BILLNO:
                if (returnObject!=null){
                    JSONObject mObject = (JSONObject)returnObject;
                    String flag = mObject.getString("flag");
                    String msg = mObject.getString("msg");
                    JSONObject mObject1 = mObject.getJSONObject("data");
//                    ToastUtil.showShort(this, msg);
                    if (flag.equals("0000")){
                        JSONArray jsonArray = mObject1.getJSONArray("result");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        billNoValue = jsonObject.getString("BillNo");
                        billNoText.setText(billNoValue);
                    }
                }
                break;
            case TASK_DELETE_EVENT_IMAGE:
                if(returnObject!=null){
                    hideProgressDialog();
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    ToastUtil.showShort(this, "删除事件图片："+msg);
                    if (flag.equals("0000")){
                        mImageUrls.remove(deleteImgIndex);
                        mAdapter.notifyDataSetChanged();
                        resizeGridHeight();
                    }
                }
                break;
            case TASK_GETROUTE:
                if(returnObject!=null){
                    JSONObject jsonObject  = (JSONObject)returnObject;
                    String flag = (String)jsonObject.getString("flag");
                    Log.d("location", "flag:"+flag);
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        String routeCodeValue = jsonObject2.getString("RouteCode");
                        valid = true;
                        for (int i=0; i<mRoutes.size(); i++){
                            if (mRoutes.get(i).equals(routeCodeValue)){
                                routeSpinner.setSelection(i);
                            }
                        }
//                        routeCodeText.setText(routeCodeValue);
                    }else if (flag.equals("0001")){
                        valid = false;
                        routeSpinner.setSelection(0);
//                        routeCodeText.setText("查无此路线");
//                        ToastUtil.showShort(this, "你无法在此处添加事件");
                    }else {
                        valid = false;
                        routeSpinner.setSelection(0);
//                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加事件");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_QUERY:
                ToastUtil.showShort(this, "无法获取参数");
                break;
            case TASK_ADD_EVENT:
                hideProgressDialog();
                if (isFirstSave){
                    ToastUtil.showShort(this, "保存为草稿失败");
                }else {
                    if (!isSubmit){
                        ToastUtil.showShort(this, "更新草稿失败");
                    }else {
                        ToastUtil.showShort(this, "提交事件失败");
                    }
                }
                break;
            case TASK_CREATE_BILLNO:
                hideProgressDialog();
                ToastUtil.showShort(this, "获取时间编号失败");
                break;
            case TASK_DELETE_EVENT_IMAGE:
                hideProgressDialog();
                ToastUtil.showShort(this, "删除事件图片失败");
                break;
            case TASK_GETROUTE:
                valid = false;
                routeSpinner.setSelection(0);
//                routeCodeText.setText("查无此路线");
                ToastUtil.showShort(this, "你无法在此处添加事件");
                break;
            default:
                break;
        }
    }

    private void startPickImage(){
        new BottomSheet.Builder(this)
                .title("选择获取图片方式")
                .sheet(R.menu.menu_takephoto)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.camera:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                try {
                                    // 指定存放拍摄照片的位置
                                    File f = createImageFile();
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                    startActivityForResult(intent, REQUEST_CAMERA);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case R.id.album:
                                Intent intent1 = new Intent(Intent.ACTION_PICK);
                                intent1.setType("image/*");//相片类型
                                startActivityForResult(intent1, REQUEST_ALBUM);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "qifei_" + timeStamp + ".jpg";

        File image = new File(ImageCompassUtil.getAlbumDir(), imageFileName);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                case REQUEST_CAMERA:
                    // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
//                    ImageCompassUtil.galleryAddPic(getActivity(), mCurrentPhotoPath);
                    Bitmap mBitmap = ImageCompassUtil.getSmallBitmap(mCurrentPhotoPath);
//                    mImageView.setImageBitmap(mBitmap);
                    mImageUrls.add(mImageUrls.size()-1, new ImageItem(mBitmap, 0, null, null));
                    mAdapter.notifyDataSetChanged();
                    isUpload = true;
                    resizeGridHeight();
                    save(mCurrentPhotoPath);
                    startUploadImage(new File(
                            ImageCompassUtil.getAlbumDir(), "small_" + new File(mCurrentPhotoPath).getName()));
                    mCurrentUploadPhotoPath = new File(
                            ImageCompassUtil.getAlbumDir(), "small_" + new File(mCurrentPhotoPath).getName())
                            .getAbsolutePath();
                    break;
                case REQUEST_ALBUM:
                    if (data!=null){
                        Uri mUri = data.getData();
                        if (mUri!=null){
                            String path = FileUtil.getImageAbsolutePath(this, mUri);
//                            mImageView.setImageBitmap(ImageCompassUtil.getSmallBitmap(path));
                            mImageUrls.add(mImageUrls.size()-1, new ImageItem(ImageCompassUtil.getSmallBitmap(path), 0, null, null));
                            mAdapter.notifyDataSetChanged();
                            isUpload = true;
                            resizeGridHeight();
                            save(path);
                            startUploadImage(new File(
                                    ImageCompassUtil.getAlbumDir(), "small_" + new File(path).getName()
                            ));
                            mCurrentUploadPhotoPath = new File(
                                    ImageCompassUtil.getAlbumDir(), "small_"+new File(path).getName()
                            ).getAbsolutePath();
                        }
                    }
                    break;
                default:
                    break;
            }
        }else {
            switch (requestCode){
                case REQUEST_CAMERA:
                    ImageCompassUtil.deleteTempFile(mCurrentPhotoPath);
                    break;
                case REQUEST_ALBUM:

                    break;
                default:
                    break;
            }
        }
    }

    private void startUploadImage(File imgFile) {
        if (imgFile!=null){
            String rooturl = Constant.url+uploadImageUrl;
            String rootUrl = Urls.ROOT+Urls.EVENT_IMAGE;
            String requestUrl = rootUrl;
            requestUrl+="?type=add&";
            requestUrl+="billno="+billNoValue+"&";
            requestUrl+="upuser="+userName+"&";
            requestUrl+="cltype=提交";
            new HttpUploadManager(this).uploadFile(requestUrl, imgFile, "image");
        }
    }

    private void save(String filePath) {
        if (filePath != null) {
            try {
                File f = new File(filePath);
                Bitmap bm = ImageCompassUtil.getSmallBitmap(filePath);
                //图片压缩后保存
                FileOutputStream fos = new FileOutputStream(new File(
                        ImageCompassUtil.getAlbumDir(), "small_" + f.getName()));
                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
            } catch (Exception e) {
                Log.e(TAG, "error", e);
            }
        } else {
            Toast.makeText(this, "请先点击拍照按钮拍摄照片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_event;
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.img_choose_time:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        estimateTimeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                        estimateTimeIsSelected = true;
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String today = sdf.format(date);
                        int in = DateUtil.judgeDate(today, estimateTimeText.getText().toString());
                        if (in==0){
                            String[] ds = new SimpleDateFormat("HH:mm:ss").format(new Date()).split(":");
                            int h = Integer.parseInt(ds[0]);
                            if(Integer.parseInt(ds[1])<30){
                                mTimes.clear();
                                mTimes.add(h+":30");
                                for (int i=h+1; i<24; i++){
                                    mTimes.add(i+":00");
                                    mTimes.add(i+":30");
                                }
                                mTimeSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTimes));
                            }else if (Integer.parseInt(ds[1])<60){
                                mTimes.clear();
                                for (int i=h+1; i<24; i++){
                                    mTimes.add(i+":00");
                                    mTimes.add(i+":30");
                                }
                                mTimeSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTimes));
                            }
                        }else if(in<0){
                            mTimes.clear();
                            for (int i=0; i<24; i++){
                                mTimes.add(i+":00");
                                mTimes.add(i+":30");
                            }
                            mTimeSpinner.setAdapter(new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_list_item_1, mTimes));
                        }else {
                            ToastUtil.showShort(AddEventActivity.this, "你选择的日期不能早于当前时间");
                        }
                    }
                }, mCalendar.get(GregorianCalendar.YEAR), mCalendar.get(GregorianCalendar.MONTH),
                        mCalendar.get(GregorianCalendar.DAY_OF_MONTH)).show();
                break;
            case R.id.add_event:
                if (valid){
                    isSubmit = false;
                    if (isFirstSave){
                        showDialog("正在保存为草稿...");
                        Log.d("android", "添加事件的经纬度是:"+lon+" "+lat);
                        startGenerateParam(lon, lat);
                    }else {
                        showDialog("正在更新草稿...");
                        startGenerateParam(lon, lat);
                    }
                }else{
                    ToastUtil.showShort(this, "你无法在此处添加事件");
                }
                break;
            case R.id.submit_event:
                if (valid){
                    isSubmit = true;
                    if (!isFirstSave){
                        showDialog("正在提交事件...");
                        startGenerateParam(lon, lat);
                    }else {
                        ToastUtil.showShort(this, "先保存再提交");
                    }
                }else {
                    ToastUtil.showShort(this, "你无法在此处添加事件");
                }

                break;
            default:
                break;
        }
    }

    private void addEvent(){
        myApplication.setReceiveDataListener(this);
        Locationor.getInstance(getApplication()).startLocation();
    }

    private void createBillNo(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "createBillNo");
        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params, Urls.ROOT + Urls.ADD_EVENT, Object.class, this, TASK_CREATE_BILLNO);
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        try {
            JSONObject mObject = JSONObject.parseObject(message);
            if (mObject!=null && mObject.containsKey("flag") && mObject.containsKey("msg")){
                String mFlag = mObject.getString("flag");
                String msg = mObject.getString("msg");
                if (responseCode==HttpUploadManager.UPLOAD_SUCCESS_CODE){
                    if (!mFlag.equals(Constant.UploadImageStatus.STATUS_SUCCESS)){
                        mImageUrls.remove(mImageUrls.size()-2);
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showShort(this, "上传图片失败:serverResponseCode:" + mFlag);
                    }else {
                        JSONObject mObject1 = mObject.getJSONObject("data");
                        JSONArray mJSONArray = mObject1.getJSONArray("result");
                        JSONObject mObject2 = mJSONArray.getJSONObject(0);
                        JSONObject mObject3 = mJSONArray.getJSONObject(1);
                        String imgUrl = mObject2.getString("imageUrl");
                        String imgPk = mObject3.getString("FilePK");
                        int index = mImageUrls.size()-2;
                        ImageItem mItem = mImageUrls.get(index);
                        mItem.setUploadStatus(1);
                        mItem.setImgUrl(imgUrl);
                        mItem.setFilePk(imgPk);
                        mImageUrls.set(index, mItem);
                        mAdapter.notifyDataSetChanged();
                        ToastUtil.showShort(this, "上传图片成功");
                    }
                }else {
                    mImageUrls.remove(mImageUrls.size()-2);
                    mAdapter.notifyDataSetChanged();
                    ToastUtil.showShort(this, "上传图片失败:"+msg);
                }
//            mProgressBar.setVisibility(View.GONE);
                ImageCompassUtil.deleteTempFile(mCurrentUploadPhotoPath);
            }
            isUpload = false;
        }catch (Exception e){
            mImageUrls.remove(mImageUrls.size()-2);
            mAdapter.notifyDataSetChanged();
            ToastUtil.showShort(this, "上传图片失败:"+e.getMessage());
            isUpload = false;
        }
    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }

    private void resizeGridHeight(){
        int lines = mImageUrls.size()/4+1;
        WindowManager mManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mMetrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(mMetrics);
        int imageWidth = (mMetrics.widthPixels- DensityUtil.dip2px(this, 64))/4;
        LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mImageGrid.getLayoutParams();
        mParams.height = (imageWidth+DensityUtil.dip2px(this, 8))*lines;
        mImageGrid.setLayoutParams(mParams);
    }

    @Override
    public void onReceive(double longitude, double latitude) {
        if (longitude!=0 && latitude!=0){
//            lon = longitude;
//            lat = latitude;
            Log.d("android", "添加事件的经纬度是:"+longitude+" "+latitude);
            startGenerateParam(longitude, latitude);
        }else {
            hideProgressDialog();
            ToastUtil.showShort(this, "定位当前位置失败");
        }
    }

    private void startGenerateParam(double longitude, double latitude){
        Map<String, String> mParams = new HashMap<>();
        if (isFirstSave){
            mParams.put("type", "add");
        }else {
            if (!isSubmit){
                mParams.put("type", "update");
            }else {
                mParams.put("type", "submit");
            }
        }
        String dateParam = getCurrentData();
        mParams.put("billno", billNoValue);
        mParams.put("billdate", dateParam);
        mParams.put("grade", (String) rankSpinner.getSelectedItem());
        mParams.put("carno", ((String) carNoSpinner.getSelectedItem()).equals("空")?"":((String) carNoSpinner.getSelectedItem()));
//        mParams.put("routecode", routeCodeText.getText().toString().equals("查无此路线")?"":routeCodeText.getText().toString());
        mParams.put("routecode", ((String) routeSpinner.getSelectedItem()).equals("空")?"":((String) routeSpinner.getSelectedItem()));
        mParams.put("username", mUserName.getText().toString());
        mParams.put("userphone", mUserPhone.getText().toString());
        mParams.put("direction", (String) directionSpinner.getSelectedItem());
        mParams.put("lon", longitude+"");
        mParams.put("lat", latitude+"");
        mParams.put("type1", (String) typeSpinner.getSelectedItem());
        mParams.put("type2", ((String) projectSpinner.getSelectedItem()).equals("空")?"":((String) projectSpinner.getSelectedItem()));
        mParams.put("type3", ((String) projectTypeSpinner.getSelectedItem()).equals("空")?"":((String) projectTypeSpinner.getSelectedItem()));
        mParams.put("cltype", ((String) dealTypeSpinner.getSelectedItem()).equals("空")?"":((String) dealTypeSpinner.getSelectedItem()));
        if (countEt.getText().toString().length()>0){
            int value = Integer.parseInt(countEt.getText().toString());
            if (value>0){
                mParams.put("number", value+"");
            }else {
                ToastUtil.showShort(this, "输入的数量不能小于零");
                hideProgressDialog();
                return;
            }
        }else {
            ToastUtil.showShort(this, "请输入数量");
            hideProgressDialog();
            return;
        }
        mParams.put("unit", ((String) countUnitSpinner.getSelectedItem()).equals("空")?"":((String) countUnitSpinner.getSelectedItem()));
        mParams.put("processtype", ((String) dealStatusSpinner.getSelectedItem()).equals("空")?"":((String) dealStatusSpinner.getSelectedItem()));
        mParams.put("processdept", ((String) constructionSpinner.getSelectedItem()).equals("空")?"":((String) constructionSpinner.getSelectedItem()));
        if (estimateTimeIsSelected){
            if (DateUtil.judgeDate(dateParam, estimateTimeText.getText().toString())<=0){
                if (roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)){
                    mParams.put("requendtime", estimateTimeText.getText().toString()+" "+((String)mTimeSpinner.getSelectedItem()));
                }else {
                    if (!((String)mTimeSpinner.getSelectedItem()).equals("0:00")){
                        mParams.put("requendtime", estimateTimeText.getText().toString()+" "+((String)mTimeSpinner.getSelectedItem()));
                    }else {
                        ToastUtil.showShort(this, "请选择精确的预计完成时间");
                        hideProgressDialog();
                        return;
                    }
                }

            }else {
                ToastUtil.showShort(this, "选择的预期完成时间不能早于当前时间");
                hideProgressDialog();
                return;
            }
        }
//        else{
//            ToastUtil.showShort(this, "选择预计完成时间");
//            hideProgressDialog();
//            return;
//        }
        mParams.put("isprocess", mIsDeal.isChecked()?"true":"false");
        mParams.put("memo", mMemo.getText().toString());
        String requestBody = NetWorkUtil.appendParameter(Constant.url, mParams);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, Urls.ROOT + Urls.ADD_EVENT, Object.class, this, TASK_ADD_EVENT);
    }

    private String getCurrentData() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
