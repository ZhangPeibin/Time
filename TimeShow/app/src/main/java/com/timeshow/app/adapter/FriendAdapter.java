package com.timeshow.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timeshow.app.R;
import com.timeshow.app.activity.MyFriendActivity;
import com.timeshow.app.model.HistoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by peibin on 18-2-14.
 */

public class FriendAdapter extends BaseAdapter implements View.OnClickListener {

    private MyFriendActivity mMyFriendActivity;
    private LayoutInflater mLayoutInflater;
    private List<String> mHistoryModels ;

    public FriendAdapter (MyFriendActivity context){
        this.mMyFriendActivity = context;
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
        View view  = mLayoutInflater.inflate(R.layout.friend_item,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.title);
        TextView countTv = (TextView) view.findViewById(R.id.count);
        TextView delete = (TextView) view.findViewById(R.id.delete);
        delete.setTag(mHistoryModels.get(position));
        delete.setOnClickListener(this);

        String historyModel = mHistoryModels.get(position);
        textView.setText("手机号");
        countTv.setText(historyModel);
        return view;
    }

    public void notifyDataChanged(List<String> historyModels){
        if ( historyModels == null ) return;
        mHistoryModels.clear();
        mHistoryModels.addAll(historyModels);
        Collections.reverse(mHistoryModels);
        notifyDataSetChanged();
    }

    @Override
    public void onClick (View v) {
        String rphone = v.getTag().toString();
        mMyFriendActivity.delete(rphone);
    }
}
