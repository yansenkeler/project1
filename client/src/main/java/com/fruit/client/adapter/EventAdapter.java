package com.fruit.client.adapter;

import android.content.Context;
import com.fruit.client.R;
import com.fruit.client.object.event.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by qianyx on 16-5-9.
 */
public class EventAdapter extends BaseAdapter{
    private ArrayList<Event> mEvents;
    private Context mContext;

    public EventAdapter(ArrayList<Event> mEvents, Context mContext) {
        this.mEvents = mEvents;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event mEvent = mEvents.get(position);
        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_event_list, null, false);
            vh.img = (ImageView)convertView.findViewById(R.id.img);
            vh.grade = (TextView)convertView.findViewById(R.id.rank);
            vh.carNo = (TextView)convertView.findViewById(R.id.plate_number);
            vh.routeCode = (TextView)convertView.findViewById(R.id.route_code);
            vh.dealStatus = (TextView)convertView.findViewById(R.id.deal_status);
            vh.estimateTime = (TextView)convertView.findViewById(R.id.estimate_time);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.grade.setText(mEvent.getGrade());
        vh.carNo.setText(mEvent.getCarNo());
        vh.routeCode.setText(mEvent.getRouteNo());
        vh.estimateTime.setText(mEvent.getBillDate());
        vh.dealStatus.setText(mEvent.getState());
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView grade;
        TextView carNo;
        TextView routeCode;
        TextView dealStatus;
        TextView estimateTime;
    }
}
