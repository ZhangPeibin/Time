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

  private View view2131296375;

  private View view2131296486;

  private View view2131296487;

  private View view2131296374;

  private View view2131296506;

  private View view2131296553;

  private View view2131296540;

  private View view2131296494;

  private View view2131296541;

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
    view2131296375 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.get(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.save, "method 'save'");
    view2131296486 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.save(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.scan, "method 'onScan'");
    view2131296487 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onScan(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.fushijian, "method 'fushijian'");
    view2131296374 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.fushijian(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shoushijian, "method 'shoushijian'");
    view2131296506 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.shoushijian(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.translate, "method 'translate'");
    view2131296553 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.translate(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.timeget, "method 'timeget'");
    view2131296540 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.timeget(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_badge, "method 'searchBadge'");
    view2131296494 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.searchBadge(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.timme_money, "method 'timme_money'");
    view2131296541 = view;
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

    view2131296375.setOnClickListener(null);
    view2131296375 = null;
    view2131296486.setOnClickListener(null);
    view2131296486 = null;
    view2131296487.setOnClickListener(null);
    view2131296487 = null;
    view2131296374.setOnClickListener(null);
    view2131296374 = null;
    view2131296506.setOnClickListener(null);
    view2131296506 = null;
    view2131296553.setOnClickListener(null);
    view2131296553 = null;
    view2131296540.setOnClickListener(null);
    view2131296540 = null;
    view2131296494.setOnClickListener(null);
    view2131296494 = null;
    view2131296541.setOnClickListener(null);
    view2131296541 = null;
  }
}
