package com.timeshow.app.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.timeshow.app.R;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class Generatectivity extends AppCompatActivity {

    public static final String TYPE  = "type";

    private ImageView mChineseIv;

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.back)
    public ImageView back;

    @BindView(R.id.tops)
    public TextView mTips;

    int type = -1;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_generate);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.back)
    void back(View v){
        finish();
    }

    private void initView () {
        type = getIntent().getIntExtra(TYPE,-1);
        if ( type == -1 ){
            Toast.makeText(getApplicationContext(),"参数错误",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if ( type == 0 ){
            title.setText("付时间");
            mTips.setText("向别人付时间");
        }else if ( type == 1 ){
            title.setText("收时间");
            mTips.setText("扫一扫,向我付时间");
        }
        back.setVisibility(View.VISIBLE);
        mChineseIv = (ImageView) findViewById(R.id.iv_chinese);
        String title = type == 0?"请输入付出时间金额":"请输入收取时间金额";
        String hint = type == 0?"请输入你要付出的时间金额":"请输入你要收取的时间金额";
        new MaterialDialog.Builder(this)
                .title(title)
                .positiveText("确定")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(hint, "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        final String count = input.toString();
                        if ( type == 0 ){
                            //理由
                            new MaterialDialog.Builder(Generatectivity.this)
                                    .title("请输入付时间备注")
                                    .positiveText("确定")
                                    .inputType(InputType.TYPE_CLASS_TEXT)
                                    .input("请输入...", "", new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(MaterialDialog dialog, CharSequence input) {
                                            // Do something
                                            createChineseQRCode(input.toString(),count);
                                        }
                                    }).show();

                        }else{
                            createChineseQRCode(null,input.toString());
                        }
                    }
                }).show();


    }


    private void createChineseQRCode (final String reason, final String s) {
        String phone = SpUtils.get_str(getApplicationContext(),"phone");
        JSONObject j = new JSONObject();
        try {
            j.put("phone",phone);
            j.put("count",s);
            j.put("type",type);
            if ( reason != null ){
                j.put("reason",reason);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String f= j.toString();
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground (Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(f, BGAQRCodeUtil.dp2px(Generatectivity.this, 150));
            }

            @Override
            protected void onPostExecute (Bitmap bitmap) {
                if ( bitmap != null ) {
                    mChineseIv.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(Generatectivity.this, "生成中文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

}