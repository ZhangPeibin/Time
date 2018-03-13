package com.timeshow.app.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.timeshow.app.R;
import com.timeshow.app.request.TransferCoinService;
import com.timeshow.app.request.TransferGetCoinService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = ScanActivity.class.getSimpleName();

    private QRCodeView mQRCodeView;

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void onStart () {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop () {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy () {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate () {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess (String result) {
        vibrate();
        mQRCodeView.startSpot();
        try {
            JSONObject j = new JSONObject(result);
            if ( !j.has("phone") ){
                Toast.makeText(getApplicationContext(),"暂不支持此二维码",Toast.LENGTH_SHORT).show();
            }else{
                final String phone = j.optString("phone");
                final String count = j.optString("count");
                final String reason = j.optString("reason");
                final int type = j.optInt("type");
                if ( type == 0 ){
                    //此二维码是付款二维码
                    //phone对应的人减去这个count
                    //我加上这个count
                    TransferGetCoinTask transferCoinTask = new TransferGetCoinTask(phone,count,reason);
                    transferCoinTask.execute();
                }else if ( type == 1 ){
                    //此二维码是收款二维码
                    new MaterialDialog.Builder(this)
                            .title("转账二维码")
                            .content("是否向"+phone+"转账"+count)
                            .positiveText("转账")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    TransferCoinTask transferCoinTask = new TransferCoinTask(phone,count);
                                    transferCoinTask.execute();
                                }
                            })
                            .show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"暂不支持此二维码",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError () {
        Log.e(TAG, "打开相机出错");
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
    }

    public class TransferGetCoinTask extends AsyncTask<Void, Void, String> {

        private String mPhone;
        private String mCount;
        private String reason;

        public TransferGetCoinTask (String phone, String count, String reason){
            mPhone = phone;
            mCount = count;
            this.reason = reason;
        }

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            TransferGetCoinService loginRequest = retrofit.create(TransferGetCoinService.class);
            Call<String> request = loginRequest.get(SpUtils.get_str(getApplicationContext(),"token"),
                    mPhone,mCount,reason);
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
