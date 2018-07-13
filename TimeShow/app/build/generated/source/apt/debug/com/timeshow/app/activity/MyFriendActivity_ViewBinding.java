// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyFriendActivity_ViewBinding implements Unbinder {
  private MyFriendActivity target;

  private View view2131296287;

  private View view2131296486;

  @UiThread
  public MyFriendActivity_ViewBinding(MyFriendActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyFriendActivity_ViewBinding(final MyFriendActivity target, View source) {
    this.target = target;

    View view;
    target.mListView = Utils.findRequiredViewAsType(source, R.id.listview, "field 'mListView'", ListView.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'mImageView' and method 'onBack'");
    target.mImageView = Utils.castView(view, R.id.back, "field 'mImageView'", ImageView.class);
    view2131296287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBack(p0);
      }
    });
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.right_text, "field 'rightTitle' and method 'add'");
    target.rightTitle = Utils.castView(view, R.id.right_text, "field 'rightTitle'", TextView.class);
    view2131296486 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.add(p0);
      }
    });
    target.input = Utils.findRequiredViewAsType(source, R.id.input, "field 'input'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFriendActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mListView = null;
    target.mImageView = null;
    target.mTitle = null;
    target.rightTitle = null;
    target.input = null;

    view2131296287.setOnClickListener(null);
    view2131296287 = null;
    view2131296486.setOnClickListener(null);
    view2131296486 = null;
  }
}
