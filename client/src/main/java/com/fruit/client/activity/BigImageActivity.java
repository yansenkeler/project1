package com.fruit.client.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.fruit.client.R;
import com.fruit.client.fragment.BigImageFragment;
import com.fruit.core.activity.FruitActivity;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/15.
 */
public class BigImageActivity extends FruitActivity {
    private static final int CLICK_TIME = 100;

    private ViewPager mViewPager;
    private ImageView close;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<BigImageFragment> fragments = new ArrayList<>();
    private int currentIndex = 0;
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = getIntent();
        if (mIntent!=null && mIntent.hasExtra("currentIndex") && mIntent.hasExtra("imageurls")){
            currentIndex = mIntent.getIntExtra("currentIndex", -1);
            imageUrls = mIntent.getStringArrayListExtra("imageurls");
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
        for (int i=0; i<imageUrls.size(); i++){
            BigImageFragment fragment = BigImageFragment.newInstance(imageUrls.get(i));
            fragments.add(fragment);
        }
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
            return imageUrls.size();
        }
    }
}
