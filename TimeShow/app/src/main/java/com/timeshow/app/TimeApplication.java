package com.timeshow.app;

import android.app.Application;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.tencent.bugly.crashreport.CrashReport;
import com.timeshow.app.model.ActiveModel;

/**
 * Created by peibin on 18-6-16.
 */
public class TimeApplication extends Application {

    private static TimeApplication instance;

    public static TimeApplication getInstance(){
        return instance;
    }

    private ActiveModel mActiveModel;

    public ActiveModel getActiveModel () {
        ActiveModel activeModel = mActiveModel;
        mActiveModel = null;
        return activeModel;
    }

    public void setActiveModel (ActiveModel activeModel) {
        mActiveModel = activeModel;
    }//

    @Override
    public void onCreate () {
        super.onCreate();
        instance = this;
        CrashReport.initCrashReport(getApplicationContext(), "e29540baa4", true);

    }
}
