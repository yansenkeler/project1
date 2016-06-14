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
public class BigImage2Activity extends FruitActivity {
    private static final int CLICK_TIME = 100;
    private String billNoValue;
    private ViewPager mViewPager;
    private ImageView close;
    private ArrayList<String> imageInfos = new ArrayList<>();
    private ArrayList<BigImageFragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = getIntent();
        if (mIntent!=null && mIntent.hasExtra("imageurls") && mIntent.hasExtra("currentIndex")){
            imageInfos = mIntent.getStringArrayListExtra("imageurls");
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFragments();
    }

    private void initFragments(){
        for (int i=0; i<imageInfos.size(); i++){
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setUrl(imageInfos.get(i));
            BigImageFragment bigImageFragment = BigImageFragment.newInstance(imageInfo);
            fragments.add(bigImageFragment);
        }
        mAdapter.notifyDataSetChanged();
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
