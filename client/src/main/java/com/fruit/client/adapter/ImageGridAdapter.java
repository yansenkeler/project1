package com.fruit.client.adapter;

import android.content.Context;
import com.fruit.client.R;
import com.fruit.client.object.ImageItem;
import com.fruit.client.util.Urls;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fruit.common.res.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by user on 2016/4/29.
 */
public class ImageGridAdapter extends BaseAdapter {
    private ArrayList<ImageItem> mImages = new ArrayList<>();
    private Context mContext;

    public ImageGridAdapter(ArrayList<ImageItem> mImages, Context mContext) {
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        ImageItem imageItem = mImages.get(position);
        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            v = LayoutInflater.from(mContext).inflate(R.layout.image_grid_item, null, false);
            vh.mImageView = (ImageView)v.findViewById(R.id.image);
            vh.mProgress = (FrameLayout)v.findViewById(R.id.progress);
            v.setTag(vh);
        }else {
            v = convertView;
            vh = (ViewHolder) v.getTag();
        }
        FrameLayout.LayoutParams mParams = (FrameLayout.LayoutParams) vh.mImageView.getLayoutParams();
        WindowManager mManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mMetrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(mMetrics);
        mParams.width = (mMetrics.widthPixels - DensityUtil.dip2px(mContext, 64)) / 4;
        mParams.height = (mMetrics.widthPixels - DensityUtil.dip2px(mContext, 64)) / 4;
        vh.mImageView.setLayoutParams(mParams);
        FrameLayout.LayoutParams mParams1 = (FrameLayout.LayoutParams) vh.mProgress.getLayoutParams();
        mParams1.width = mParams.width;
        mParams1.height = mParams.width;
        vh.mProgress.setLayoutParams(mParams1);
        if (imageItem.isLast()){
            vh.mImageView.setImageResource(R.drawable.add);
            vh.mProgress.setVisibility(View.GONE);
            Log.d("imagegrid", "添加按钮 "+position);
        }else {
            Log.d("imagegrid", "图片 "+position);
            if (mImages.get(position).getBitmap()!=null){
                vh.mImageView.setImageBitmap(mImages.get(position).getBitmap());
            }else {
                String imgUrl = mImages.get(position).getImgUrl();
                ImageLoader.getInstance().displayImage(Urls.ROOT+imgUrl, vh.mImageView);
            }
            if (mImages.get(position).getUploadStatus()==0){
                vh.mProgress.setVisibility(View.VISIBLE);
            }else {
                vh.mProgress.setVisibility(View.GONE);
            }
        }
        return v;
    }

    public void hideAddIcon(){

    }

    class ViewHolder{
        ImageView mImageView;
        FrameLayout mProgress;
    }

}
