// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MineFragment_ViewBinding implements Unbinder {
  private MineFragment target;

  private View view2131296256;

  private View view2131296397;

  private View view2131296363;

  private View view2131296374;

  @UiThread
  public MineFragment_ViewBinding(final MineFragment target, View source) {
    this.target = target;

    View view;
    target.user_title = Utils.findRequiredViewAsType(source, R.id.user_title, "field 'user_title'", TextView.class);
    target.user_profile = Utils.findRequiredViewAsType(source, R.id.user_profile, "field 'user_profile'", TextView.class);
    target.iconImg = Utils.findRequiredViewAsType(source, R.id.iconImg, "field 'iconImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.about, "method 'about'");
    view2131296256 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.about(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.information, "method 'exit'");
    view2131296397 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.exit(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.exit_user, "method 'bye'");
    view2131296363 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bye(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.friend, "method 'friend'");
    view2131296374 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.friend(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MineFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.user_title = null;
    target.user_profile = null;
    target.iconImg = null;

    view2131296256.setOnClickListener(null);
    view2131296256 = null;
    view2131296397.setOnClickListener(null);
    view2131296397 = null;
    view2131296363.setOnClickListener(null);
    view2131296363 = null;
    view2131296374.setOnClickListener(null);
    view2131296374 = null;
  }
}
