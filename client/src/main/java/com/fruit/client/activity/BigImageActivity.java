package com.fruit.client.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fruit.client.R;
import com.fruit.client.fragment.BigImageFragment;
import com.fruit.client.object.ImageItem;
import com.fruit.core.activity.FruitActivity;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/15.
 */
public class BigImageActivity extends FruitActivity {
    private static final int CLICK_TIME = 100;

    private ViewPager mViewPager;
    private ImageView close;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    private ArrayList<BigImageFragment> fragments = new ArrayList<>();
    private int currentIndex;
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = getIntent();
        if (mIntent!=null){
            currentIndex = mIntent.getIntExtra("currentIndex", -1);
            imageItems = mIntent.getParcelableArrayListExtra("imageitems");
        }else {
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        initFragment();

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(currentIndex);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

    }

    private void initFragment() {
        for (int i=0; i<imageItems.size(); i++){
            BigImageFragment fragment = BigImageFragment.newInstance(imageItems.get(i).getImgUrl());
            fragments.add(fragment);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    class ImagePagerAdapter extends FragmentStatePagerAdapter{

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return imageItems.size();
        }
    }
}
