package com.fruit.client.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import com.fruit.client.R;
import com.fruit.client.adapter.ImageGridAdapter;
import com.fruit.client.object.ImageItem;
import com.fruit.client.object.event.Event;
import com.fruit.client.object.event.EventListResponse;
import com.fruit.client.object.query.Param;
import com.fruit.client.object.query.QueryParamsResponse;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qianyx on 16-5-9.
 */
public class EventDetailEditActivity extends NaviActivity implements HttpUploadManager.OnUploadProcessListener {
    private static final String quertParams = "/AJAXReturnData/GetParameter.ashx";
    private static final int DRAFT_UPDATE = 0;
    private static final int DRAFT_DELETE = 1;
    private static final int DRAFT_SUBMIT = 2;
    private static final int BUSI_CONFIRM_SUBMIT = 3;
    private static final int BUSI_CONFIRM_RETURN = 4;
    private static final int CONSTR_CONFIRM_SUBMIT = 5;
    private static final int CONSTR_CONFIRM_RETURN = 6;
    private static final int CONSTR_SUBMIT = 7;
    private static final int CONSTR_EXTENT = 8;
    private static final int ACCEPTANCE_SUBMIT = 9;
    private static final int ACCEPTANCE_RETURN = 10;

    private static final int TASK_QUERY = 0;
    private static final int TASK_DETAIL = 1;
    private static final int TASK_ADD_EVENT = 2;
    private static final int TASK_DELETE_EVENT = 3;
    private static final int TASK_CONFIRM_SUBMIT = 4;
    private static final int TASK_CONFIRM_RETURN = 5;
    private static final int TASK_CONSTR_CONFIRM_SUBMIT = 6;
    private static final int TASK_CONSTR_CONFIRM_RETURN  = 7;
    private static final int TASK_CONSTR_SUBMIT = 8;
    private static final int TASK_CONSTR_DELAY = 9;
    private static final int TASK_ACCEPTANCE_SUBMIT = 10;
    private static final int TASK_ACCEPTANCE_RETURN = 11;
    private static final int TASK_GET_IMAGE = 12;
    private static final int TASK_DELETE_EVENT_IMAGE = 13;
    private static final int TASK_SET_MSG_READ = 14;

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_ALBUM = 1;
    private static final String TAG = EventDetailEditActivity.class.getSimpleName();


    private TextView stateText;
    private TextView billNoText;
    private TextView billDateText;
    private Spinner rankSpinner;
    private Spinner carNoSpinner;
    private Spinner routeSpinner;
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
    private TextView mUserName;
    private TextView mUserPhone;
    private CheckBox mIsDeal;
    private EditText mMemo;
    private Spinner timeSpinner;
    private FrameLayout stateActionContainer;

    //草稿状态的控件
    private LinearLayout v1;
    private Button addEventBtn;
    private Button submitEventBtn;
    private Button deleteEventBtn;
    //业务确认状态的控件
    private LinearLayout v2;
    private EditText commentEdit1;
    private Button submitBtn1;
    private Button returnBtn1;
    //施工确认状态的按钮
    private LinearLayout v3;
    private EditText commentEdit2;
    private Button submitBtn2;
    private Button returnBtn2;
    //施工状态的按钮
    private LinearLayout v4;
    private EditText commentEdit3;
    private Button submitBtn3;
    private Button returnBtn3;
    //施工验收状态的按钮
    private LinearLayout v5;
    private EditText commentEdit4;
    private Button submitBtn4;
    private Button returnBtn4;

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

    private String billNoValue;
    private boolean isSubmit;
    private boolean estimateTimeIsSelected;

    private Event event = new Event();
    private double longitude;
    private double latitude;

    private String state;
    private String roleName;
    private String deptName;
    private String userName;

