package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityWindowInfo;

class AccessibilityWindowInfoCompatApi24
{
  AccessibilityWindowInfoCompatApi24() {}
  
  public static Object getAnchor(Object paramObject)
  {
    return ((AccessibilityWindowInfo)paramObject).getAnchor();
  }
  
  public static CharSequence getTitle(Object paramObject)
  {
    return ((AccessibilityWindowInfo)paramObject).getTitle();
  }
}
