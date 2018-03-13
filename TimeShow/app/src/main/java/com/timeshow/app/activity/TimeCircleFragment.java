package com.timeshow.app.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.timeshow.app.R;
import com.timeshow.app.adapter.TimeCircleAdapter;
import com.timeshow.app.model.ActiveModel;
import com.timeshow.app.model.FriendActiveModel;
import com.timeshow.app.request.CommentActivesService;
import com.timeshow.app.request.GetActiveService;
import com.timeshow.app.request.GetFriendActivesService;
import com.timeshow.app.request.GoodeActivesService;
import com.timeshow.app.request.Urls;
import com.timeshow.app.utils.SpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class TimeCircleFragment extends Fragment {

    @BindView(R.id.listview)
    public ListView mListView;
    public TimeCircleAdapter mTimeCircleAdapter;
    View headView;
    TextView profileView;
    TextView titleView;
    ImageView iconImg;

    private boolean first = true;

    public TimeCircleFragment () {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_time_circle, container, false);
        ButterKnife.bind(this,v);
        mTimeCircleAdapter = new TimeCircleAdapter(getActivity(),this);
        headView = getLayoutInflater().inflate(R.layout.time_circle_head,null);
        profileView = (TextView) headView.findViewById(R.id.profile);
        titleView = (TextView) headView.findViewById(R.id.nick_name);
        iconImg = (ImageView) headView.findViewById(R.id.iconImg);
        mListView.addHeaderView(headView);
        mListView.setAdapter(mTimeCircleAdapter);
        return v;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ( first ){
            request(true);
            first = false;
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        update();
        if ( !first )
            request(false);
    }

    private void update(){
        String title = SpUtils.get_str(getActivity(),"title");
        if ( title!=null && title.length() != 0 && !"null".equals(title)){
            titleView.setText(title);
        }else{
            String phone = SpUtils.get_str(getActivity(),"phone");
            titleView.setText(phone);
        }
        String profile = SpUtils.get_str(getActivity(),"profile");
        if ( profile!=null && profile.length() != 0 && !"null".equals(profile)){
            profileView.setText(profile);
        }

        String url = SpUtils.get_str(getActivity(),"head");
        if ( url != null && url.length()!=0){
            Glide.with(this).asBitmap().load(url).into(iconImg);
        }
    }

    public void good (String id) {
        goodRequest(id);
    }

    public void comment (final String id) {
        new MaterialDialog.Builder(getActivity())
                .title("请输入评论内容")
                .positiveText("确定")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("说点什么...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        commentRequest(id,input.toString());
                    }
                }).show();
    }

    private void request (boolean show) {
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
        GetFriendActivesService getActiveService = retrofit.create(GetFriendActivesService.class);
        Call<String> request = getActiveService.get(SpUtils.get_str(getActivity().getApplicationContext(), "token"));
        final MaterialDialog finalDialog = dialog;
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                if ( finalDialog != null )
                    finalDialog.dismiss();
                String body = response.body();
                try {
                    JSONObject j = new JSONObject(body);
                    int status = j.optInt("status");
                    if ( status != 0 ) {
                        return;
                    }
                    JSONArray jsonArray = j.optJSONArray("list");
                    List<FriendActiveModel> activeModels =
                            new Gson().fromJson(jsonArray.toString(), new TypeToken<List<FriendActiveModel>>() {
                            }.getType());
                    mTimeCircleAdapter.notifyDataChanged(activeModels);
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

    private void goodRequest (String  id) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .content("正在请求中...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        GoodeActivesService getActiveService = retrofit.create(GoodeActivesService.class);
        Call<String> request = getActiveService.get(SpUtils.get_str(getActivity().getApplicationContext(), "token"),id);
        final MaterialDialog finalDialog = dialog;
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                if ( finalDialog != null )
                    finalDialog.dismiss();
                String body = response.body();
                try {
                    JSONObject j = new JSONObject(body);
                    int status = j.optInt("status");
                    if ( status != 0 ) {
                        return;
                    }
                    String message = j.optString("message");
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    request(false);
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

    private void commentRequest (String  id,String content) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .content("正在请求中...")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL).addConverterFactory(ScalarsConverterFactory.create()).build();
        CommentActivesService getActiveService = retrofit.create(CommentActivesService.class);
        Call<String> request = getActiveService.get(SpUtils.get_str(getActivity().getApplicationContext(), "token"),
                id,content);
        final MaterialDialog finalDialog = dialog;
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse (Call<String> call, Response<String> response) {
                if ( finalDialog != null )
                    finalDialog.dismiss();
                String body = response.body();
                try {
                    JSONObject j = new JSONObject(body);
                    int status = j.optInt("status");
                    if ( status != 0 ) {
                        return;
                    }
                    String message = j.optString("message");
                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                    request(false);
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
}
