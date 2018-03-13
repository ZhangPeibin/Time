package com.timeshow.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.timeshow.app.R;
import com.timeshow.app.utils.BottomNavigationViewHelper;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    @BindView ( R.id.title )
    public TextView mTitle;

    @BindView(R.id.left_text)
    public TextView mLeftView;

    private int mLastVisibleIndex = -1;
    private HomeFragment mHomeFragment = null;
    private ActiveFragment mActiveFragment = null;
    private TimeCircleFragment mTimeCircleFragment = null;
    private MineFragment mMineFragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item) {
            switch ( item.getItemId() ) {
                case R.id.navigation_home:
                    mTitle.setText("首页");
                    showIndex(0);
                    mLeftView.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    mTitle.setText("活动");
                    mLeftView.setVisibility(View.VISIBLE);
                    showIndex(1);
                    return true;
                case R.id.navigation_notifications:
                    mTitle.setText("时间圈");
                    mLeftView.setVisibility(View.GONE);
                    showIndex(2);
                    return true;
                case R.id.navigation_my:
                    mTitle.setText("我");
                    mLeftView.setVisibility(View.GONE);
                    showIndex(3);
                    return true;
            }
            return false;
        }
    };


    @OnClick(R.id.left_text)
    void selectArea(View view){
        Intent intent = new Intent(this, CityListSelectActivity.class);
        startActivityForResult(intent, CityListSelectActivity.CITY_SELECT_RESULT_FRAG);
    }

    private void showIndex(int index){

        if (  mLastVisibleIndex == index )
            return;

        hide();

        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if ( index == 0 ){
            if ( mHomeFragment == null ){
                mHomeFragment = new HomeFragment();
                fragmentTransaction
                        .add(R.id.container,mHomeFragment);
            }
            fragmentTransaction.show(mHomeFragment);
        }else if ( index == 1 ){
            if ( mActiveFragment == null ) {
                mActiveFragment = new ActiveFragment();
                fragmentTransaction
                        .add(R.id.container,mActiveFragment);
            }
            fragmentTransaction.show(mActiveFragment);
        }else if ( index == 2 ){
            if ( mTimeCircleFragment == null ) {
                mTimeCircleFragment = new TimeCircleFragment();
                fragmentTransaction
                        .add(R.id.container,mTimeCircleFragment);
            }
            fragmentTransaction.show(mTimeCircleFragment);
        }else if ( index == 3 ){
            if ( mMineFragment == null ) {
                mMineFragment = new MineFragment();
                fragmentTransaction
                        .add(R.id.container,mMineFragment);
            }
            fragmentTransaction.show(mMineFragment);
        }

        mLastVisibleIndex = index;
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hide(){
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        if ( mHomeFragment != null )
            fragmentTransaction.hide(mHomeFragment);
        if ( mActiveFragment != null )
            fragmentTransaction.hide(mActiveFragment);
        if ( mTimeCircleFragment != null )
            fragmentTransaction.hide(mTimeCircleFragment);
        if ( mMineFragment != null )
            fragmentTransaction.hide(mMineFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        CityListLoader.getInstance().loadCityData(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        showIndex(0);
        mTitle.setText("首页");
    }


    @OnClick(R.id.navigation_center_image)
    void addActive(View view){
        Intent intent = new Intent(this,AddActiveActivity.class);
        startActivity(intent);
    }

    private long mBackPress = 0;

    @Override
    public void onBackPressed () {
        if ( mBackPress + 2000 > System.currentTimeMillis() ) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
        }
        mBackPress = System.currentTimeMillis();
    }
    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted (1)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", 1, perms);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CityListSelectActivity.CITY_SELECT_RESULT_FRAG) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                Bundle bundle = data.getExtras();

                CityInfoBean cityInfoBean = (CityInfoBean) bundle.getParcelable("cityinfo");

                if (null == cityInfoBean) {
                    return;
                }

                mLeftView.setText(cityInfoBean.getName());
                if ( mActiveFragment != null ){
                    mActiveFragment.setArea(cityInfoBean.getName());
                }
            }
        }
    }
}
