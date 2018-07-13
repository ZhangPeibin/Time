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

public class TransferActivity_ViewBinding implements Unbinder {
  private TransferActivity target;

  private View view2131296287;

  private View view2131296357;

  @UiThread
  public TransferActivity_ViewBinding(TransferActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TransferActivity_ViewBinding(final TransferActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.back, "field 'mImageView' and method 'onBack'");
    target.mImageView = Utils.castView(view, R.id.back, "field 'mImageView'", ImageView.class);
    view2131296287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack(p0);
      }
    });
    target.mPhone = Utils.findRequiredViewAsType(source, R.id.phone, "field 'mPhone'", EditText.class);
    target.mCount = Utils.findRequiredViewAsType(source, R.id.password, "field 'mCount'", EditText.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.email_sign_in_button, "method 'transfer'");
    view2131296357 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.transfer(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TransferActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mImageView = null;
    target.mPhone = null;
    target.mCount = null;
    target.mTitle = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
    view2131296357.setOnClickListener(null);
    view2131296357 = null;
  }
}
