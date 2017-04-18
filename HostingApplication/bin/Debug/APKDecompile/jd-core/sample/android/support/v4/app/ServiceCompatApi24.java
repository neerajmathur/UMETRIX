package android.support.v4.app;

import android.app.Service;

class ServiceCompatApi24
{
  ServiceCompatApi24() {}
  
  public static void stopForeground(Service paramService, int paramInt)
  {
    paramService.stopForeground(paramInt);
  }
}
