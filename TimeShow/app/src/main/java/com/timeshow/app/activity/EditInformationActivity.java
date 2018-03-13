package com.timeshow.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.zxing.common.StringUtils;
import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
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

public class EditInformationActivity extends Activity implements PhotoObtainHelper.OnBitmapBackListener {

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
        title.setText("编辑信息");

        String title = SpUtils.get_str(getApplicationContext(), "title");
        if ( title != null && title.length() != 0 && !"null".equals(title)) {
            nick_name.setText(title);
        }
        String profile = SpUtils.get_str(getApplicationContext(), "profile");
        if ( profile != null && profile.length() != 0 && !"null".equals(profile)) {
            profileView.setText(profile);
        }

        String mobileStr = SpUtils.get_str(getApplicationContext(), "mobile");
        if ( mobileStr != null && mobileStr.length() != 0 && !"null".equals(mobileStr)) {
            moblie.setText(mobileStr);
        }

        String cardStr = SpUtils.get_str(getApplicationContext(), "card");
        if ( cardStr != null && cardStr.length() != 0 && !"null".equals(cardStr)) {
            card.setText(cardStr);
        }

        String typeStr = SpUtils.get_str(getApplicationContext(), "type");
        if ( typeStr != null && !"null".equals(typeStr)) {
            if ( "0".equals(typeStr) ){
                inforType.setText("个人");
                type = "0";
            }else{
                inforType.setText("机构");
                type = "1";
            }
        }

        url = SpUtils.get_str(getApplicationContext(), "head");
        if ( url != null && url.length() != 0 ) {
            Glide.with(this).asBitmap().load(url).into(headView);
        }

        String phone = SpUtils.get_str(getApplicationContext(), "phone");
        loginAccount.setText(phone);
    }

    @OnClick ( R.id.edit_photo )
    void pickImage (View view) {
        mPhotoObtainHelper.show(this, headView);
        mPhotoObtainHelper.setBitmapBackListener(this);
    }

    @OnClick ( R.id.back )
    void back (View view) {
        finish();
    }

    @OnClick ( R.id.edit_infor_type )
    void selectType (View view) {
        new MaterialDialog.Builder(this)
                .title(R.string.active_title)
                .items(R.array.infor_type)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection (MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        inforType.setText(text);
                        type = position + "";
                    }
                })
                .show();
    }


    @OnClick ( R.id.exit_user )
    void postEdit (View view) {
        final String nickName = nick_name.getText().toString();
        if ( nickName.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        final String profile = profileView.getText().toString();
        if ( nickName.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请输入简介", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( url == null || url.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请设置头像", Toast.LENGTH_SHORT).show();
            return;
        }

        final String moblieStr = moblie.getText().toString();
        if ( moblieStr.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        final String cardStr = card.getText().toString();
        if ( moblieStr.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请输入身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( type == null || type.length() == 0 ) {
            Toast.makeText(getApplicationContext(), "请选择身份类型", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        UpdateUserInfoService updateUserInfoService = retrofit.create(UpdateUserInfoService.class);
        final Call<String> res = updateUserInfoService.update(SpUtils.get_str(getApplicationContext(), "token"),
                nickName, profile, url,type,moblieStr,cardStr);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                String bodyStr = response.body();
                try {
                    JSONObject j = new JSONObject(bodyStr);
                    int status = j.optInt("status");
                    if ( status == ErrorCode.SUCCESS ) {
                        SpUtils.save_str(getApplicationContext(), "title", nickName);
                        SpUtils.save_str(getApplicationContext(), "profile", profile);
                        SpUtils.save_str(getApplicationContext(), "head", url);
                        SpUtils.save_str(getApplicationContext(), "type", type);
                        SpUtils.save_str(getApplicationContext(), "mobile", moblieStr);
                        SpUtils.save_str(getApplicationContext(), "card", cardStr);
                        finish();
                    }
                    String message = j.optString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( mPhotoObtainHelper != null )
            mPhotoObtainHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBitmap (Bitmap bitmap, String path) {
        headView.setImageBitmap(bitmap);

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
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", SpUtils.get_str(getApplicationContext(), "phone") + file.getName(), requestFile);
        Call<String> request = loginRequest.uploadImage(body);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                dialog.dismiss();
                String responseBody = response.body();
                if ( responseBody == null ) {
                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject j = new JSONObject(responseBody);
                        int status = j.optInt("status");
                        if ( status == ErrorCode.SUCCESS ) {
                            mPhotoObtainHelper.dismiss();
                            url = j.optString("url");
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
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
