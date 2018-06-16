// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view2131296356;

  private View view2131296474;

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
    view = Utils.findRequiredView(source, R.id.email_sign_in_button, "field 'mButton' and method 'login'");
    target.mButton = Utils.castView(view, R.id.email_sign_in_button, "field 'mButton'", Button.class);
    view2131296356 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
    view = Utils.findRequiredView(source, R.id.register, "method 'register'");
    view2131296474 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.register(p0);
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
    target.mButton = null;

    view2131296356.setOnClickListener(null);
    view2131296356 = null;
    view2131296474.setOnClickListener(null);
    view2131296474 = null;
  }
}
