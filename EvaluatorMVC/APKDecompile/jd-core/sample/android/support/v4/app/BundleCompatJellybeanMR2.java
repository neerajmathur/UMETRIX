package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;

class BundleCompatJellybeanMR2
{
  BundleCompatJellybeanMR2() {}
  
  public static IBinder getBinder(Bundle paramBundle, String paramString)
  {
    return paramBundle.getBinder(paramString);
  }
  
  public static void putBinder(Bundle paramBundle, String paramString, IBinder paramIBinder)
  {
    paramBundle.putBinder(paramString, paramIBinder);
  }
}
