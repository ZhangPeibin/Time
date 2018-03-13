package com.timeshow.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.timeshow.app.R;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.LoginService;
import com.timeshow.app.request.RegisterService;
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
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    @BindView( R.id.phone)
    public EditText mPhone;
    @BindView(R.id.password)
    public EditText mPassWord;

    @BindView(R.id.back)
    public ImageView back;
    @BindView(R.id.title)
    public TextView title;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        back.setVisibility(View.VISIBLE);
        title.setText("注册");
    }

    @OnClick(R.id.back)
    public void back(View view){
        finish();
    }


    @OnClick(R.id.register)
    void login(){
        final String phone =mPhone.getText().toString();
        final String password = mPassWord.getText().toString();
        if ( phone.length() == 0 ){
            Toast.makeText(getApplicationContext(),"请填写手机号",Toast.LENGTH_SHORT).show();
            return;
        }

        if ( !checkCellphone(phone) ){
            Toast.makeText(getApplicationContext(),"请填写正确的手机号码",Toast.LENGTH_SHORT).show();
            return;
        }

        if ( !isPasswordValid(password) ){
            Toast.makeText(getApplicationContext(),"请填写至少6位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        UserLoginTask userLoginTask = new UserLoginTask(phone,password);
        userLoginTask.execute();
    }

    private boolean isPasswordValid (String password) {
        return password.length() > 5;
    }

    public static boolean checkCellphone(String cellphone) {
//        String regex = "^134[0-8]\\d{7}$|^13[^4]\\d{8}$|^14[5-9]\\d{8}$|^15[^4]\\d{8}$|^16[6]\\d{8}$|^17[0-8]\\d{8}$|^18[\\d]{9}$|^19[8,9]\\d{8}$";
//        return Pattern.matches(regex,cellphone);
        return cellphone.length() == 11;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mPhone;
        private final String mPassword;

        UserLoginTask (String email, String password) {
            mPhone = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            RegisterService loginRequest = retrofit.create(RegisterService.class);
            Call<String> request = loginRequest.login(mPhone,mPassword);
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
                Log.d(RegisterActivity.class.getSimpleName(),res);
                try {
                    JSONObject j = new JSONObject(res);
                    String message = j.optString("message");
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    int status = j.optInt("status");
                    if(status == ErrorCode.SUCCESS){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onCancelled () {
        }
    }

    private void toHomePage () {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }
}

