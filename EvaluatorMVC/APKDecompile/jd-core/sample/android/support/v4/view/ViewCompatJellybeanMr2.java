package android.support.v4.view;

import android.graphics.Rect;
import android.view.View;

class ViewCompatJellybeanMr2
{
  ViewCompatJellybeanMr2() {}
  
  public static Rect getClipBounds(View paramView)
  {
    return paramView.getClipBounds();
  }
  
  public static boolean isInLayout(View paramView)
  {
    return paramView.isInLayout();
  }
  
  public static void setClipBounds(View paramView, Rect paramRect)
  {
    paramView.setClipBounds(paramRect);
  }
}
