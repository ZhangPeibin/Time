package com.timeshow.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timeshow.app.R;
import com.timeshow.app.activity.TimeCircleFragment;
import com.timeshow.app.model.ActiveModel;
import com.timeshow.app.model.FriendActiveModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by peibin on 18-2-14.
 */

public class TimeCircleAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<FriendActiveModel> mHistoryModels ;
    private Context mContext;
    private TimeCircleFragment mTimeCircleFragment;

    public TimeCircleAdapter (Context context, TimeCircleFragment timeCircleFragment){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mHistoryModels = new ArrayList<>();

        mTimeCircleFragment = timeCircleFragment;
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
        View view  = mLayoutInflater.inflate(R.layout.time_circle_item,parent,false);
        ImageView head = (ImageView) view.findViewById(R.id.iconImg);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.profile);
        TextView profile = (TextView) view.findViewById(R.id.profile);
        TextView time = (TextView) view.findViewById(R.id.time);
        TextView lovers = (TextView) view.findViewById(R.id.lovers);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView locate = (TextView) view.findViewById(R.id.location);
        ImageView good = (ImageView) view.findViewById(R.id.good);
        ImageView comment = (ImageView) view.findViewById(R.id.comment);
        ListView listView = (ListView) view.findViewById(R.id.active_comment);
        final FriendActiveModel activeModel = (FriendActiveModel) getItem(position);

        Glide.with(mContext).asBitmap().load(activeModel.url).into(image);
        Glide.with(mContext).asBitmap().load(activeModel.profile).into(head);


        title.setText(activeModel.head);
        profile.setText(activeModel.active_profile);
        address.setText(activeModel.address);
        time.setText("3分钟前");
        locate.setText(activeModel.address+"-"+activeModel.details_address);

        if ( activeModel.likes == null || activeModel.likes.size() == 0 ){
            ((View)lovers.getParent()).setVisibility(View.GONE);
        }else{
            ((View)lovers.getParent()).setVisibility(View.VISIBLE);
            int size = activeModel.likes.size();
            if ( size < 3 ){
                if ( size == 1 ){
                    lovers.setText(activeModel.likes.get(0)+"觉得很赞");
                }else{
                    lovers.setText(activeModel.likes.get(0)+","+
                            activeModel.likes.get(1)+"觉得很赞");
                }
            }else {
                lovers.setText(activeModel.likes.get(0)+","+
                        activeModel.likes.get(1)+"等"+size+"人"+"觉得很赞");
            }
        }

        if ( activeModel.comments != null && activeModel.comments.size() !=0 ){
            CommentChildAdapter commentChildAdapter = new CommentChildAdapter(mContext);
            listView.setAdapter(commentChildAdapter);
            commentChildAdapter.add(activeModel.comments);
        }

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mTimeCircleFragment.good(activeModel.id+"");
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                mTimeCircleFragment.comment(activeModel.id+"");
            }
        });
        return view;
    }

    public void notifyDataChanged(List<FriendActiveModel> historyModels){
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
}
