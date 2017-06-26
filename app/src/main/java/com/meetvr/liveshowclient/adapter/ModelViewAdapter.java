package com.meetvr.liveshowclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetvr.liveshowclient.R;
import com.meetvr.share.utrils.PreferencesUtils;

import java.util.ArrayList;

/**
 * Created by wzm-pc on 2016/8/29.
 */
public class ModelViewAdapter extends BaseAdapter {
    private ArrayList<String> datas;


    private LayoutInflater mInflater;
    private Context ctx;

    public ModelViewAdapter(Context context, ArrayList<String> coll) {
        ctx = context;
        this.datas = coll;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return (datas==null)?0:datas.size() ;
    }

    @Override
    public Object getItem(int position) {
        return (datas==null)?null:datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //final RicherData entity = datas.get(position);
        int msgType = position;

        ViewHolder viewHolder = null;
        if (convertView == null) {

                convertView = mInflater.inflate(
                      R.layout.eye_item, null);

            viewHolder = new ViewHolder();

            viewHolder.eyeName =(TextView) convertView.findViewById(R.id.eye_name);
            viewHolder.selImg = (ImageView)convertView.findViewById(R.id.eye_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String eyeName = datas.get(position);
        viewHolder.eyeName.setText(eyeName);
        int eyeSel = PreferencesUtils.getSharePreInt(ctx,"modsel",0);
        if(eyeSel ==position){
            viewHolder.selImg.setVisibility(View.VISIBLE);
        }else{
            viewHolder.selImg.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView eyeName;

        public ImageView selImg;
    }
}
