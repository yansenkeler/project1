package com.fruit.client.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import com.fruit.client.R;
import com.fruit.client.util.Constant;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.cocosw.bottomsheet.BottomSheet;
import com.fruit.client.util.Urls;
import com.fruit.common.file.FileUtil;
import com.fruit.common.img.ImageCompassUtil;
import com.fruit.common.network.NetWorkUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/2/29.
 */
public class AddMarkActivity extends NaviActivity implements HttpUploadManager.OnUploadProcessListener{
    public static final String TAG = AddMarkActivity.class.getSimpleName();
    private static final String uploadImageUrl = "AJAXReturnData/ImageUpload.ashx";
    private static final String managerFacilityUrl = "AJAXReturnData/Mark.ashx";
    private static final int REQUEST_ALBUM = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int TASK_ADD_FACILITY = 0;
    private static final int TASK_GETROUTE = 1;

    private Spinner mSingalSpinner, mLocationSpinner, mSupportMethodSpinner;
    private EditText pageSizeEt, baseSizeEt, postEt, descriptionEt;
    private TextView mAdd;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private TextView currentAddress;
    private TextView routeCodeText;

    private ArrayList<String> mRoutes = new ArrayList<>();
    private ArrayList<String> mLocations = new ArrayList<>();
    private ArrayList<String> mSingals = new ArrayList<>();
    private ArrayList<String> mSupportMethods = new ArrayList<>();
    private String paramPagesize, paramBasesize, paramPost, paramDescription;
    private String currentAddressString;

    private double latitude, longitude;

    //当前上传图片的总大小
    private int currentFileSize;
    //单个图片上传返回字符串
    private String imageUrl;
    private String mCurrentPhotoPath;
    private String mCurrentUploadPhotoPath;
    private ScrollView mScrollView;

    private boolean valid = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("latitude") && intent.hasExtra("longitude") &&
                intent.hasExtra("currentAddress")){
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
        setActivityTitle("添加设施");
        setLeftImageView(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_48dp));

        routeCodeText = (TextView)findViewById(R.id.text_route_code);
//        mRouteSpinner = (Spinner)findViewById(R.id.spinner_routine);
        mSingalSpinner = (Spinner)findViewById(R.id.spinner_singal);
        mLocationSpinner = (Spinner)findViewById(R.id.spinner_location);
        mSupportMethodSpinner = (Spinner)findViewById(R.id.spinner_support_method);
        pageSizeEt = (EditText)findViewById(R.id.edittext_pagesize);
        baseSizeEt = (EditText)findViewById(R.id.edittext_basesize);
        postEt = (EditText)findViewById(R.id.edittext_post);
        descriptionEt = (EditText)findViewById(R.id.edittext_description);
        mAdd = (TextView)findViewById(R.id.textview_add);
        mImageView = (ImageView)findViewById(R.id.image);
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        mScrollView = (ScrollView)findViewById(R.id.root);
        currentAddress = (TextView)findViewById(R.id.currentAddress);

        currentAddress.setText("当前位置："+currentAddressString);

        mImageView.setOnClickListener(this);
        mAdd.setOnClickListener(this);

//        mRouteSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mRoutes));
        mSingalSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSingals));
        mLocationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLocations));
        mSupportMethodSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSupportMethods));

        String lastFacilityRoute = DBUtil.getConfigValue("LastFacilityRoute");
