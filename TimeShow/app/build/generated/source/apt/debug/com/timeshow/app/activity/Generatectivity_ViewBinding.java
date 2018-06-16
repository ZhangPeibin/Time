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

public class Generatectivity_ViewBinding implements Unbinder {
  private Generatectivity target;

  private View view2131296287;

  @UiThread
  public Generatectivity_ViewBinding(Generatectivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Generatectivity_ViewBinding(final Generatectivity target, View source) {
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
    target.mTips = Utils.findRequiredViewAsType(source, R.id.tops, "field 'mTips'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Generatectivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.back = null;
    target.mTips = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
  }
}
