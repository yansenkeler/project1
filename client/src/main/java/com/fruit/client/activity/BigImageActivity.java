package com.fruit.client.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.fruit.client.R;
import com.fruit.client.fragment.BigImageFragment;
import com.fruit.client.object.ImageInfo;
import com.fruit.client.util.Constant;
import com.fruit.client.util.Urls;
import com.fruit.common.network.NetWorkUtil;
import com.fruit.common.ui.ToastUtil;
import com.fruit.core.activity.FruitActivity;
import com.fruit.core.http.VolleyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/3/15.
 */
public class BigImageActivity extends FruitActivity {
    private static final int CLICK_TIME = 100;
    private static final int TASK_GET_IMAGE = 0;
    private String billNoValue;
    private ViewPager mViewPager;
    private ImageView close;
    private ArrayList<ImageInfo> imageInfos = new ArrayList<>();
    private ArrayList<BigImageFragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = getIntent();
        if (mIntent!=null && mIntent.hasExtra("billno") && mIntent.hasExtra("currentIndex")){
            billNoValue = mIntent.getStringExtra("billno");
            currentIndex = mIntent.getIntExtra("currentIndex", 0);
        }else {
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        showDialog("正在获取图片信息...");
        getImagesInfo();
    }

    @Override
    public void dealFailureResult(VolleyError returnObject, int taskid) {
        super.dealFailureResult(returnObject, taskid);
        switch (taskid){
            case TASK_GET_IMAGE:
                hideProgressDialog();
                ToastUtil.showShort(this, "获取图片失败");
                break;
            default:
                break;
        }
    }

    @Override
    public void dealSuccessResult(Object returnObject, int taskid) {
        super.dealSuccessResult(returnObject, taskid);
        switch (taskid){
            case TASK_GET_IMAGE:
                if (returnObject!=null){
                    JSONObject jsonObject = (JSONObject)returnObject;
                    String flag = jsonObject.getString("flag");
                    if (flag.equals("0000")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("list");
                        for (int i=0; i<jsonArray.size(); i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setType(jsonObject2.getString("type")==null?"":jsonObject2.getString("type"));
                            imageInfo.setUpUser(jsonObject2.getString("upUser")==null?"":jsonObject2.getString("upUser"));
                            imageInfo.setUpTime(jsonObject2.getString("upTime")==null?"":jsonObject2.getString("upTime"));
                            imageInfo.setUrl(jsonObject2.getString("Url")==null?"":jsonObject2.getString("Url"));
                            BigImageFragment bigImageFragment = BigImageFragment.newInstance(imageInfo);
                            fragments.add(bigImageFragment);
                        }
                        mAdapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(currentIndex);
                        hideProgressDialog();
                    }else if (flag.equals("0001")){
                        hideProgressDialog();
                        ToastUtil.showShort(this, "没有图片");
                    }else {
                        hideProgressDialog();
                        ToastUtil.showShort(this, "获取图片失败");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getImagesInfo(){
        Map<String, String> params = new HashMap<>();
        params.put("type", "query");
        params.put("billno", billNoValue);
        String s = NetWorkUtil.appendParameter(Constant.url, params);
        VolleyManager.newInstance(this).JsonPostRequest(null, "utf-8", s, params, Urls.ROOT+Urls.EVENT_IMAGE,
                Object.class, this, TASK_GET_IMAGE);
    }


    class ImagePagerAdapter extends FragmentStatePagerAdapter{
        private ArrayList<BigImageFragment> imageFragments = new ArrayList<>();

        public ImagePagerAdapter(FragmentManager fm, ArrayList<BigImageFragment> imageInfos) {
            super(fm);
            this.imageFragments = imageInfos;
        }

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return imageFragments.get(position);
        }

        @Override
        public int getCount() {
            return imageFragments.size();
        }
    }
}
