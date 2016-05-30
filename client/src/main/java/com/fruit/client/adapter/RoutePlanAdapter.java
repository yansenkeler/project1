package com.fruit.client.adapter;

import android.content.Context;
import com.fruit.client.object.RoutePlan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by John on 2016/5/12.
 */
public class RoutePlanAdapter extends BaseAdapter{
    private ArrayList<RoutePlan> routePlen = new ArrayList<>();
    private Context context;

    public RoutePlanAdapter(ArrayList<RoutePlan> routePlen, Context context) {
        this.routePlen = routePlen;
        this.context = context;
    }

    @Override
    public int getCount() {
        return routePlen.size();
    }

    @Override
    public Object getItem(int position) {
        return routePlen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RoutePlan routePlan = routePlen.get(position);
        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null, false);
            vh.label = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.label.setText(routePlan.getPlanName());
        return convertView;
    }

    class ViewHolder{
        TextView label;
    }
}
