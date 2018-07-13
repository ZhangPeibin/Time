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
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131296407;

  private View view2131296447;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.left_text, "field 'mLeftView' and method 'selectArea'");
    target.mLeftView = Utils.castView(view, R.id.left_text, "field 'mLeftView'", TextView.class);
    view2131296407 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectArea(p0);
      }
    });
    target.commonTitle = Utils.findRequiredView(source, R.id.common_title, "field 'commonTitle'");
    view = Utils.findRequiredView(source, R.id.navigation_center_image, "method 'addActive'");
    view2131296447 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addActive(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTitle = null;
    target.mLeftView = null;
    target.commonTitle = null;

    view2131296407.setOnClickListener(null);
    view2131296407 = null;
    view2131296447.setOnClickListener(null);
    view2131296447 = null;
  }
}
