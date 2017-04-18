package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatHoneycombMR2
{
  ConnectivityManagerCompatHoneycombMR2() {}
  
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager)
  {
    paramConnectivityManager = paramConnectivityManager.getActiveNetworkInfo();
    if (paramConnectivityManager == null) {
      return true;
    }
    switch (paramConnectivityManager.getType())
    {
    case 0: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 8: 
    default: 
      return true;
    }
    return false;
  }
}
