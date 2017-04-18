package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;

class AccessibilityNodeInfoCompatApi24
{
  AccessibilityNodeInfoCompatApi24() {}
  
  public static Object getActionSetProgress()
  {
    return AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
  }
  
  public static int getDrawingOrder(Object paramObject)
  {
    return ((AccessibilityNodeInfo)paramObject).getDrawingOrder();
  }
  
  public static boolean isImportantForAccessibility(Object paramObject)
  {
    return ((AccessibilityNodeInfo)paramObject).isImportantForAccessibility();
  }
  
  public static void setDrawingOrder(Object paramObject, int paramInt)
  {
    ((AccessibilityNodeInfo)paramObject).setDrawingOrder(paramInt);
  }
  
  public static void setImportantForAccessibility(Object paramObject, boolean paramBoolean)
  {
    ((AccessibilityNodeInfo)paramObject).setImportantForAccessibility(paramBoolean);
  }
}
