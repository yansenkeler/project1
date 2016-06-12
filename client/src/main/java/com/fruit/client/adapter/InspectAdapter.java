package com.fruit.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fruit.client.R;
import com.fruit.client.object.RoutePlan;

import java.util.ArrayList;

/**
 * Created by John on 6/12/2016.
 */
public class InspectAdapter extends BaseAdapter {
    private ArrayList<RoutePlan> routePlen = new ArrayList<>();
    private Context context;

    public InspectAdapter(ArrayList<RoutePlan> routePlen, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_inspect_item, null, false);
            vh.name = (TextView)convertView.findViewById(R.id.name);
            vh.date = (TextView)convertView.findViewById(R.id.date);
            vh.desc = (TextView)convertView.findViewById(R.id.desc);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.name.setText(routePlan.getPlanName());
        vh.date.setText(routePlan.getDateInfo());
        vh.desc.setText(routePlan.getPlanDescr());
        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView date;
        TextView desc;
    }
}
