package com.fruit.client.adapter;

import android.content.Context;
import com.fruit.client.R;
import com.fruit.client.object.facilitymanager.ImageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/1.
 */
public class UploadImageGridViewAdapter extends BaseAdapter {
    private ArrayList<ImageInfo> mImageInfos = new ArrayList<>();
    private Context mContext;

    public UploadImageGridViewAdapter(ArrayList<ImageInfo> mImageInfos, Context mContext) {
        this.mImageInfos = mImageInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mImageInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageInfo mInfo = mImageInfos.get(position);
        ViewHolder mHolder;
        if (convertView==null){
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_add_facility_gridview_item, null, false);
            mHolder.mImageView = (ImageView)convertView.findViewById(R.id.image);
            mHolder.mProgressBar = (ProgressBar)convertView.findViewById(R.id.progress);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder)convertView.getTag();
        }
        if (mInfo.isImage()){
            mHolder.mImageView.setImageBitmap(mInfo.getBitmap());
            if (mInfo.getProgress()<100){
                mHolder.mProgressBar.setProgress(mInfo.getProgress());
                mHolder.mProgressBar.setVisibility(View.VISIBLE);
            }else {
                mHolder.mProgressBar.setProgress(100);
                mHolder.mProgressBar.setVisibility(View.GONE);
            }
        }else {
            mHolder.mProgressBar.setVisibility(View.GONE);
            mHolder.mImageView.setImageResource(R.drawable.ic_add_grey600_48dp);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView mImageView;
        ProgressBar mProgressBar;
    }
}