//        if (lastFacilityRoute!=null && lastFacilityRoute.length()>0){
//            for (int i=0; i<mRoutes.size(); i++){
//                if (mRoutes.get(i).equals(lastFacilityRoute)){
//                    mRouteSpinner.setSelection(i);
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
        String filePath  = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"params.txt";
        String jsonString = FileUtil.readStringFromFile(filePath);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONArray mJSONArray = jsonObject.getJSONArray("路线");
        mRoutes.clear();
        mSupportMethods.clear();
        mLocations.clear();
        mSingals.clear();
        for (int i=0; i<mJSONArray.size(); i++){
            JSONObject jsonObject1  =mJSONArray.getJSONObject(i);
            mRoutes.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray1 = jsonObject.getJSONArray("支持方式");
        for (int i=0; i<mJSONArray1.size(); i++){
            JSONObject jsonObject1  =mJSONArray1.getJSONObject(i);
            mSupportMethods.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray2 = jsonObject.getJSONArray("位置");
        for (int i=0; i<mJSONArray2.size(); i++){
            JSONObject jsonObject1  =mJSONArray2.getJSONObject(i);
            mLocations.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray3 = jsonObject.getJSONArray("标志");
        for (int i=0; i<mJSONArray3.size(); i++){
            JSONObject jsonObject1  =mJSONArray3.getJSONObject(i);
            mSingals.add(jsonObject1.getString("name"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_mark;
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

    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 企飞 文件夹中
     * 照片的命名规则为：qifei_20130125_173729.jpg
     *
     * @return
     * @throws IOException
     */
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
                    mImageView.setImageBitmap(mBitmap);
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
                            mImageView.setImageBitmap(ImageCompassUtil.getSmallBitmap(path));
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
    public void onResponse(Object response, int taskid) {
        super.onResponse(response, taskid);
        switch (taskid){
            case TASK_ADD_FACILITY:
//                DBUtil.setConfigValue("LastFacilityRoute", (String) mRouteSpinner.getSelectedItem());
                hideProgressDialog();
                JSONObject mObject = (JSONObject)response;
                String flag = mObject.getString("flag");
                String msg = mObject.getString("msg");
                JSONObject mObject1 = mObject.getJSONObject("data");
                ToastUtil.showShort(this, msg);
                setResult(RESULT_OK);
                finish();
                break;
            case TASK_GETROUTE:
                if(response!=null){
                    JSONObject jsonObject  = (JSONObject)response;
                    String flag1 = (String)jsonObject.getString("flag");
                    if (flag1.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        String routeCodeValue = jsonObject2.getString("RouteCode");
                        valid = true;
                        routeCodeText.setText(routeCodeValue);
                    }else if (flag1.equals("0001")){
                        valid = false;
                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加标志");
                    }else {
                        valid = false;
                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加标志");
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
            case TASK_ADD_FACILITY:
                hideProgressDialog();
                ToastUtil.showShort(this, "添加失败");
                break;
            case TASK_GETROUTE:
                valid = false;
                routeCodeText.setText("查无此路线");
                ToastUtil.showShort(this, "你无法在此处添加标志");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFruitActivityClick(View view) {
        switch (view.getId()){
            case R.id.textview_add:
                if (valid){
                    addMarkFacility();
                }else {
                    ToastUtil.showShort(this, "你无法在此处添加标志");
                }
                break;
            case R.id.image:
                startPickImage();
                break;
            default:
                break;
        }
    }

    private void startUploadImage(File imgFile)
    {
        if (imgFile!=null){
            new HttpUploadManager(this).uploadFile(Constant.url+uploadImageUrl, imgFile, "image");
        }
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        JSONObject mObject = JSONObject.parseObject(message);
        if (mObject!=null && mObject.containsKey("flag") && mObject.containsKey("msg")){
            String mFlag = mObject.getString("flag");
            String msg = mObject.getString("msg");
            if (responseCode==HttpUploadManager.UPLOAD_SUCCESS_CODE){
                if (!mFlag.equals(Constant.UploadImageStatus.STATUS_SUCCESS)){
                    ToastUtil.showShort(this, "上传图片失败:serverResponseCode:"+mFlag);
                }else {
                    JSONObject mObject1 = mObject.getJSONObject("data");
                    JSONArray mJSONArray = mObject1.getJSONArray("list");
                    for (int i=0; i<mJSONArray.size(); i++){
                        JSONObject mObject2 = mJSONArray.getJSONObject(i);
                        String imgUrl = mObject2.getString("imageUrl");
                        imageUrl = imgUrl;
                    }
                    ToastUtil.showShort(this, "上传图片成功");
                }
            }else {
                ToastUtil.showShort(this, "上传图片失败:"+msg);
            }
            mProgressBar.setVisibility(View.GONE);
            ImageCompassUtil.deleteTempFile(mCurrentUploadPhotoPath);
        }
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        int progress = uploadSize/ (currentFileSize/100);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(progress);
    }

    @Override
    public void initUpload(int fileSize) {
        currentFileSize = fileSize;
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        Log.d("upload size", "upload file size:" + fileSize + "byte");
    }

    private void addMarkFacility()
    {
        if (checkParam()){
            if (!isFinishing()){
                showDialog("正在添加设施...");
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "add");
//            params.put("routecode", (String)mRouteSpinner.getSelectedItem());
            params.put("routecode", routeCodeText.getText().toString().equals("查无此路线")?"":routeCodeText.getText().toString());
            params.put("facilityname", (String)mSingalSpinner.getSelectedItem());
            params.put("location", (String)mLocationSpinner.getSelectedItem());
            params.put("supporttype", (String)mSupportMethodSpinner.getSelectedItem());
            params.put("pagesize", paramPagesize);
            params.put("basesize", paramBasesize);
            params.put("post", paramPost);
            params.put("longitude", longitude+"");
            params.put("latitude", latitude+"");
            params.put("description", paramDescription);
            params.put("imagename", imageUrl);
            String requestBody = NetWorkUtil.appendParameter(Constant.url+managerFacilityUrl, params);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params, Constant.url + managerFacilityUrl, Object.class, this, TASK_ADD_FACILITY);
        }
    }



    private boolean checkParam()
    {
        boolean result = true;
        paramPagesize = pageSizeEt.getText().toString();
        paramBasesize = baseSizeEt.getText().toString();
        paramPost = postEt.getText().toString();
        paramDescription = descriptionEt.getText().toString();
//        if (imageUrl==null || imageUrl.length()==0){
//            ToastUtil.showShort(getActivity(), "没有上传图片");
//            result = false;
//        }
        if (latitude==0 || longitude==0){
            ToastUtil.showShort(this, "无法获得当前位置经纬度");
            return  false;
        }
        return result;
    }
}
