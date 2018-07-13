// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import com.youth.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeFragment_ViewBinding implements Unbinder {
  private HomeFragment target;

  private View view2131296376;

  private View view2131296488;

  private View view2131296489;

  private View view2131296375;

  private View view2131296510;

  private View view2131296557;

  private View view2131296544;

  private View view2131296496;

  private View view2131296545;

  @UiThread
  public HomeFragment_ViewBinding(final HomeFragment target, View source) {
    this.target = target;

    View view;
    target.mBlueText = Utils.findRequiredViewAsType(source, R.id.blue_money, "field 'mBlueText'", TextView.class);
    target.mOrangeText = Utils.findRequiredViewAsType(source, R.id.cheng_money, "field 'mOrangeText'", TextView.class);
    target.giftText = Utils.findRequiredViewAsType(source, R.id.gift_count, "field 'giftText'", TextView.class);
    target.time = Utils.findRequiredViewAsType(source, R.id.time, "field 'time'", TextView.class);
    target.mBanner = Utils.findRequiredViewAsType(source, R.id.banner, "field 'mBanner'", Banner.class);
    view = Utils.findRequiredView(source, R.id.get, "method 'get'");
    view2131296376 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.get(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.save, "method 'save'");
    view2131296488 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.save(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.scan, "method 'onScan'");
    view2131296489 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onScan(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.fushijian, "method 'fushijian'");
    view2131296375 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.fushijian(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shoushijian, "method 'shoushijian'");
    view2131296510 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.shoushijian(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.translate, "method 'translate'");
    view2131296557 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.translate(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.timeget, "method 'timeget'");
    view2131296544 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.timeget(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_badge, "method 'searchBadge'");
    view2131296496 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchBadge(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.timme_money, "method 'timme_money'");
    view2131296545 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.timme_money(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mBlueText = null;
    target.mOrangeText = null;
    target.giftText = null;
    target.time = null;
    target.mBanner = null;

    view2131296376.setOnClickListener(null);
    view2131296376 = null;
    view2131296488.setOnClickListener(null);
    view2131296488 = null;
    view2131296489.setOnClickListener(null);
    view2131296489 = null;
    view2131296375.setOnClickListener(null);
    view2131296375 = null;
    view2131296510.setOnClickListener(null);
    view2131296510 = null;
    view2131296557.setOnClickListener(null);
    view2131296557 = null;
    view2131296544.setOnClickListener(null);
    view2131296544 = null;
    view2131296496.setOnClickListener(null);
    view2131296496 = null;
    view2131296545.setOnClickListener(null);
    view2131296545 = null;
  }
}
