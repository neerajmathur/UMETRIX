package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;

class AppOpsManagerCompat23
{
  AppOpsManagerCompat23() {}
  
  public static int noteOp(Context paramContext, String paramString1, int paramInt, String paramString2)
  {
    return ((AppOpsManager)paramContext.getSystemService(AppOpsManager.class)).noteOp(paramString1, paramInt, paramString2);
  }
  
  public static int noteProxyOp(Context paramContext, String paramString1, String paramString2)
  {
    return ((AppOpsManager)paramContext.getSystemService(AppOpsManager.class)).noteProxyOp(paramString1, paramString2);
  }
  
  public static String permissionToOp(String paramString)
  {
    return AppOpsManager.permissionToOp(paramString);
  }
}
