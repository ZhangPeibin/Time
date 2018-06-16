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

public class EditInformationActivity_ViewBinding implements Unbinder {
  private EditInformationActivity target;

  private View view2131296287;

  private View view2131296354;

  private View view2131296349;

  private View view2131296362;

  @UiThread
  public EditInformationActivity_ViewBinding(EditInformationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditInformationActivity_ViewBinding(final EditInformationActivity target, View source) {
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
    target.nick_name = Utils.findRequiredViewAsType(source, R.id.nick_name, "field 'nick_name'", EditText.class);
    target.profileView = Utils.findRequiredViewAsType(source, R.id.profile, "field 'profileView'", EditText.class);
    target.headView = Utils.findRequiredViewAsType(source, R.id.iconImg, "field 'headView'", ImageView.class);
    target.loginAccount = Utils.findRequiredViewAsType(source, R.id.pay_card, "field 'loginAccount'", TextView.class);
    target.inforType = Utils.findRequiredViewAsType(source, R.id.infor_type, "field 'inforType'", TextView.class);
    target.moblie = Utils.findRequiredViewAsType(source, R.id.phone, "field 'moblie'", EditText.class);
    target.card = Utils.findRequiredViewAsType(source, R.id.card, "field 'card'", EditText.class);
    view = Utils.findRequiredView(source, R.id.edit_photo, "method 'pickImage'");
    view2131296354 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pickImage(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.edit_infor_type, "method 'selectType'");
    view2131296349 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectType(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.exit_user, "method 'postEdit'");
    view2131296362 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.postEdit(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EditInformationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.back = null;
    target.nick_name = null;
    target.profileView = null;
    target.headView = null;
    target.loginAccount = null;
    target.inforType = null;
    target.moblie = null;
    target.card = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
    view2131296354.setOnClickListener(null);
    view2131296354 = null;
    view2131296349.setOnClickListener(null);
    view2131296349 = null;
    view2131296362.setOnClickListener(null);
    view2131296362 = null;
  }
}
