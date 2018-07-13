package com.timeshow.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.timeshow.app.R;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.UpdateUserInfoService;
import com.timeshow.app.request.UploadImageService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.PhotoObtainHelper;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
 * Created by peibin on 18-2-23.
 */

public class InformationActivity extends Activity{
    public static final String PHONE = "phone";

    @BindView ( R.id.title )
    public TextView title;

    @BindView ( R.id.back )
    public ImageView back;

    @BindView ( R.id.nick_name )
    public EditText nick_name;

    @BindView ( R.id.profile )
    public EditText profileView;

    @BindView ( R.id.iconImg )
    public ImageView headView;

    //会员号
    @BindView ( R.id.pay_card )
    public TextView loginAccount;
    //会员类型
    @BindView ( R.id.infor_type )
    public TextView inforType;
    //手机号
    @BindView ( R.id.phone )
    public EditText moblie;

    @BindView ( R.id.card )
    public EditText card;

    PhotoObtainHelper mPhotoObtainHelper;

    public String url;

    private String type;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editinformation);
        ButterKnife.bind(this);
        mPhotoObtainHelper = new PhotoObtainHelper();
        back.setVisibility(View.VISIBLE);
        title.setText("查看信息");
        String phone = getIntent().getStringExtra(PHONE);
        moblie.setText(phone);
    }



}
