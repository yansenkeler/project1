package com.fruit.client.adapter;

import android.content.Context;
import com.fruit.client.R;
import com.fruit.client.object.SelectTypeAlertObj;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/24.
 */
public class SelectTypeAlertAdapter extends BaseAdapter {
    private ArrayList<SelectTypeAlertObj> mAlertObjs = new ArrayList<>();
    private Context mContext;

    public SelectTypeAlertAdapter(ArrayList<SelectTypeAlertObj> mAlertObjs, Context mContext) {
        this.mAlertObjs = mAlertObjs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mAlertObjs.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlertObjs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectTypeAlertObj mAlertObj = mAlertObjs.get(position);
        ViewHolder mHolder;
        if (convertView==null){
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alert_select_type_item, null, false);
            mHolder.mIcon = (ImageView) convertView.findViewById(R.id.image);
            mHolder.mLabel = (TextView)convertView.findViewById(R.id.label);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mIcon.setImageResource(mAlertObj.getImage());
        mHolder.mLabel.setText(mAlertObj.getTitle());
        return convertView;
    }

    class ViewHolder{
        ImageView mIcon;
        TextView mLabel;
    }
}
