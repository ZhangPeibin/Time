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

public class AboutActivity_ViewBinding implements Unbinder {
  private AboutActivity target;

  private View view2131296287;

  @UiThread
  public AboutActivity_ViewBinding(AboutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AboutActivity_ViewBinding(final AboutActivity target, View source) {
    this.target = target;

    View view;
    target.about = Utils.findRequiredViewAsType(source, R.id.version, "field 'about'", TextView.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'mImageView' and method 'back'");
    target.mImageView = Utils.castView(view, R.id.back, "field 'mImageView'", ImageView.class);
    view2131296287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.about = null;
    target.mImageView = null;
    target.mTitle = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
  }
}
