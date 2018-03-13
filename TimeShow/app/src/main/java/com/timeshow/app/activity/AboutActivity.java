package com.timeshow.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timeshow.app.R;
import com.timeshow.app.adapter.FriendAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by peibin on 18-3-13.
 */

public class AboutActivity extends Activity {
    @BindView(R.id.version)
    public TextView about;
    @BindView(R.id.back)
    public ImageView mImageView;
    @BindView ( R.id.title )
    public TextView mTitle;
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        about.setText("当前版本:1.0.0");
        mImageView.setVisibility(View.VISIBLE);
        mTitle.setText("关于");
    }

    @OnClick(R.id.back)
    public void back(){
        finish();
    }
}
