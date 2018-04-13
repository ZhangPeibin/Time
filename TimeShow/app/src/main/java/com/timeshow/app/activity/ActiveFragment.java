package com.timeshow.app.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.view.ViewCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timeshow.app.R;
import com.timeshow.app.adapter.ActiveAdapter;
import com.timeshow.app.adapter.HistoryAdapter;
import com.timeshow.app.model.ActiveModel;
import com.timeshow.app.request.GetActiveService;
import com.timeshow.app.request.PayActiveCoinService;
import com.timeshow.app.request.PayCoinService;
import com.timeshow.app.request.PostActiveService;
import com.timeshow.app.request.TransferCoinService;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveFragment extends Fragment implements TabLayout.OnTabSelectedListener, AdapterView.OnItemClickListener {

    @BindView ( R.id.tl_tab )
    public TabLayout mTabLayout;
    @BindView ( R.id.listview )
    public ListView mListView;

    private ActiveAdapter mActiveAdapter;

    public static final List<String> kind = new ArrayList<String>() {
        {
            add("分享");
            add("需求");
            add("公益");
        }
    };

    private int type = 0;

    private String city = null;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume () {
        super.onResume();
        request(type, false);
    }

    public void init () {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), android.R.color.black),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));
        ViewCompat.setElevation(mTabLayout, 10);

        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText(kind.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(kind.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(kind.get(2)));
        mTabLayout.addOnTabSelectedListener(this);

        mActiveAdapter = new ActiveAdapter(getActivity());
        mListView.setAdapter(mActiveAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onTabSelected (TabLayout.Tab tab) {
        int position = tab.getPosition();
        if ( position == type ) {
            return;
        }
        type = position;
        mActiveAdapter.clear();
        request(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected (TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected (TabLayout.Tab tab) {

    }


    private void request (int type, boolean show) {
        MaterialDialog dialog = null;
        if ( show ) {
            dialog = new MaterialDialog.Builder(getActivity())
                    .content("正在请求中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        GetActiveService getActiveService = retrofit.create(GetActiveService.class);
        Call<String> request = getActiveService.get(SpUtils.get_str(getActivity().getApplicationContext(), "token"),
                type + "",city);
        final MaterialDialog finalDialog = dialog;
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                if ( finalDialog != null )
                    finalDialog.dismiss();
                String body = response.body();
                try {
                    if ( body == null )
                        return;
                    JSONObject j = new JSONObject(body);
                    int status = j.optInt("status");
                    if ( status != 0 ) {
                        return;
                    }
                    JSONArray jsonArray = j.optJSONArray("list");
                    List<ActiveModel> activeModels =
                            new Gson().fromJson(jsonArray.toString(), new TypeToken<List<ActiveModel>>() {
                            }.getType());
                    mActiveAdapter.notifyDataChanged(activeModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                if ( finalDialog != null )
                    finalDialog.dismiss();
                Toast.makeText(getActivity(), "服务器错误,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        ActiveModel activeModel = (ActiveModel) mActiveAdapter.getItem(position);
        if ( activeModel != null ) {
            String phone = SpUtils.get_str(getActivity(), "phone");
            if ( "2".equals(activeModel.status) ){
                if (activeModel.phone.equals(phone)){
                    paypay(activeModel.phone, activeModel.cost,activeModel.id);
                }
            }else{
                if (activeModel.phone.equals(phone)){
                    Toast.makeText(getActivity(), "不能参加自己发布的活动", Toast.LENGTH_SHORT).show();
                }else{
                    payfor(activeModel.phone, activeModel.cost, activeModel.id);
                }
            }
        }
    }

    private void paypay (final String phone, final String cost, final String id) {
        new MaterialDialog.Builder(getActivity())
                .title("支付提示")
                .content("确定向" + phone + "用户支付" + cost + "时间")
                .positiveText("确定")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        paypayRequest(phone, cost, id);
                    }
                }).show();
    }

    private void paypayRequest (String phone, String cost, String id) {
        final MaterialDialog dialog  = new MaterialDialog.Builder(getActivity())
                .content("正在请求中...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        PayActiveCoinService loginRequest = retrofit.create(PayActiveCoinService.class);
        Call<String> request = loginRequest.transfer(SpUtils.get_str(getActivity(), "token"), phone, cost, id);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                dialog.dismiss();
                String result = response.body();
                if ( result == null ) {
                    Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject j = new JSONObject(result);
                        int status = j.optInt("status");
                        String message = j.optString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void payfor (final String phone, final String cost, final String id) {
        new MaterialDialog.Builder(getActivity())
                .title("支付提示")
                .content("确定向" + phone + "用户收" + cost + "时间")
                .positiveText("确定")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        pay(phone, cost, id);
                    }
                }).show();
    }

    private void pay (String phone, String cost, String id) {
        final MaterialDialog dialog  = new MaterialDialog.Builder(getActivity())
                    .content("正在请求中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        PayCoinService loginRequest = retrofit.create(PayCoinService.class);
        Call<String> request = loginRequest.transfer(SpUtils.get_str(getActivity(), "token"), phone, cost, id);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                dialog.dismiss();
                String result = response.body();
                if ( result == null ) {
                    Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject j = new JSONObject(result);
                        int status = j.optInt("status");
                        String message = j.optString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure (Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setArea (String name) {
        city = name;
        request(type,false);
    }
}
