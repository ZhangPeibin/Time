// Generated code from Butter Knife. Do not modify!
package com.timeshow.app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view2131165221;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.mPhone = Utils.findRequiredViewAsType(source, R.id.phone, "field 'mPhone'", EditText.class);
    target.mPassWord = Utils.findRequiredViewAsType(source, R.id.password, "field 'mPassWord'", EditText.class);
    view = Utils.findRequiredView(source, R.id.email_sign_in_button, "method 'login'");
    view2131165221 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPhone = null;
    target.mPassWord = null;

    view2131165221.setOnClickListener(null);
    view2131165221 = null;
  }
}
