package com.timeshow.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timeshow.app.R;
import com.timeshow.app.model.FriendActiveModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzq on 2016/4/29.
 */
public class CommentChildAdapter extends BaseAdapter {

    private Context mContext;
    private List<FriendActiveModel.Comment> mNoteCommentInfos;

    public CommentChildAdapter (Context context) {
        this.mContext = context;
        mNoteCommentInfos = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mNoteCommentInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mNoteCommentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<FriendActiveModel.Comment> getList() {
        return mNoteCommentInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendActiveModel.Comment item = mNoteCommentInfos.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.note_child_comment, null);
            holder.txt_question = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String userName = item.name;
        String content = item.content + "";
        SpannableStringBuilder style = new SpannableStringBuilder(userName + "：" + content);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#507daf")),
                0, userName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.txt_question.setText(style);
        return convertView;
    }


    public void clear() {
        if (mNoteCommentInfos != null && mNoteCommentInfos.size() != 0) {
            mNoteCommentInfos.clear();
        }
    }

    public void add(List<FriendActiveModel.Comment> commentChildren) {
        if (mNoteCommentInfos == null) mNoteCommentInfos = new ArrayList<>();
        mNoteCommentInfos.addAll(commentChildren);
    }

    private static class ViewHolder {
        private TextView txt_question;
    }

}