    private ImageGridAdapter imageAdapter;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private String mCurrentPhotoPath;
    private String mCurrentUploadPhotoPath;
    private ArrayList<String> returnImgUrls = new ArrayList<>();
    private boolean isUpload = false;
    private String uploadImageUrl = "AJAXReturnData/ImageUpload.ashx";
    private boolean isFromPictureSelect = false;
    private int delImgIndex = -1;
    private ImageItem addImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("bill_no")){
            billNoValue = intent.getStringExtra("bill_no");
            if (intent.hasExtra("msg_pk")){
                String msgPkValue = intent.getStringExtra("msg_pk");
                setMsgRead(msgPkValue);
            }
        }
        super.onCreate(savedInstanceState);
        initFixedData();
        initData();
        setActivityTitle("我的事件");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        setUseMutiStateView(false);
        stateText = (TextView)findViewById(R.id.state);
        billNoText = (TextView)findViewById(R.id.bill_no);
        billDateText = (TextView)findViewById(R.id.bill_date);
        rankSpinner = (Spinner)findViewById(R.id.spin_rank);
        carNoSpinner = (Spinner) findViewById(R.id.spin_car_no);
        routeSpinner = (Spinner)findViewById(R.id.spin_route);
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
        deleteEventBtn = (Button)findViewById(R.id.delete_event);
        mImageGrid = (GridView)findViewById(R.id.grid_view);
        mUserName = (TextView)findViewById(R.id.user_name);
        mUserPhone = (TextView)findViewById(R.id.user_phone);
        mIsDeal = (CheckBox)findViewById(R.id.is_deal);
        mMemo = (EditText)findViewById(R.id.memo);
        timeSpinner = (Spinner)findViewById(R.id.spin_time);
        stateActionContainer = (FrameLayout)findViewById(R.id.state_action_container);

        //加载所有状态的控件
        v1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_draft_action, null, false);
        addEventBtn = (Button)v1.findViewById(R.id.add_event);
        deleteEventBtn = (Button)v1.findViewById(R.id.delete_event);
        submitEventBtn = (Button)v1.findViewById(R.id.submit_event);
        v2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_business_confirm_action, null, false);
        submitBtn1 = (Button)v2.findViewById(R.id.submit_event);
        returnBtn1 = (Button)v2.findViewById(R.id.return_event);
        commentEdit1 = (EditText)v2.findViewById(R.id.memo);
        v3 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_constr_confirm_action, null, false);
        submitBtn2 = (Button)v3.findViewById(R.id.submit_event);
        returnBtn2 = (Button)v3.findViewById(R.id.return_event);
        commentEdit2 = (EditText)v3.findViewById(R.id.memo);
        v4 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_constr_action, null, false);
        submitBtn3 = (Button)v4.findViewById(R.id.submit_event);
        returnBtn3 = (Button)v4.findViewById(R.id.extent_event);
        commentEdit3 = (EditText)v4.findViewById(R.id.memo);
        v5 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_acceptance_action, null, false);
        submitBtn4 = (Button)v5.findViewById(R.id.submit_event);
        returnBtn4 = (Button)v5.findViewById(R.id.return_event);
        commentEdit4 = (EditText)v5.findViewById(R.id.memo);
        billNoText.setText(billNoValue);
        chooseTimeImg.setOnClickListener(this);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSubmit = false;
                eventPublishUpdateRequest();
            }
        });
        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSubmit = true;
                eventPublisSubmitRequest();
            }
        });
        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventPublishDeleteRequest();
            }
        });
        submitBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSubmitRequest();
            }
        });
        returnBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReturnRequest();
            }
        });
        submitBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constrConfirmSubmitRequest();
            }
        });
        returnBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constrConfirmReturnRequest();
            }
        });
        submitBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constrSubmitRequest();
            }
        });
        returnBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constrDelayRequest();
            }
        });
        submitBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptanceSubmitRequest();
            }
        });
        returnBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptanceReturnRequest();
            }
        });
        imageAdapter = new ImageGridAdapter(imageItems, this);
        mImageGrid.setAdapter(imageAdapter);
        mImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isUpload){
                    if (imageItems.get(position).isLast()){
                        startPickImage();
                    }else {
                        Intent intent1 = new Intent(EventDetailEditActivity.this, BigImageActivity.class);
                        intent1.putExtra("billno", billNoValue);
                        intent1.putExtra("currentIndex", position);
                        EventDetailEditActivity.this.startActivity(intent1);
                    }
                }else {
                    ToastUtil.showShort(EventDetailEditActivity.this, "等待上传完成");
                }
            }
        });
        mImageGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int finalPosition = position;
                if (!isUpload){
                    if (position==imageItems.size()-1){

                    }else {
                        new AlertDialog.Builder(EventDetailEditActivity.this)
                                .setTitle("删除图片")
                                .setMessage("是否删除该图片?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showDialog("正在删除图片");
                                        String filePk = imageItems.get(finalPosition).getFilePk();
                                        deleteEventImageRequest(filePk, finalPosition);
                                        delImgIndex = finalPosition;
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
                    ToastUtil.showShort(EventDetailEditActivity.this, "等待上传完成");
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
        timeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTimes));
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTypes21));
                        setType2Spinner();
                        break;
                    case 1:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTypes22));
                        setType2Spinner();
                        break;
                    case 2:
                        projectSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTypes23));
                        setType2Spinner();
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

    private void setMsgRead(String msgpk){
        Map<String, String> params = new HashMap<>();
        params.put("type", "read");
        params.put("msgpk", msgpk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.USER_MAG, Object.class,
                this, TASK_SET_MSG_READ);
    }

    private void deleteEventImageRequest(String filePk, int position) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "delete");
        params.put("filepk", filePk);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.EVENT_IMAGE, Object.class,
                this, TASK_DELETE_EVENT_IMAGE);
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
                    isFromPictureSelect = true;
                    // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
                    Bitmap mBitmap = ImageCompassUtil.getSmallBitmap(mCurrentPhotoPath);
                    imageItems.remove(imageItems.size()-1);
                    imageItems.add(new ImageItem(mBitmap, 0, null, null));
                    imageItems.add(addImageBtn);
                    imageAdapter.notifyDataSetChanged();
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
                    isFromPictureSelect = true;
                    if (data!=null){
                        Uri mUri = data.getData();
                        if (mUri!=null){
                            String path = FileUtil.getImageAbsolutePath(this, mUri);
                            imageItems.remove(imageItems.size()-1);
                            imageItems.add(new ImageItem(ImageCompassUtil.getSmallBitmap(path), 0, null, null));
                            imageItems.add(addImageBtn);
                            imageAdapter.notifyDataSetChanged();
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
            requestUrl+="billno="+event.getBillNo()+"&";
            requestUrl+="upuser="+userName+"&";
            if (state.equals(Constant.State.DRAFT)){
                requestUrl+="cltype=提交";
            }else if(state.equals(Constant.State.BUSI_CONFIRM)){
                requestUrl+="cltype=业务部门确认";
            }else if(state.equals(Constant.State.CONSTR_CONFIRM)){
                requestUrl+="cltype=施工确认";
            }else if(state.equals(Constant.State.CONSTR)){
                requestUrl+="cltype=施工";
            }else if(state.equals(Constant.State.ACCEPTANCE)){
                requestUrl+="cltype=验收";
            }else {
                return;
            }
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

    private void resizeGridHeight(){
        int lines;
        if (imageItems.size()>0){
            lines = (imageItems.size()-1)/4+1;
        }else {
            lines = 0;
        }
        WindowManager mManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mMetrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(mMetrics);
        int imageWidth = (mMetrics.widthPixels- DensityUtil.dip2px(this, 64))/4;
        LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) mImageGrid.getLayoutParams();
        mParams.height = (imageWidth+DensityUtil.dip2px(this, 8))*lines;
        mImageGrid.setLayoutParams(mParams);
    }

    private void updateDraftView(){
        stateActionContainer.removeAllViews();
        stateActionContainer.addView(v1);
        mImageGrid.setVisibility(View.VISIBLE);
        //通用控件的可用性
        rankSpinner.setEnabled(true);
        carNoSpinner.setEnabled(true);
        routeSpinner.setEnabled(true);
        directionSpinner.setEnabled(true);
        typeSpinner.setEnabled(true);
        projectSpinner.setEnabled(true);
        projectTypeSpinner.setEnabled(true);
        dealTypeSpinner.setEnabled(true);
        countEt.setClickable(true);
        countEt.setEnabled(true);
        countUnitSpinner.setEnabled(true);
        dealStatusSpinner.setEnabled(true);
        constructionSpinner.setEnabled(true);
        timeSpinner.setEnabled(true);
        chooseTimeImg.setClickable(true);
        mIsDeal.setClickable(true);
        mMemo.setClickable(true);
        mMemo.setEnabled(true);
    }

    private void updateBussinessConfirmView(){
        stateActionContainer.removeAllViews();
        stateActionContainer.addView(v2);
        mImageGrid.setVisibility(View.VISIBLE);
        rankSpinner.setEnabled(true);
        carNoSpinner.setEnabled(true);
        routeSpinner.setEnabled(true);
        directionSpinner.setEnabled(true);
        typeSpinner.setEnabled(true);
        projectSpinner.setEnabled(true);
        projectTypeSpinner.setEnabled(true);
        dealTypeSpinner.setEnabled(true);
        countEt.setClickable(true);
        countEt.setEnabled(true);
        countUnitSpinner.setEnabled(true);
        dealStatusSpinner.setEnabled(true);
        constructionSpinner.setEnabled(true);
        timeSpinner.setEnabled(true);
        chooseTimeImg.setClickable(true);
        mIsDeal.setClickable(true);
        mMemo.setClickable(true);
        mMemo.setEnabled(true);
    }

    private void updateConstrConfirmView(){
        stateActionContainer.removeAllViews();
        stateActionContainer.addView(v3);
        mImageGrid.setVisibility(View.VISIBLE);
        rankSpinner.setEnabled(false);
        carNoSpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        directionSpinner.setEnabled(false);
        typeSpinner.setEnabled(false);
        projectSpinner.setEnabled(false);
        projectTypeSpinner.setEnabled(false);
        dealTypeSpinner.setEnabled(false);
        countEt.setClickable(false);
        countEt.setEnabled(false);
        countUnitSpinner.setEnabled(false);
        dealStatusSpinner.setEnabled(false);
        constructionSpinner.setEnabled(false);
        timeSpinner.setEnabled(false);
        chooseTimeImg.setClickable(false);
        mIsDeal.setClickable(false);
        mMemo.setClickable(false);
        mMemo.setEnabled(false);
    }

    private void updateConstrView(){
        stateActionContainer.removeAllViews();
        stateActionContainer.addView(v4);
        mImageGrid.setVisibility(View.VISIBLE);
        rankSpinner.setEnabled(false);
        carNoSpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        directionSpinner.setEnabled(false);
        typeSpinner.setEnabled(false);
        projectSpinner.setEnabled(false);
        projectTypeSpinner.setEnabled(false);
        dealTypeSpinner.setEnabled(false);
        countEt.setClickable(false);
        countEt.setEnabled(false);
        countUnitSpinner.setEnabled(false);
        dealStatusSpinner.setEnabled(false);
        constructionSpinner.setEnabled(false);
        timeSpinner.setEnabled(false);
        chooseTimeImg.setClickable(false);
        mIsDeal.setClickable(false);
        mMemo.setClickable(false);
        mMemo.setEnabled(false);
    }

    private void updateAcceptanceView(){
        stateActionContainer.removeAllViews();
        stateActionContainer.addView(v5);
        mImageGrid.setVisibility(View.VISIBLE);
        rankSpinner.setEnabled(false);
        carNoSpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        directionSpinner.setEnabled(false);
        typeSpinner.setEnabled(false);
        projectSpinner.setEnabled(false);
        projectTypeSpinner.setEnabled(false);
        dealTypeSpinner.setEnabled(false);
        countEt.setClickable(false);
        countEt.setEnabled(false);
        countUnitSpinner.setEnabled(false);
        dealStatusSpinner.setEnabled(false);
        constructionSpinner.setEnabled(false);
        timeSpinner.setEnabled(false);
        chooseTimeImg.setClickable(false);
        mIsDeal.setClickable(false);
        mMemo.setClickable(false);
        mMemo.setEnabled(false);
    }

    private void updateFinishView() {
        stateActionContainer.removeAllViews();
        mImageGrid.setVisibility(View.VISIBLE);
        rankSpinner.setEnabled(false);
        carNoSpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        directionSpinner.setEnabled(false);
        typeSpinner.setEnabled(false);
        projectSpinner.setEnabled(false);
        projectTypeSpinner.setEnabled(false);
        dealTypeSpinner.setEnabled(false);
        countEt.setClickable(false);
        countEt.setEnabled(false);
        countUnitSpinner.setEnabled(false);
        dealStatusSpinner.setEnabled(false);
        constructionSpinner.setEnabled(false);
        timeSpinner.setEnabled(false);
        chooseTimeImg.setClickable(false);
        mIsDeal.setClickable(false);
        mMemo.setClickable(false);
        mMemo.setEnabled(false);
    }

    //将获得的数据显示在通用的视图上
    private void updateGeneralView(){
        //更新数据
        state = event.getState();
        stateText.setText(state);
        mUserName.setText(event.getRealName());
        mUserPhone.setText(event.getUserPhone());
        longitude = Double.parseDouble(event.getLon());
        latitude = Double.parseDouble(event.getLat());
        billDateText.setText(event.getBillDate());
        for (int i=0; i<mGrades.size(); i++) {
            if (mGrades.get(i).equals(event.getGrade())){
                rankSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mCarNos.size(); i++) {
            if (mCarNos.get(i).equals(event.getCarNo())){
                carNoSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mRoutes.size(); i++) {
            if (mRoutes.get(i).equals(event.getRouteNo())){
                routeSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mDirections.size(); i++) {
            if (mDirections.get(i).equals(event.getDirection())){
                directionSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mTypes1.size(); i++) {
            if (mTypes1.get(i).equals(event.getType1())){
                typeSpinner.setSelection(i);
                break;
            }
        }
        setType2Spinner();
        for (int i=0; i<mTypes.size(); i++){
            if (mTypes.get(i).equals(event.getType())){
                dealTypeSpinner.setSelection(i);
                break;
            }
        }
        countEt.setText((int)Double.parseDouble(event.getNumber())+"");
        for (int i=0; i<mUnits.size(); i++){
            if (mUnits.get(i).equals(event.getUnit())){
                countUnitSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mProcessTypes.size(); i++){
            if (mProcessTypes.get(i).equals(event.getProcessType())){
                dealStatusSpinner.setSelection(i);
                break;
            }
        }
        for (int i=0; i<mProcessDepts.size(); i++){
            if (mProcessDepts.get(i).equals(event.getProcessDept())){
                constructionSpinner.setSelection(i);
                break;
            }
        }
        String[] ret = event.getRequEndTime().split(" ");
        if (ret[0].equals("1900/1/1")){
            estimateTimeIsSelected = false;
            estimateTimeText.setText("请选择预计完成时间");
        }else {
            estimateTimeIsSelected = true;
            estimateTimeText.setText(event.getRequEndTime().split(" ")[0]);
            updateHourMinuteSelect();
        }
        String hms = event.getRequEndTime().split(" ")[1];
        String sd = hms.split(":")[0]+":"+hms.split(":")[1];
        for (int i=0; i<mTimes.size(); i++){
            if (mTimes.get(i).equals(sd)){
                timeSpinner.setSelection(i);
                break;
            }
        }
        mIsDeal.setChecked(event.getIsProcess().equals("true")?true:false);
        mMemo.setText(event.getMemo());
    }

    private void updateHourMinuteSelect(){
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
                timeSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTimes));
            }else if (Integer.parseInt(ds[1])<60){
                mTimes.clear();
                for (int i=h+1; i<24; i++){
                    mTimes.add(i+":00");
                    mTimes.add(i+":30");
                }
                timeSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTimes));
            }
        }else if(in<0){
            mTimes.clear();
            for (int i=0; i<24; i++){
                mTimes.add(i+":00");
                mTimes.add(i+":30");
            }
            timeSpinner.setAdapter(new ArrayAdapter<String>(EventDetailEditActivity.this, android.R.layout.simple_list_item_1, mTimes));
        }else {
            ToastUtil.showShort(EventDetailEditActivity.this, "你选择的日期不能早于当前时间");
        }
    }

    /**
     * 检查通用参数的有效性
     * @return
     */
    private boolean checkGeneralValue(){
        if (carNoSpinner.getSelectedItemPosition()==0){
            ToastUtil.showShort(this, "选择巡查车辆");
            return false;
        }
        if (routeSpinner.getSelectedItemPosition()==0){
            ToastUtil.showShort(this, "选择路线编号");
            return false;
        }
        if (projectSpinner.getSelectedItemPosition()==0){
            ToastUtil.showShort(this, "选择一个项目");
            return false;
        }
        if (countEt.getText().toString().length()>0){
            int value = Integer.parseInt(countEt.getText().toString());
            if (value==0){
                ToastUtil.showShort(this, "输入的数量不能小于零");
                return false;
            }
        }else {
            ToastUtil.showShort(this, "请输入数量");
            return false;
        }
        String dateParam = event.getBillDate();
        if(estimateTimeIsSelected){
            if(DateUtil.judgeDate(dateParam, estimateTimeText.getText().toString())>0){
                ToastUtil.showShort(this, "选择的预期完成时间不能早于当前时间");
                return false;
            }
        }
        return true;
    }

    /**
     * 检查参数的有效性
     * @param type 点击的按钮类型
     * @return
     */
    private boolean checkValue(int type){
        if (state.equals(Constant.State.DRAFT)){
            if (type==DRAFT_SUBMIT){
                //如果点击的是提交按钮需要判断哪些是不需要经过业务部门确认的
                if (roleName.equals(Constant.RoleName.SYSTEM_ADMINSTRATIONAR)||
                        (roleName.equals(Constant.RoleName.LUZHENG)&&event.getType1().equals(Constant.RoleName.LUZHENG))||
                        !event.getType1().equals(roleName)){
                    //如果发布该草稿的角色是系统管理员，或者路政发布的是本部门的事件，或者发布事件的部门与事件的类型不一致，
                    //需要业务部门确认,那么某些参数是不需要的
                    if (!checkGeneralValue()){
                        return false;
                    }
                }else {
                    //该条件下的事件不需要业务部门确认
                    if (checkGeneralValue()){
                        if (((String)timeSpinner.getSelectedItem()).equals("0:00")){
                            ToastUtil.showShort(this, "请选择预计完成时间具体时间");
                            return false;
                        }
                        if (((String)constructionSpinner.getSelectedItem()).equals("空")){
                            ToastUtil.showShort(this, "请选择施工单位");
                            return false;
                        }
                    }else {
                        return false;
                    }
                }
            }
        }else if(state.equals(Constant.State.BUSI_CONFIRM)){
            if (checkGeneralValue()){
                if (((String)timeSpinner.getSelectedItem()).equals("0:00")){
                    ToastUtil.showShort(this, "请选择预计完成时间具体时间");
                    return false;
                }
                if (((String)constructionSpinner.getSelectedItem()).equals("空")){
                    ToastUtil.showShort(this, "请选择施工单位");
                    return false;
                }
                if (commentEdit1.getText().toString().length()==0){
                    ToastUtil.showShort(this, "请填写事件确认处理意见");
                    return false;
                }
            }else {
                return false;
            }
        }else if(state.equals(Constant.State.CONSTR_CONFIRM)){
            if (commentEdit2.getText().toString().length()==0){
                ToastUtil.showShort(this, "请填写施工确认处理意见");
                return false;
            }
        }else if(state.equals(Constant.State.CONSTR)){
            if (commentEdit3.getText().toString().length()==0){
                ToastUtil.showShort(this, "请填写施工处理意见");
                return false;
            }
        }else if(state.equals(Constant.State.ACCEPTANCE)){
            if (commentEdit4.getText().toString().length()==0){
                ToastUtil.showShort(this, "请填写施工处理意见");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFromPictureSelect){
            getEventDetail();
        }
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_QUERY:
                ToastUtil.showShort(this, "无法获取参数");
                break;
            case TASK_DETAIL:
                ToastUtil.showShort(this, "无法获取时间详情");
                break;
            case TASK_ADD_EVENT:
                if (!isSubmit){
                    ToastUtil.showShort(this, "无法更新事件");
                }else {
                    ToastUtil.showShort(this, "无法提交事件");
                }
                break;
            case TASK_DELETE_EVENT:
                ToastUtil.showShort(this, "无法删除事件");
                break;
            case TASK_CONFIRM_SUBMIT:
                ToastUtil.showShort(this, "事件确认提交失败");
                break;
            case TASK_CONFIRM_RETURN:
                ToastUtil.showShort(this, "事件确认退回失败");
                break;
            case TASK_CONSTR_CONFIRM_SUBMIT:
                ToastUtil.showShort(this, "施工确认提交失败");
                break;
            case TASK_CONSTR_CONFIRM_RETURN:
                ToastUtil.showShort(this, "施工确认退回失败");
                break;
            case TASK_CONSTR_SUBMIT:
                ToastUtil.showShort(this, "施工申请审核失败");
                break;
            case TASK_CONSTR_DELAY:
                ToastUtil.showShort(this, "施工提申请延期失败");
                break;
            case TASK_ACCEPTANCE_SUBMIT:
                ToastUtil.showShort(this, "竣工验收提交失败");
                break;
            case TASK_ACCEPTANCE_RETURN:
                ToastUtil.showShort(this, "竣工验收退回失败");
                break;
            case TASK_GET_IMAGE:
                ToastUtil.showShort(this, "获取图片失败");
                break;
            case TASK_DELETE_EVENT_IMAGE:
                hideProgressDialog();
                ToastUtil.showShort(this, "删除图片失败");
                break;
            case TASK_SET_MSG_READ:
                ToastUtil.showShort(this, "设置消息为已读失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.img_choose_time:
                String value = estimateTimeText.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date  = null;
                try {
                    date = sdf.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date!=null){
                    int year = date.getYear()+1900;
                    int month = date.getMonth();
                    int day = date.getDate();
                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            estimateTimeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                            estimateTimeIsSelected = true;
                            updateHourMinuteSelect();
                        }
                    }, year, month, day).show();
                }else {
                    int year = new Date().getYear()+1900;
                    int month = new Date().getMonth();
                    int day = new Date().getDate();
                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            estimateTimeText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                            estimateTimeIsSelected = true;
                            updateHourMinuteSelect();
                        }
                    }, year, month, day).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_SET_MSG_READ:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){

                    }else {
                        ToastUtil.showShort(this, "设置消息为已读失败");
                    }
                }
                break;
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
                    for (int i=0; i<mTypes3.size(); i++){
                        if (mTypes3.get(i).equals(event.getType3())){
                            projectTypeSpinner.setSelection(i);
                            break;
                        }
                    }
                }
                break;
            case TASK_DETAIL:
                EventListResponse eventListResponse = (EventListResponse)returnObject;
                if (eventListResponse.getFlag().equals("0000")){
                    //成功返回事件详情
                    Object object = eventListResponse.getData();
                    if (object instanceof JSONObject){
                        JSONObject jsonObject = (JSONObject)object;
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        JSONObject json = jsonArray.getJSONObject(0);
                        //将返回的数据给event对象
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
                        startRequestEventImages();
                        updateGeneralView();
                        if (state.equals(Constant.State.DRAFT)){
                            updateDraftView();
                        }else if(state.equals(Constant.State.BUSI_CONFIRM)){
                            updateBussinessConfirmView();
                        }else if(state.equals(Constant.State.CONSTR_CONFIRM)){
                            updateConstrConfirmView();
                        }else if(state.equals(Constant.State.CONSTR)){
                            updateConstrView();
                        }else if(state.equals(Constant.State.ACCEPTANCE)){
                            updateAcceptanceView();
                        } else {
                            updateFinishView();
                        }
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
                    if (!isSubmit){
                        ToastUtil.showShort(this, "更新草稿："+msg);
                    }else {
                        ToastUtil.showShort(this, "提交事件：："+msg);
                    }
                    if (flag.equals("0000")){
                        if (isSubmit){
                            finish();
                        }
                    }
                }
                break;
            case TASK_DELETE_EVENT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "删除事件："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "删除事件："+msg);
                    }
                }
                break;
            case TASK_CONFIRM_SUBMIT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "事件确认提交："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "事件确认提交："+msg);
                    }
                }
                break;
            case TASK_CONFIRM_RETURN:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "事件确认退回："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "事件确认退回："+msg);
                    }
                }
                break;
            case TASK_CONSTR_CONFIRM_SUBMIT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "施工确认提交："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "施工确认提交："+msg);
                    }
                }
                break;
            case TASK_CONSTR_CONFIRM_RETURN:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "施工确认退回："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "施工确认退回："+msg);
                    }
                }
                break;
            case TASK_CONSTR_SUBMIT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "施工申请审核："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "施工申请审核："+msg);
                    }
                }
                break;
            case TASK_CONSTR_DELAY:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "施工申请延期："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "施工申请延期："+msg);
                    }
                }
                break;
            case TASK_ACCEPTANCE_SUBMIT:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "竣工验收提交："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "竣工验收提交："+msg);
                    }
                }
                break;
            case TASK_ACCEPTANCE_RETURN:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        ToastUtil.showShort(this, "竣工验收退回："+msg);
                        finish();
                    }else {
                        ToastUtil.showShort(this, "竣工验收退回："+msg);
                    }
                }
                break;
            case TASK_GET_IMAGE:
                if(returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    String msg = jsonObject.getString("msg");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        if (jsonArray!=null){
                            imageItems.clear();
                            for(int i=0; i<jsonArray.size(); i++){
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String imgUrl = jsonObject2.getString("Url");
                                String filePk = jsonObject2.getString("filePK");
                                ImageItem imageItem = new ImageItem(null, 1, imgUrl, filePk);
                                imageItems.add(imageItem);
                            }
                            if (state.equals(Constant.State.DRAFT)){
                                imageItems.add(addImageBtn);
                            }else if(state.equals(Constant.State.BUSI_CONFIRM)){
                                imageItems.add(addImageBtn);
                            }else if(state.equals(Constant.State.CONSTR_CONFIRM)){
                                imageItems.add(addImageBtn);
                            }else if(state.equals(Constant.State.CONSTR)){
                                imageItems.add(addImageBtn);
                            }else if(state.equals(Constant.State.ACCEPTANCE)){
                                imageItems.add(addImageBtn);
                            } else {

                            }
                            imageAdapter.notifyDataSetChanged();
                            resizeGridHeight();
                        }
                    }else if (flag.equals("0001")){
                        imageItems.clear();
                        if (state.equals(Constant.State.DRAFT)){
                            imageItems.add(addImageBtn);
                        }else if(state.equals(Constant.State.BUSI_CONFIRM)){
                            imageItems.add(addImageBtn);
                        }else if(state.equals(Constant.State.CONSTR_CONFIRM)){
                            imageItems.add(addImageBtn);
                        }else if(state.equals(Constant.State.CONSTR)){
                            imageItems.add(addImageBtn);
                        }else if(state.equals(Constant.State.ACCEPTANCE)){
                            imageItems.add(addImageBtn);
                        }
                        imageAdapter.notifyDataSetChanged();
                        resizeGridHeight();
                    }else {
                        ToastUtil.showShort(this, "获取事件图片："+msg);
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
                        imageItems.remove(delImgIndex);
                        imageAdapter.notifyDataSetChanged();
                        resizeGridHeight();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startRequestEventImages() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        params.put("billno", event.getBillNo());
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.EVENT_IMAGE,
                Object.class, this, TASK_GET_IMAGE);
    }

    private void setType2Spinner(){
        switch (typeSpinner.getSelectedItemPosition()){
            case 0:
                for (int i=0; i<mTypes21.size(); i++) {
                    if (mTypes21.get(i).equals(event.getType2())){
                        projectSpinner.setSelection(i);
                        String name = (String) projectSpinner.getSelectedItem();
                        queryParam("", name, QueryParamsResponse.class, TASK_QUERY);
                        break;
                    }
                }
                break;
            case 1:
                for (int i=0; i<mTypes22.size(); i++) {
                    if (mTypes22.get(i).equals(event.getType2())){
                        projectSpinner.setSelection(i);
                        String name = (String) projectSpinner.getSelectedItem();
                        queryParam("", name, QueryParamsResponse.class, TASK_QUERY);
                        break;
                    }
                }
                break;
            case 2:
                for (int i=0; i<mTypes23.size(); i++) {
                    if (mTypes23.get(i).equals(event.getType2())){
                        projectSpinner.setSelection(i);
                        String name = (String) projectSpinner.getSelectedItem();
                        queryParam("", name, QueryParamsResponse.class, TASK_QUERY);
                        break;
                    }
                }
                break;
        }
    }

    private void confirmSubmitRequest(){
        if (checkValue(BUSI_CONFIRM_SUBMIT)){
            Map<String, String> mParams = generateConfirmParams("submit");
            String requestBody = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, Urls.ROOT + Urls.EVENT_CONFIRM, Object.class, this, TASK_CONFIRM_SUBMIT);
        }else {
            return;
        }
    }

    private void confirmReturnRequest(){
        if (checkValue(BUSI_CONFIRM_RETURN)){
            Map<String, String> mParams = generateConfirmParams("return");
            String requestBody = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, Urls.ROOT + Urls.EVENT_CONFIRM, Object.class, this, TASK_CONFIRM_RETURN);
        }else{
            return;
        }
    }

    private void constrConfirmSubmitRequest(){
        if (checkValue(CONSTR_CONFIRM_SUBMIT)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "submit");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit2.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.CONSTR_CONFIRM, Object.class, this, TASK_CONSTR_CONFIRM_SUBMIT);
        }else {
            return;
        }
    }

    private void constrConfirmReturnRequest(){
        if (checkValue(CONSTR_CONFIRM_RETURN)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "return");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit2.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.CONSTR_CONFIRM, Object.class, this, TASK_CONSTR_CONFIRM_RETURN);
        }else {
            return;
        }
    }

    private void constrSubmitRequest(){
        if (checkValue(CONSTR_SUBMIT)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "submit");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit3.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.CONSTR, Object.class, this, TASK_CONSTR_SUBMIT);
        }else {
            return;
        }
    }

    private void constrDelayRequest(){
        if (checkValue(CONSTR_EXTENT)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "delay");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit3.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.CONSTR, Object.class, this, TASK_CONSTR_DELAY);
        }else {
            return;
        }
    }

    private void acceptanceSubmitRequest(){
        if (checkValue(ACCEPTANCE_SUBMIT)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "submit");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit4.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.ACCEPTANCE, Object.class, this, TASK_ACCEPTANCE_SUBMIT);
        }else {
            return;
        }
    }

    private void acceptanceReturnRequest(){
        if (checkValue(ACCEPTANCE_RETURN)){
            Map<String, String> mParams = new HashMap<>();
            mParams.put("type", "return");
            mParams.put("billno", event.getBillNo());
            mParams.put("username", userName);
            mParams.put("memo", commentEdit4.getText().toString());
            String requestString = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestString, mParams, Urls.ROOT+Urls.ACCEPTANCE, Object.class, this, TASK_ACCEPTANCE_RETURN);
        }else {
            return;
        }
    }

    private Map<String, String> generateConfirmParams(String type){
        Map<String, String> mParams = new HashMap<>();
        mParams.put("type", type);
        if(type.equals("submit")){
            mParams.put("billno", billNoValue);
        }
        String dateParam = event.getBillDate();
        mParams.put("billdate", dateParam);
        mParams.put("grade", (String) rankSpinner.getSelectedItem());
        mParams.put("carno", ((String) carNoSpinner.getSelectedItem()).equals("空")?"":((String) carNoSpinner.getSelectedItem()));
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
        int value = Integer.parseInt(countEt.getText().toString());
        mParams.put("number", value+"");
        mParams.put("unit", ((String) countUnitSpinner.getSelectedItem()).equals("空")?"":((String) countUnitSpinner.getSelectedItem()));
        mParams.put("processtype", ((String) dealStatusSpinner.getSelectedItem()).equals("空")?"":((String) dealStatusSpinner.getSelectedItem()));
        mParams.put("processdept", ((String) constructionSpinner.getSelectedItem()).equals("空")?"":((String) constructionSpinner.getSelectedItem()));
        mParams.put("requendtime", estimateTimeText.getText().toString()+" "+((String)timeSpinner.getSelectedItem()));
        mParams.put("isprocess", mIsDeal.isChecked()?"true":"false");
        mParams.put("memo", commentEdit1.getText().toString());
        return mParams;
    }

    private void eventPublishDeleteRequest(){
        if (checkValue(DRAFT_DELETE)){
            Map<String, String> params = new HashMap<>();
            params.put("type", "delete");
            params.put("billno", event.getBillNo());
            String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params, Urls.ROOT+Urls.ADD_EVENT,
                    Object.class, this, TASK_DELETE_EVENT);
        }else {
            return;
        }
    }

    private void eventPublisSubmitRequest(){
        if (checkValue(DRAFT_SUBMIT)){
            Map<String, String> mParams = generateEventPublishParams("submit");
            String requestBody = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, Urls.ROOT + Urls.ADD_EVENT, Object.class, this, TASK_ADD_EVENT);
        }else {
            return;
        }
    }

    private void eventPublishUpdateRequest(){
        if (checkValue(DRAFT_UPDATE)){
            Map<String, String> mParams = generateEventPublishParams("update");
            String requestBody = NetWorkUtil.appendParameter(Constant.url, mParams);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, mParams, Urls.ROOT + Urls.ADD_EVENT, Object.class, this, TASK_ADD_EVENT);
        }else {
            return;
        }
    }

    private Map<String, String> generateEventPublishParams(String type){
        Map<String, String> mParams = new HashMap<>();
        mParams.put("type", type);
        String dateParam = event.getBillDate();
        mParams.put("billno", billNoValue);
        mParams.put("billdate", dateParam);
        mParams.put("grade", (String) rankSpinner.getSelectedItem());
        mParams.put("carno", ((String) carNoSpinner.getSelectedItem()).equals("空")?"":((String) carNoSpinner.getSelectedItem()));
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
        int value = Integer.parseInt(countEt.getText().toString());
        mParams.put("number", value+"");
        mParams.put("unit", ((String) countUnitSpinner.getSelectedItem()).equals("空")?"":((String) countUnitSpinner.getSelectedItem()));
        mParams.put("processtype", ((String) dealStatusSpinner.getSelectedItem()).equals("空")?"":((String) dealStatusSpinner.getSelectedItem()));
        mParams.put("processdept", ((String) constructionSpinner.getSelectedItem()).equals("空")?"":((String) constructionSpinner.getSelectedItem()));
        if (estimateTimeIsSelected){
            mParams.put("requendtime", estimateTimeText.getText().toString());
        }else {
            mParams.put("requendtime", "");
        }
        mParams.put("isprocess", mIsDeal.isChecked()?"true":"false");
        mParams.put("memo", mMemo.getText().toString());
        return mParams;
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
        for (int i=8; i<19; i++){
            mTimes.add(i+":00");
            mTimes.add(i+":30");
        }
        mTimes.add("20:00");
        addImageBtn = new ImageItem();
        addImageBtn.setLast(true);
//        imageItems.add(addImageBtn);
    }

    private void initData(){
        roleName = DBUtil.getConfigValue("role_name");
        deptName = DBUtil.getConfigValue("dept_name");
        userName = DBUtil.getConfigValue("user_name");

        String filePath  = Constant.getStaticParamsDir(this)+Constant.PARAM_NAME;
        if (new File(filePath).exists()){
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
        }else {
            ToastUtil.showShort(this, "请先初始化数据");
            finish();
        }
    }

    private void queryParam(String tag, String type, Class clz, int taskid) {
        String mStr = Urls.ROOT + quertParams + "?type="+ type;
        VolleyManager.newInstance(this).JsonGetRequest(tag, mStr, clz, this, taskid);
    }

    private void getEventDetail(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        params.put("billno", billNoValue);
        String requestBody = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params,
                Urls.ROOT+Urls.EVENT_QUERY, EventListResponse.class, this, TASK_DETAIL);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_detail_edit;
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
                        imageItems.remove(imageItems.size()-2);
                        imageAdapter.notifyDataSetChanged();
                        ToastUtil.showShort(this, "上传图片失败:" + mFlag);
                    }else {
                        JSONObject mObject1 = mObject.getJSONObject("data");
                        JSONArray mJSONArray = mObject1.getJSONArray("result");
                        JSONObject mObject2 = mJSONArray.getJSONObject(0);
                        JSONObject mObject3 = mJSONArray.getJSONObject(1);
                        String imgUrl = mObject2.getString("imageUrl");
                        String imgPk = mObject3.getString("FilePK");
                        int index = imageItems.size()-2;
                        ImageItem mItem = imageItems.get(index);
                        mItem.setUploadStatus(1);
                        mItem.setImgUrl(imgUrl);
                        mItem.setFilePk(imgPk);
                        imageItems.set(index, mItem);
                        imageAdapter.notifyDataSetChanged();
                        ToastUtil.showShort(this, "上传图片成功");
                    }
                }else {
                    imageItems.remove(imageItems.size()-2);
                    imageAdapter.notifyDataSetChanged();
                    ToastUtil.showShort(this, "上传图片失败:"+msg);
                }
                ImageCompassUtil.deleteTempFile(mCurrentUploadPhotoPath);
            }
            isUpload = false;
        }catch (Exception e){
            imageItems.remove(imageItems.size()-2);
            imageAdapter.notifyDataSetChanged();
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
}
