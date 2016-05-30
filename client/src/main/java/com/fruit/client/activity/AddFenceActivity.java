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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by user on 2016/4/23.
 */
public class AddFenceActivity extends NaviActivity implements HttpUploadManager.OnUploadProcessListener {
    private static final String uploadImageUrl = "AJAXReturnData/ImageUpload.ashx";
    private static final String addFenceUrl = "AJAXReturnData/Fence.ashx";
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_ALBUM = 1;
    private static final int REQUEST_DESTINATION = 2;
    private static final String TAG = AddFenceActivity.class.getSimpleName();
    private static final int TASK_ADD_FACILITY = 0;
    private static final int TASK_GETROUTE = 1;


    private TextView startAddress, endAddress;
    private Spinner nameSpinner, locationSpinner, typeSpinner;
    private EditText length, desc;
    private ImageView image;
    private TextView selectDestination, submit;
    private TextView routeCodeText;

    private double startLat, startLon, endLat, endLon;
    private String startAddressValue, endAddressValue;
    private ArrayList<String> mRoutes = new ArrayList<>();
    private ArrayList<String> mLocations = new ArrayList<>();
    private ArrayList<String> mFences = new ArrayList<>();
    private ArrayList<String> mFenceTypes = new ArrayList<>();
    private String lengthValue, descValue;
    private String imagePath1, imagePath2, returnImagePath;

    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("latitude") &&
                intent.hasExtra("longitude") && intent.hasExtra("start_address")){
            startLat = intent.getDoubleExtra("latitude", 0);
            startLon = intent.getDoubleExtra("longitude", 0);
            startAddressValue = intent.getStringExtra("start_address");
        }
        super.onCreate(savedInstanceState);
        setUseMutiStateView(false);
        initData();
        setActivityTitle("添加护栏");
        setTopbarBackground(getResources().getColor(R.color.colorPrimary));
        setLeftImageView(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_48dp));
        startAddress = (TextView)findViewById(R.id.start_address);
        endAddress = (TextView)findViewById(R.id.end_address);
        routeCodeText = (TextView)findViewById(R.id.text_route_code);
//        routeSpinner = (Spinner)findViewById(R.id.spinner_routine);
        nameSpinner = (Spinner)findViewById(R.id.spinner_fence);
        locationSpinner = (Spinner)findViewById(R.id.spinner_location);
        typeSpinner = (Spinner)findViewById(R.id.spinner_fence_type);
        length = (EditText)findViewById(R.id.fence_length);
        desc = (EditText)findViewById(R.id.fence_desc);
        image = (ImageView)findViewById(R.id.image);
        selectDestination = (TextView)findViewById(R.id.select_destination);
        submit = (TextView)findViewById(R.id.submit);

        startAddress.setText("起点位置："+startAddressValue);
        endAddress.setText("终点位置：");

        image.setOnClickListener(this);
        selectDestination.setOnClickListener(this);
        submit.setOnClickListener(this);

//        routeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mRoutes));
        nameSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFences));
        locationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mLocations));
        typeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFenceTypes));

        String lastFacilityRoute = DBUtil.getConfigValue("LastFenceRoute");
