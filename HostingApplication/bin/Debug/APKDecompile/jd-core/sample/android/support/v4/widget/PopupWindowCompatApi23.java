package android.support.v4.widget;

import android.widget.PopupWindow;

class PopupWindowCompatApi23
{
  PopupWindowCompatApi23() {}
  
  static boolean getOverlapAnchor(PopupWindow paramPopupWindow)
  {
    return paramPopupWindow.getOverlapAnchor();
  }
  
  static int getWindowLayoutType(PopupWindow paramPopupWindow)
  {
    return paramPopupWindow.getWindowLayoutType();
  }
  
  static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean)
  {
    paramPopupWindow.setOverlapAnchor(paramBoolean);
  }
  
  static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt)
  {
    paramPopupWindow.setWindowLayoutType(paramInt);
  }
}
