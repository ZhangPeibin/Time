package com.timeshow.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timeshow.app.R;
import com.timeshow.app.adapter.FriendAdapter;
import com.timeshow.app.adapter.HistoryAdapter;
import com.timeshow.app.model.HistoryModel;
import com.timeshow.app.request.AddFriendService;
import com.timeshow.app.request.DeleteFriendService;
import com.timeshow.app.request.MyFriendService;
import com.timeshow.app.request.TransactionHistoryService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by peibin on 18-2-14.
 */
public class MyFriendActivity extends Activity {

    @BindView(R.id.listview)
    public ListView mListView;
    @BindView(R.id.back)
    public ImageView mImageView;
    private FriendAdapter mHistoryAdapter;
    @BindView ( R.id.title )
    public TextView mTitle;
    @BindView(R.id.right_text)
    public TextView rightTitle;
    @BindView(R.id.input)
    public TextView input;

    private List<String> oldData = new ArrayList<>();

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_layout);
        ButterKnife.bind(this);
        mTitle.setText("我的好友");
        mImageView.setVisibility(View.VISIBLE);
        rightTitle.setVisibility(View.VISIBLE);
        TransferCoinTask transferCoinTask = new TransferCoinTask();
        transferCoinTask.execute();
        mHistoryAdapter = new FriendAdapter(this);
        mListView.setAdapter(mHistoryAdapter);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                String search = s.toString();
                if ( count == 0 ){
                    mHistoryAdapter.notifyDataChanged(oldData);
                }else{
                    List<String> searchResult = new ArrayList<>();
                    for ( String oldStr : oldData ){
                        if ( search.equals(oldStr) ){
                            searchResult.add(oldStr);
                        }
                    }
                    mHistoryAdapter.notifyDataChanged(searchResult);
                }
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                String a = (String) mHistoryAdapter.getItem(position);
                Intent i = new Intent(MyFriendActivity.this,InformationActivity.class);
                i.putExtra(InformationActivity.PHONE,a);
                startActivity(i);
            }
        });
    }

    @OnClick (R.id.back )
    void onBack(View view){
        finish();
    }

    @OnClick(R.id.right_text)
    void add(View view){
        new MaterialDialog.Builder(this)
                .title("请输入你要添加的好友的手机号")
                .positiveText("确定")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("要添加好用的手机号", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
                        AddFriendService loginRequest = retrofit.create(AddFriendService.class);
                        Call<String> request = loginRequest.add(SpUtils.get_str(getApplicationContext(),"token"),
                                input.toString());
                        request.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse (Call<String> call, Response<String> response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body());
                                    int status = jsonObject.optInt("status");
                                    String message = jsonObject.optString("message");
                                    if ( status != 0 ){
                                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                TransferCoinTask transferCoinTask = new TransferCoinTask();
                                transferCoinTask.execute();
                            }

                            @Override
                            public void onFailure (Call<String> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show();
    }

    public void delete (String rphone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
        DeleteFriendService loginRequest = retrofit.create(DeleteFriendService.class);
        Call<String> request = loginRequest.add(SpUtils.get_str(getApplicationContext(),"token"),
                rphone);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if ( status != 0 ){
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                    TransferCoinTask transferCoinTask = new TransferCoinTask();
                    transferCoinTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_SHORT).show();
            }
        });
    }







    public class TransferCoinTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            MyFriendService loginRequest = retrofit.create(MyFriendService.class);
            Call<String> request = loginRequest.get(SpUtils.get_str(getApplicationContext(),"token"));
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
                    JSONObject jsonObject = new JSONObject(res);
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if ( status != 0 ){
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }else{
                        JSONArray list = jsonObject.optJSONArray("list");
                        List<String> historyModels = new ArrayList<>();
                        String phone = SpUtils.get_str(getApplicationContext(),"phone");
                        for (int i = 0;i<list.length();i++ ){
                            JSONObject j = list.optJSONObject(i);
                            String rphone = j.optString("rphone");
                            if ( phone != null && !phone.equals(rphone) ){
                                historyModels.add(j.optString("rphone"));
                            }
                        }
                        oldData.clear();
                        oldData.addAll(historyModels);
                        mHistoryAdapter.notifyDataChanged(historyModels);
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
}
