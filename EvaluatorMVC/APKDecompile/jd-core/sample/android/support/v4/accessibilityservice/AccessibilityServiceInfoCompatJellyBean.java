package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;

class AccessibilityServiceInfoCompatJellyBean
{
  AccessibilityServiceInfoCompatJellyBean() {}
  
  public static String loadDescription(AccessibilityServiceInfo paramAccessibilityServiceInfo, PackageManager paramPackageManager)
  {
    return paramAccessibilityServiceInfo.loadDescription(paramPackageManager);
  }
}
