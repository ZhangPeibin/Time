package com.timeshow.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

/**
 * Created by peibin on 18-2-13.
 */

public class SpUtils {

    public static void save_str(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        sharedPreferences.edit().putString(key,value).apply();
    }

    public static String get_str(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        return sharedPreferences.getString(key,null);
    }

    public static void save_long(Context context,String key,long value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        sharedPreferences.edit().putLong(key,value).apply();
    }

    public static long get_long(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        return sharedPreferences.getLong(key,0L);
    }

    public static void save_bool(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    public static boolean get_bool(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        return sharedPreferences.getBoolean(key,false);
    }

    public static void clear (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",Context.MODE_APPEND);
        sharedPreferences.edit().clear().commit();
    }
}

