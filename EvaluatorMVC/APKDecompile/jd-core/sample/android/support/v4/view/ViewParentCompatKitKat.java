package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

class ViewParentCompatKitKat
{
  ViewParentCompatKitKat() {}
  
  public static void notifySubtreeAccessibilityStateChanged(ViewParent paramViewParent, View paramView1, View paramView2, int paramInt)
  {
    paramViewParent.notifySubtreeAccessibilityStateChanged(paramView1, paramView2, paramInt);
  }
}
