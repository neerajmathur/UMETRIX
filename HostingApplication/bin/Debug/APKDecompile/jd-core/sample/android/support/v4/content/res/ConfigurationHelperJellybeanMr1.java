package android.support.v4.content.res;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;

class ConfigurationHelperJellybeanMr1
{
  ConfigurationHelperJellybeanMr1() {}
  
  static int getDensityDpi(@NonNull Resources paramResources)
  {
    return paramResources.getConfiguration().densityDpi;
  }
}
