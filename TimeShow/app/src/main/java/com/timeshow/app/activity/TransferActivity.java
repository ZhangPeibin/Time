package com.timeshow.app.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timeshow.app.R;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.TransferCoinService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by peibin on 18-2-14.
 */

public class TransferActivity extends Activity {

    @BindView(R.id.back)
    public ImageView mImageView;

    @BindView(R.id.phone)
    public EditText mPhone;
    @BindView(R.id.password)
    public EditText mCount;
    @BindView ( R.id.title )
    public TextView mTitle;


    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_layout);
        ButterKnife.bind(this);
        mImageView.setVisibility(View.VISIBLE);
        mTitle.setText("时间转账");
    }

    @OnClick(R.id.back )
    void onBack(View view){
        finish();
    }

    @OnClick(R.id.email_sign_in_button)
    void transfer(View view){
        final String phone =mPhone.getText().toString();
        final String password = mCount.getText().toString();
        if ( phone.length() == 0 ){
            Toast.makeText(getApplicationContext(),"请填写手机号",Toast.LENGTH_SHORT).show();
            return;
        }

        if ( !checkCellphone(phone) ){
            Toast.makeText(getApplicationContext(),"请填写正确的手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
        TransferCoinTask transferCoinTask = new TransferCoinTask(phone,password);
        transferCoinTask.execute();
    }

    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        return Pattern.matches(regex,cellphone);
    }

    public class TransferCoinTask extends AsyncTask<Void, Void, String> {

        private String mPhone;
        private String mCount;

        public TransferCoinTask(String phone, String count){
            mPhone = phone;
            mCount = count;
        }

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            TransferCoinService loginRequest = retrofit.create(TransferCoinService.class);
            Call<String> request = loginRequest.transfer(SpUtils.get_str(getApplicationContext(),"token"),mPhone,mCount);
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
                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject j = new JSONObject(res);
                    int status = j.optInt("status");
                    if(status == ErrorCode.SUCCESS){
                        double blue_coin =j.optDouble("blue_coin");
                        double orange_coin =j.optDouble("orange_coin");
                    }
                    String message = j.optString("message");
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
