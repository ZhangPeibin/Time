package com.timeshow.app.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timeshow.app.R;
import com.timeshow.app.adapter.HistoryAdapter;
import com.timeshow.app.model.HistoryModel;
import com.timeshow.app.request.ErrorCode;
import com.timeshow.app.request.TransactionHistoryService;
import com.timeshow.app.request.TransferCoinService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
public class TransactionHistoryActivity extends Activity {

    @BindView(R.id.listview)
    public ListView mListView;
    @BindView(R.id.back)
    public ImageView mImageView;
    private HistoryAdapter mHistoryAdapter;
    @BindView ( R.id.title )
    public TextView mTitle;
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_layout);
        ButterKnife.bind(this);
        mTitle.setText("交易查询");
        mImageView.setVisibility(View.VISIBLE);
        TransferCoinTask transferCoinTask = new TransferCoinTask();
        transferCoinTask.execute();
        mHistoryAdapter = new HistoryAdapter(getApplicationContext());
        mListView.setAdapter(mHistoryAdapter);
    }

    @OnClick (R.id.back )
    void onBack(View view){
        finish();
    }

    public class TransferCoinTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground (Void... params) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create() ).build();
            TransactionHistoryService loginRequest = retrofit.create(TransactionHistoryService.class);
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
                        Gson gson = new Gson();
                        List<HistoryModel> historyModels = gson.fromJson(list.toString(),new TypeToken<List<HistoryModel>>(){}.getType());
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
