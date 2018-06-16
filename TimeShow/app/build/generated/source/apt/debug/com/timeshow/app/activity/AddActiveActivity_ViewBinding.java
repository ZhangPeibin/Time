// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddActiveActivity_ViewBinding implements Unbinder {
  private AddActiveActivity target;

  private View view2131296287;

  private View view2131296279;

  private View view2131296539;

  private View view2131296388;

  private View view2131296400;

  private View view2131296356;

  @UiThread
  public AddActiveActivity_ViewBinding(AddActiveActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddActiveActivity_ViewBinding(final AddActiveActivity target, View source) {
    this.target = target;

    View view;
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'back'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back(p0);
      }
    });
    target.titleView = Utils.findRequiredViewAsType(source, R.id.atitle, "field 'titleView'", EditText.class);
    target.profileView = Utils.findRequiredViewAsType(source, R.id.profile, "field 'profileView'", EditText.class);
    target.costView = Utils.findRequiredViewAsType(source, R.id.money, "field 'costView'", EditText.class);
    view = Utils.findRequiredView(source, R.id.address, "field 'addressView' and method 'selectAddress'");
    target.addressView = Utils.castView(view, R.id.address, "field 'addressView'", TextView.class);
    view2131296279 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectAddress(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.time, "field 'timeView' and method 'selectTime'");
    target.timeView = Utils.castView(view, R.id.time, "field 'timeView'", TextView.class);
    view2131296539 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectTime(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.image, "field 'image' and method 'pickImage'");
    target.image = Utils.castView(view, R.id.image, "field 'image'", ImageView.class);
    view2131296388 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickImage(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.kind, "field 'kindView' and method 'kind'");
    target.kindView = Utils.castView(view, R.id.kind, "field 'kindView'", TextView.class);
    view2131296400 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.kind(p0);
      }
    });
    target.detailView = Utils.findRequiredViewAsType(source, R.id.details_address, "field 'detailView'", EditText.class);
    view = Utils.findRequiredView(source, R.id.email_sign_in_button, "method 'post'");
    view2131296356 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.post(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddActiveActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.back = null;
    target.titleView = null;
    target.profileView = null;
    target.costView = null;
    target.addressView = null;
    target.timeView = null;
    target.image = null;
    target.kindView = null;
    target.detailView = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
    view2131296279.setOnClickListener(null);
    view2131296279 = null;
    view2131296539.setOnClickListener(null);
    view2131296539 = null;
    view2131296388.setOnClickListener(null);
    view2131296388 = null;
    view2131296400.setOnClickListener(null);
    view2131296400 = null;
    view2131296356.setOnClickListener(null);
    view2131296356 = null;
  }
}
