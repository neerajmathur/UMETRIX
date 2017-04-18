package android.support.v4.view;

import android.view.View;

class ViewCompatKitKat
{
  ViewCompatKitKat() {}
  
  public static int getAccessibilityLiveRegion(View paramView)
  {
    return paramView.getAccessibilityLiveRegion();
  }
  
  public static boolean isAttachedToWindow(View paramView)
  {
    return paramView.isAttachedToWindow();
  }
  
  public static boolean isLaidOut(View paramView)
  {
    return paramView.isLaidOut();
  }
  
  public static boolean isLayoutDirectionResolved(View paramView)
  {
    return paramView.isLayoutDirectionResolved();
  }
  
  public static void setAccessibilityLiveRegion(View paramView, int paramInt)
  {
    paramView.setAccessibilityLiveRegion(paramInt);
  }
}
