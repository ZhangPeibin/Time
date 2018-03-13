package com.timeshow.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.timeshow.app.R;
import com.timeshow.app.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {


    @BindView(R.id.user_title)
    public TextView user_title;
    @BindView(R.id.user_profile)
    public TextView user_profile;
    @BindView(R.id.iconImg)
    public ImageView iconImg;




    public MineFragment () {
        // Required empty public constructor
    }


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onResume () {
        super.onResume();
        update();
    }

    private void update(){
        String title = SpUtils.get_str(getActivity(),"title");
        if ( title!=null && title.length() != 0 && !"null".equals(title)){
            user_title.setText(title);
        }else{
            user_title.setText("未设置");
        }
        String profile = SpUtils.get_str(getActivity(),"profile");
        if ( profile!=null && profile.length() != 0 && !"null".equals(profile)){
            user_profile.setText(profile);
        }else{
            user_profile.setText("未设置");
        }

        String url = SpUtils.get_str(getActivity(),"head");
        if ( url != null && url.length()!=0){
            Glide.with(this).asBitmap().load(url).into(iconImg);
        }
    }

    @OnClick(R.id.about)
    void about(View view){
        Intent intent = new Intent(getActivity(),AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.information)
    void exit(View view){

        Intent intent = new Intent(getActivity(),EditInformationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.exit_user)
    void bye(View view){
        SpUtils.clear(getActivity());
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.friend)
    void friend(View view){
        Intent intent = new Intent(getActivity(),MyFriendActivity.class);
        startActivity(intent);
    }
}
