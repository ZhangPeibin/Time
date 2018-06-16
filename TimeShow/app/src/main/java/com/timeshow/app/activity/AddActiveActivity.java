package com.timeshow.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.timeshow.app.R;
import com.timeshow.app.TimeApplication;
import com.timeshow.app.model.ActiveModel;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.PostActiveService;
import com.timeshow.app.request.UploadImageService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.PhotoObtainHelper;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by peibin on 18-2-15.
 */

public class AddActiveActivity extends Activity implements PhotoObtainHelper.OnBitmapBackListener {

    @BindView ( R.id.title )
    public TextView title;
    @BindView ( R.id.back )
    public ImageView back;

    @BindView ( R.id.atitle )
    public EditText titleView;
    @BindView ( R.id.profile )
    public EditText profileView;
    @BindView ( R.id.money )
    public EditText costView;
    @BindView ( R.id.address )
    public TextView addressView;
    @BindView ( R.id.time )
    public TextView timeView;
    @BindView ( R.id.image )
    public ImageView image;
    @BindView(R.id.kind)
    public TextView kindView;
    @BindView(R.id.details_address)
    public EditText detailView;

    public int kind = -1;
    public String time;
    public String url;

    PhotoObtainHelper mPhotoObtainHelper;

    //类型, 0代表新曾, 1代表编辑
    private int mType = 0;
    private String mId = null;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_active_layout);
        ButterKnife.bind(this);
        title.setText("发布活动");
        back.setVisibility(View.VISIBLE);
        mPhotoObtainHelper = new PhotoObtainHelper();
        mPhotoObtainHelper.setCrop(false);
        CityListLoader.getInstance().loadCityData(this);

        if ( getIntent().hasExtra("type") ){
            mType = getIntent().getIntExtra("type",0);
        }

        if ( mType == 1 ){
            ActiveModel activeModel = TimeApplication.getInstance().getActiveModel();
            if ( activeModel != null ){
                title.setText(activeModel.title);
                profileView.setText(activeModel.profile);
                costView.setText(activeModel.cost);
                mId = activeModel.id;
            }
        }
    }

    @OnClick ( R.id.image )
    void pickImage (View view) {
        mPhotoObtainHelper.show(this, image);
        mPhotoObtainHelper.setBitmapBackListener(this);
    }

    @OnClick ( R.id.back )
    void back (View view) {
        finish();
    }

    @OnClick(R.id.address)
    void selectAddress(View view){
        Intent intent = new Intent(this, CityListSelectActivity.class);
        startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);
    }


    @OnClick(R.id.kind)
    void kind(View view){
        new MaterialDialog.Builder(this)
                .title(R.string.active_title)
                .items(R.array.active_kind)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection (MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        kindView.setText(text);
                        kind = position;
                    }
                })
                .show();
    }


    @OnClick ( R.id.email_sign_in_button )
    void post (View view) {
        String title = titleView.getText().toString();
        if ( !checkParams(title, "请填写活动标题") ) {
            return;
        }
        String profile = profileView.getText().toString();
        if ( !checkParams(profile, "请填写活动简介") ) {
            return;
        }
        String cost = costView.getText().toString();
        if ( !checkParams(cost, "请填写活动费用") ) {
            return;
        }

        String adddress = addressView.getText().toString();
        if ( !checkParams(adddress, "请选择活动城市") ) {
            return;
        }

        String detailadddress = detailView.getText().toString();
        if ( !checkParams(detailadddress, "请填写活动详细地址") ) {
            return;
        }
        String timeStr = timeView.getText().toString();
        if ( !checkParams(timeStr, "请选择活动时间") ) {
            return;
        }
        if ( !checkParams(url, "请上传图片") ) {
            return;
        }
        if ( kind == -1) {
            Toast.makeText(getApplicationContext(),"请选择分类",Toast.LENGTH_SHORT).show();
            return;
        }

        TransferCoinTask transferCoinTask = new TransferCoinTask(mId,
                title, profile, cost, adddress,detailadddress, time, url,(kind+1)+""
        );
        transferCoinTask.execute();
    }

    @Override
    public void onBitmap (Bitmap bitmap, String path) {
        image.setImageBitmap(bitmap);

        File file = new File(path);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("图片上传")
                .content("正在上传中,请等待...")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        UploadImageService loginRequest = retrofit.create(UploadImageService.class);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); //
        // MultipartBody.Part 和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<String> request = loginRequest.uploadImage(body);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                dialog.dismiss();
                String responseBody = response.body();
                if ( responseBody == null ){
                    Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject j = new JSONObject(responseBody);
                        int status = j.optInt("status");
                        if ( status == ErrorCode.SUCCESS ) {
                            mPhotoObtainHelper.dismiss();
                            url = j.optString("url");
                            Log.d("fuck",url);
                        }
                        String message = j.optString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"上传失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( mPhotoObtainHelper != null )
            mPhotoObtainHelper.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                Bundle bundle = data.getExtras();

                CityInfoBean cityInfoBean = (CityInfoBean) bundle.getParcelable("cityinfo");

                if (null == cityInfoBean) {
                    return;
                }

                addressView.setText(cityInfoBean.getName());
            }
        }
    }

    public class TransferCoinTask extends AsyncTask<Void, Void, String> {

        private String title;
        private String profile;
        private String cost;
        private String address;
        private String time;
        private String url;
        private String kind;
        private String details;
        private String id;

        public TransferCoinTask (String id, String title,
                                 String profile,
                                 String cost,
                                 String address,String details, String time, String url,String kind) {
            this.id = id;
            this.title = title;
            this.profile = profile;
            this.cost = cost;
            this.address = address;
            this.time = time;
            this.url = url;
            this.kind = kind;
            this.details = details;
        }

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
            PostActiveService loginRequest = retrofit.create(PostActiveService.class);
            Call<String> request = loginRequest.add(SpUtils.get_str(getApplicationContext(), "token"),
                    title, profile, cost, address,details, time, url,kind,mId);
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
            if ( res == null ) {
                Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject j = new JSONObject(res);
                    int status = j.optInt("status");
                    if ( status == ErrorCode.SUCCESS ) {
                        finish();
                    }
                    String message = j.optString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled () {
        }
    }


    public boolean checkParams (String message, String toastStr) {
        if ( message == null || message.length() == 0 ) {
            Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick ( R.id.time )
    void selectTime (View view) {
        Calendar c = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(this,
                // 绑定监听器
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet (DatePicker view, int year,
                                           int monthOfYear, int dayOfMonth) {
                        showTimePick(year, monthOfYear, dayOfMonth);
                    }
                }
                // 设置初始日期
                , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                .get(Calendar.DAY_OF_MONTH)).show();
    }


    private void showTimePick (final int year, final int monthOfYear, final int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint ( "SetTextI18n" )
                    @Override
                    public void onTimeSet (TimePicker view,
                                           int hourOfDay, int minute) {
                        timeView.setText(year + "年" + monthOfYear
                                + "月" + dayOfMonth + "日  " + hourOfDay + "时" + minute + "分");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,monthOfYear,dayOfMonth,hourOfDay,minute);
                        time = calendar.getTimeInMillis()+"";
                        Log.d("fuck",time+"");
                    }
                }
                // 设置初始时间
                , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                // true表示采用24小时制
                true).show();
    }
}
