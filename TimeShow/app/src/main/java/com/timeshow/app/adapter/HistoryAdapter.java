package com.timeshow.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timeshow.app.R;
import com.timeshow.app.model.HistoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by peibin on 18-2-14.
 */

public class HistoryAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<HistoryModel> mHistoryModels ;

    public HistoryAdapter(Context context){
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
        View view  = mLayoutInflater.inflate(R.layout.history_item,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.title);
        TextView countTv = (TextView) view.findViewById(R.id.count);
        HistoryModel historyModel = mHistoryModels.get(position);
        String title = historyModel.title;
        String phone = historyModel.phone;
        String type = historyModel.type;
        String count = historyModel.count;
        textView.setText(title);
        if ( "1".equals(type) ){
            countTv.setText("收入"+count);
        }else{
            countTv.setText("支出"+count);
        }
        return view;
    }

    public void notifyDataChanged(List<HistoryModel> historyModels){
        if ( historyModels == null ) return;
        mHistoryModels.clear();
        mHistoryModels.addAll(historyModels);
        Collections.reverse(mHistoryModels);
        notifyDataSetChanged();
    }
}
