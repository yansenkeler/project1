package com.fruit.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruit.client.R;
import com.fruit.client.object.event.PushMessage;
import com.fruit.core.db.models.gen.msg;

import java.util.ArrayList;

/**
 * Created by John on 2016/5/20.
 */
public class MyMsgAdapter extends BaseAdapter {
    private ArrayList<PushMessage> msgs = new ArrayList<>();
    private Context context;

    public MyMsgAdapter(ArrayList<PushMessage> msgs, Context context) {
        this.msgs = msgs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PushMessage msg = msgs.get(position);
        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_msg_item, null, false);
            vh.title = (TextView)convertView.findViewById(R.id.title);
            vh.description = (TextView)convertView.findViewById(R.id.desc);
            vh.msgMark = (ImageView) convertView.findViewById(R.id.is_read_mark);
            vh.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        if (msg.getIsRead().equals("True")){
            vh.msgMark.setVisibility(View.GONE);
        }else {
            vh.msgMark.setVisibility(View.VISIBLE);
        }
        vh.title.setText(msg.getTitle());
        vh.description.setText(msg.getMsgCenter());
        vh.date.setText(msg.getDateInfo());
        return convertView;
    }

    class ViewHolder{
        TextView title;
        TextView description;
        ImageView msgMark;
        TextView date;
    }
}
