package com.timeshow.app.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.timeshow.app.R;
import com.timeshow.app.request.DaiKuanCoinService;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.GetCoinService;
import com.timeshow.app.request.TransferGetCoinService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.blue_money)
    public TextView mBlueText;
    @BindView(R.id.cheng_money)
    public TextView mOrangeText;
    @BindView(R.id.gift_count)
    public TextView giftText;
    @BindView(R.id.time)
    public TextView time;
    @BindView(R.id.banner)
    public Banner mBanner;

    public static List<?> images=new ArrayList<String>(){
        {
            add("http://img.ui.cn/data/file/3/4/7/1118743.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!1800%3E/quality/90/");
            add("http://img.ui.cn/data/file/4/4/7/1118744.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!1800%3E/quality/90/");
            add("http://img.ui.cn/data/file/5/4/7/1118745.jpg?imageMogr2/auto-orient/format/jpg/strip/thumbnail/!1800%3E/quality/90/");
        }
    };

    public HomeFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        // Inflate the layout for this fragment
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                Activity activity = getActivity();
                if ( activity != null ){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            setDateInfo(time);
                        }
                    });
                }
            }
        },0,1000);
    }
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
            //用fresco加载图片简单用法，记得要写下面的createImageView方法
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }


    public String time(){
        long hour = 60 * 60 * 1000;
        long time = System.currentTimeMillis();
        time += 8 * hour;
        time %= 24 * hour;
        time = 24 * hour - time;
        String result =  String.format("%02d:%02d:%02d\n",time / hour, time % hour / 60000,time % hour / (60000*60));
        return result;
    }

    private void setDateInfo(TextView textView) {
        try {
            DateFormat format=new SimpleDateFormat("hh:mm:ss");
            Date d2=format.parse("23:59:59");
            Date currDate = new Date(System.currentTimeMillis());
            Date endDate = new Date(d2.getTime());
            long diff = currDate.getTime() - endDate.getTime(); // 得到的差值是微秒级别，可以忽略
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60)) / (1000);
            textView.setText("距离今晚0时还有"
                    + (24 - (hours + 1)) + "小时"
                    + (60 - (minutes + 1)) + "分"
                    + (60 - (seconds + 1)) + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume () {
        super.onResume();
        UserCoinTask userCoinTask = new UserCoinTask();
        userCoinTask.execute();
    }

    @OnClick(R.id.get)
    public void get(View view){

    }

    @OnClick(R.id.save)
    public void save(View view){

    }

    @OnClick(R.id.scan)
    public void onScan(View view){
        Intent i = new Intent(getActivity(),ScanActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.fushijian)
    public void fushijian(View view){
        Intent i = new Intent(getActivity(),Generatectivity.class);
        i.putExtra(Generatectivity.TYPE,0);
        startActivity(i);
    }

    @OnClick(R.id.shoushijian)
    public void shoushijian(View view){
        Intent i = new Intent(getActivity(),Generatectivity.class);
        i.putExtra(Generatectivity.TYPE,1);
        startActivity(i);
    }

    @OnClick(R.id.translate)
    public void translate(View view){
        Intent i = new Intent(getActivity(),TransferActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.timeget)
    public void timeget(View view){
        TransferGetCoinTask transferGetCoinTask = new TransferGetCoinTask("18589072871","30");
        transferGetCoinTask.execute();
    }

    @OnClick(R.id.search_badge)
    public void searchBadge(View view){
        Intent intent = new Intent(getActivity(),TransactionHistoryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.timme_money)
    public void timme_money(View view){
        DaiKuanCoinTask daiKuanCoinTask = new DaiKuanCoinTask();
        daiKuanCoinTask.execute();
    }




    public class DaiKuanCoinTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            DaiKuanCoinService loginRequest = retrofit.create(DaiKuanCoinService.class);
            Call<String> request = loginRequest.coin(SpUtils.get_str(getActivity(),"token"));
            try {
                Response<String> response = request.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute (final String res) {
            Activity activity = getActivity();
            if ( activity == null ){
                return;
            }

            if (res == null){
                Toast.makeText(activity,"服务器异常",Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject j = new JSONObject(res);
                    int status = j.optInt("status");
                    if ( status == ErrorCode.USER_NOT_EXIST ){
                        toLogin();
                        return;
                    }
                    if(status == ErrorCode.SUCCESS){
                        double blue_coin =j.optDouble("blue_coin");
                        double orange_coin =j.optDouble("orange_coin");
                        mBlueText.setText(blue_coin+"");
                        mOrangeText.setText(orange_coin+"");
                    }
                    String message = j.optString("message");
                    Toast.makeText(activity,message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled () {
        }
    }


    public class UserCoinTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            GetCoinService loginRequest = retrofit.create(GetCoinService.class);
            Call<String> request = loginRequest.coin(SpUtils.get_str(getActivity(),"token"));
            try {
                Response<String> response = request.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute (final String res) {
            Activity activity = getActivity();
            if ( activity == null ){
                return;
            }

            if (res == null){
                Toast.makeText(activity,"服务器异常",Toast.LENGTH_SHORT).show();
            }else{
                Log.d(LoginActivity.class.getSimpleName(),res);
                try {
                    JSONObject j = new JSONObject(res);
                    int status = j.optInt("status");
                    if ( status == ErrorCode.USER_NOT_EXIST ){
                        toLogin();
                        return;
                    }
                    double blue_coin =j.optDouble("blue_coin");
                    double orange_coin =j.optDouble("orange_coin");
                    String title = j.optString("title");
                    String profile = j.optString("profile");
                    String url = j.optString("head");
                    if ( title != null && title.length() != 0){
                        SpUtils.save_str(getActivity(),"title",title);
                    }
                    if ( profile != null && profile.length() != 0){
                        SpUtils.save_str(getActivity(),"profile",profile);
                    }
                    if ( url != null && url.length() != 0){
                        SpUtils.save_str(getActivity(),"url",title);
                    }
                    mBlueText.setText(blue_coin+"");
                    mOrangeText.setText(orange_coin+"");
                    giftText.setText((int)(orange_coin / 100)+"个");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled () {
        }
    }

    private void toLogin(){
        SpUtils.clear(getActivity());
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public class TransferGetCoinTask extends AsyncTask<Void, Void, String> {

        private String mPhone;
        private String mCount;

        public TransferGetCoinTask(String phone, String count){
            mPhone = phone;
            mCount = count;
        }

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            TransferGetCoinService loginRequest = retrofit.create(TransferGetCoinService.class);
            Call<String> request = loginRequest.get(SpUtils.get_str(getActivity(),"token"),
                    mPhone,mCount,"收款");
            try {
                Response<String> response = request.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute (final String res) {
            if (res == null){
                Toast.makeText(getActivity(),"服务器异常",Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject j = new JSONObject(res);
                    int status = j.optInt("status");
                    String message = j.optString("message");
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled () {
        }
    }

}
