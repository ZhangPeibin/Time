// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ActiveFragment_ViewBinding implements Unbinder {
  private ActiveFragment target;

  @UiThread
  public ActiveFragment_ViewBinding(ActiveFragment target, View source) {
    this.target = target;

    target.mTabLayout = Utils.findRequiredViewAsType(source, R.id.tl_tab, "field 'mTabLayout'", TabLayout.class);
    target.mListView = Utils.findRequiredViewAsType(source, R.id.listview, "field 'mListView'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActiveFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTabLayout = null;
    target.mListView = null;
  }
}
