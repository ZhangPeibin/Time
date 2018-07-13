package com.timeshow.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.timeshow.app.R;
import com.timeshow.app.utils.SpUtils;

/**
 * Created by peibin on 18-5-2.
 */
public class SplashActivity extends Activity implements Runnable {
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        boolean isFirst = SpUtils.get_bool(getApplicationContext(),"isFirst");
        if ( isFirst ){
            mHandler.postDelayed(this,3000);
        }else{
            mHandler.postDelayed(this,0);
        }

    }

    @Override
    public void run () {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
