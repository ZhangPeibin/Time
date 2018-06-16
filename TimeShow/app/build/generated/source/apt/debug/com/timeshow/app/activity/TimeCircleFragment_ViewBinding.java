// Generated code from Butter Knife. Do not modify!
package com.timeshow.app.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.timeshow.app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TimeCircleFragment_ViewBinding implements Unbinder {
  private TimeCircleFragment target;

  @UiThread
  public TimeCircleFragment_ViewBinding(TimeCircleFragment target, View source) {
    this.target = target;

    target.mListView = Utils.findRequiredViewAsType(source, R.id.listview, "field 'mListView'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TimeCircleFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mListView = null;
  }
}