//        if (lastFacilityRoute!=null && lastFacilityRoute.length()>0){
//            for (int i=0; i<mRoutes.size(); i++){
//                if (mRoutes.get(i).equals(lastFacilityRoute)){
//                    routeSpinner.setSelection(i);
//                    break;
//                }
//            }
//        }
    }

    private void getRouteCode(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "queryRouteCode");
        params.put("longitude", startLon+"");
        params.put("latitude", startLat+"");
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.ADD_PILE, Object.class, this, TASK_GETROUTE);
    }

    private void initData(){
        String filePath  = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"params.txt";
        String jsonString = FileUtil.readStringFromFile(filePath);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONArray mJSONArray = jsonObject.getJSONArray("路线");
        mRoutes.clear();
        mFenceTypes.clear();
        mLocations.clear();
        mFences.clear();
        for (int i=0; i<mJSONArray.size(); i++){
            JSONObject jsonObject1  =mJSONArray.getJSONObject(i);
            mRoutes.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray1 = jsonObject.getJSONArray("护栏");
        for (int i=0; i<mJSONArray1.size(); i++){
            JSONObject jsonObject1  =mJSONArray1.getJSONObject(i);
            mFences.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray2 = jsonObject.getJSONArray("位置");
        for (int i=0; i<mJSONArray2.size(); i++){
            JSONObject jsonObject1  =mJSONArray2.getJSONObject(i);
            mLocations.add(jsonObject1.getString("name"));
        }
        JSONArray mJSONArray3 = jsonObject.getJSONArray("护栏类型");
        for (int i=0; i<mJSONArray3.size(); i++){
            JSONObject jsonObject1  =mJSONArray3.getJSONObject(i);
            mFenceTypes.add(jsonObject1.getString("name"));
        }
    }

    @Override
    public void leftClick() {
        super.leftClick();
        onBackPressed();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_fence;
    }

    @Override
    public void onFruitActivityClick(View view) {
        super.onFruitActivityClick(view);
        switch (view.getId()){
            case R.id.image:
                startPickImage();
                break;
            case R.id.select_destination:
                Intent intent = new Intent(AddFenceActivity.this, Location2Activity.class);
                startActivityForResult(intent, REQUEST_DESTINATION);
                break;
            case R.id.submit:
                if (valid){
                    addFence();
                }else{
                    ToastUtil.showShort(this, "你无法在此处添加栏杆");
                }

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
        imagePath1 = image.getAbsolutePath();
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
                    Bitmap mBitmap = ImageCompassUtil.getSmallBitmap(imagePath1);
                    image.setImageBitmap(mBitmap);
                    save(imagePath1);
                    File file = new File(
                            ImageCompassUtil.getAlbumDir(), "small_" + new File(imagePath1).getName());
                    imagePath2 = file.getAbsolutePath();
                    startUploadImage(file);
                    break;
                case REQUEST_ALBUM:
                    if (data!=null){
                        Uri mUri = data.getData();
                        if (mUri!=null){
                            String path = FileUtil.getImageAbsolutePath(this, mUri);
                            image.setImageBitmap(ImageCompassUtil.getSmallBitmap(path));
                            save(path);
                            File file1 = new File(
                                    ImageCompassUtil.getAlbumDir(), "small_" + new File(path).getName());
                            imagePath2 = file1.getAbsolutePath();
                            startUploadImage(file1);
                        }
                    }
                    break;
                case REQUEST_DESTINATION:
                    if (data!=null && data.hasExtra("latitude")
                            && data.hasExtra("longitude") && data.hasExtra("end_address")){
                        endLat = data.getDoubleExtra("latitude", 0);
                        endLon = data.getDoubleExtra("longitude", 0);
                        endAddressValue = data.getStringExtra("end_address");
                        endAddress.setText("终点位置："+endAddressValue);
                    }else {
                        ToastUtil.showShort(AddFenceActivity.this, "无法获得终点位置");
                    }
                default:
                    break;
            }
        }else {
            switch (requestCode){
                case REQUEST_CAMERA:
                    ImageCompassUtil.deleteTempFile(imagePath1);
                    break;
                case REQUEST_ALBUM:

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRouteCode();
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

    private void startUploadImage(File imgFile)
    {
        if (imgFile!=null){
            new HttpUploadManager(this).uploadFile(Constant.url + uploadImageUrl, imgFile, "image");
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
                    ToastUtil.showShort(this, "上传图片失败:serverResponseCode:" + mFlag);
                }else {
                    JSONObject mObject1 = mObject.getJSONObject("data");
                    JSONArray mJSONArray = mObject1.getJSONArray("list");
                    for (int i=0; i<mJSONArray.size(); i++){
                        JSONObject mObject2 = mJSONArray.getJSONObject(i);
                        String imgUrl = mObject2.getString("imageUrl");
                        returnImagePath = imgUrl;
                    }
                    ToastUtil.showShort(this, "上传图片成功");
                }
            }else {
                ToastUtil.showShort(this, "上传图片失败:"+msg);
            }
            ImageCompassUtil.deleteTempFile(imagePath2);
        }
    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }

    private boolean checkParam()
    {
        boolean result = true;
        lengthValue = length.getText().toString();
        descValue = desc.getText().toString();
        if (startLat==0 || startLon==0){
            ToastUtil.showShort(this, "无法获得起点经纬度");
            return  false;
        }else if (endLat==0 || endLon==0){
            ToastUtil.showShort(this, "无法获得终点经纬度");
            return false;
        }
        return result;
    }

    private void addFence()
    {
        if (checkParam()){
            if (!isFinishing()){
                showDialog("正在添加护栏设施...");
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "add");
            params.put("routecode", routeCodeText.getText().toString().equals("查无此路线")?"":routeCodeText.getText().toString());
            params.put("facilityname", (String)nameSpinner.getSelectedItem());
            params.put("location", (String)locationSpinner.getSelectedItem());
            params.put("fencetype", (String)typeSpinner.getSelectedItem());
            params.put("length", lengthValue);
            params.put("longitude", startLon+"");
            params.put("latitude", startLat+"");
            params.put("longitude2", endLon+"");
            params.put("latitude2", endLat+"");
            params.put("description", descValue);
            params.put("imagename", returnImagePath);
            String requestBody = NetWorkUtil.appendParameter(Constant.url + addFenceUrl, params);
            VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", requestBody, params, Constant.url + addFenceUrl, Object.class, this, TASK_ADD_FACILITY);

        }
    }

    @Override
    public void onResponse(Object response, int taskid) {
        super.onResponse(response, taskid);
        switch (taskid){
            case TASK_ADD_FACILITY:
//                DBUtil.setConfigValue("LastFenceRoute", (String) routeSpinner.getSelectedItem());
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
                        routeCodeText.setText(routeCodeValue);
                        valid = true;
                    }else if (flag1.equals("0001")){
                        valid = false;
                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加栏杆");
                    }else {
                        valid = false;
                        routeCodeText.setText("查无此路线");
                        ToastUtil.showShort(this, "你无法在此处添加栏杆");
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
                ToastUtil.showShort(this, "你无法在此处添加栏杆");
                break;
            default:
                break;
        }
    }
}
