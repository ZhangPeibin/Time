package com.timeshow.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timeshow.app.R;
import com.timeshow.app.TimeApplication;
import com.timeshow.app.activity.AddActiveActivity;
import com.timeshow.app.model.ActiveModel;
import com.timeshow.app.model.HistoryModel;
import com.timeshow.app.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by peibin on 18-2-14.
 */

public class ActiveAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private List<ActiveModel> mHistoryModels ;
    private Context mContext;

    public ActiveAdapter (Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mHistoryModels = new ArrayList<>();
    }

    @Override
    public int getCount () {
        return mHistoryModels.size();
    }

    @Override
    public Object getItem (int position) {
        return mHistoryModels.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View view  = mLayoutInflater.inflate(R.layout.active_fragment,parent,false);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView profile = (TextView) view.findViewById(R.id.profile);
        TextView cost = (TextView) view.findViewById(R.id.cost);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView edit = (TextView) view.findViewById(R.id.edit);
        ImageView i = (ImageView) view.findViewById(R.id.image);
        ActiveModel activeModel = mHistoryModels.get(position);
        Glide.with(mContext).asBitmap().load(activeModel.url).into(i);
        if ("2".equals(activeModel.status) && activeModel.phone.equals(SpUtils.get_str(mContext,"phone")) ){
            title.setText(activeModel.title+"----(待付时间)");
        }else if ( "3".equals(activeModel.status )&& activeModel.phone.equals(SpUtils.get_str(mContext,"phone")) ){
            title.setText(activeModel.title+"----(已支付)");
        }else{
            title.setText(activeModel.title);
        }

        if ( !activeModel.phone.equals(SpUtils.get_str(mContext,"phone")) ){
            edit.setVisibility(View.GONE);
            edit.setOnClickListener(null);
        }else{
            edit.setVisibility(View.VISIBLE);
            edit.setTag(activeModel);
            edit.setOnClickListener(this);
        }

        profile.setText(activeModel.profile);
        cost.setText("费用: "+activeModel.cost+"时间");
        if ( activeModel.detail == null ){
            address.setText(activeModel.address);
        }else{
            address.setText(activeModel.address + activeModel.detail);
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(activeModel.time));
        String date = sf.format(calendar.getTime());
        time.setText(date);
        return view;
    }

    public void notifyDataChanged(List<ActiveModel> historyModels){
        if ( historyModels == null ) return;
        mHistoryModels.clear();
        mHistoryModels.addAll(historyModels);
        Collections.reverse(mHistoryModels);
        notifyDataSetChanged();
    }

    public void clear () {
        mHistoryModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick (View v) {
        if ( v.getId() == R.id.edit ){
            ActiveModel activeModel = (ActiveModel) v.getTag();
            TimeApplication.getInstance().setActiveModel(activeModel);
            Intent intent = new Intent(mContext,AddActiveActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type",1);
            mContext.startActivity(intent);

        }
    }
}
