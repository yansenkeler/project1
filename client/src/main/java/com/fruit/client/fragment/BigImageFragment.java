package com.fruit.client.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.fruit.client.R;
import com.fruit.client.object.ImageInfo;
import com.fruit.client.util.Urls;
import com.fruit.core.fragment.FruitFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by John on 2016/6/2.
 */
public class BigImageFragment extends FruitFragment {
    private ImageView imageView;
    private TextView type, upUser, upTime;
    private ImageInfo imageInfo;

    public static BigImageFragment newInstance(ImageInfo imageInfo) {
        BigImageFragment fragment = new BigImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("image_info", imageInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey("image_info")){
            imageInfo = bundle.getParcelable("image_info");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_big_img, null, false);
        imageView = (ImageView)v.findViewById(R.id.imageview);
        type = (TextView)v.findViewById(R.id.type);
        upUser = (TextView)v.findViewById(R.id.up_user);
        upTime = (TextView)v.findViewById(R.id.up_time);

        type.setText(imageInfo.getType());
        upUser.setText(imageInfo.getUpUser());
        upTime.setText(imageInfo.getUpTime());
        ImageLoader.getInstance().displayImage(Urls.ROOT+imageInfo.getUrl(), imageView);
//        if (Util.isOnMainThread()){
//            Glide.with(getActivity().getApplicationContext()).load(Urls.ROOT+imageUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
//        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (Util.isOnMainThread()){
//            Glide.with(getActivity().getApplicationContext()).onStart();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (Util.isOnMainThread()){
//            Glide.with(getActivity().getApplicationContext()).onStop();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (Util.isOnMainThread()){
//            Glide.with(getActivity().getApplicationContext()).onDestroy();
//        }
    }

    @Override
    public void onFruitClick(int id) {

    }
}
