package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityEvent;

class AccessibilityEventCompatJellyBean
{
  AccessibilityEventCompatJellyBean() {}
  
  public static int getAction(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getAction();
  }
  
  public static int getMovementGranularity(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getMovementGranularity();
  }
  
  public static void setAction(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    paramAccessibilityEvent.setAction(paramInt);
  }
  
  public static void setMovementGranularity(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    paramAccessibilityEvent.setMovementGranularity(paramInt);
  }
}
