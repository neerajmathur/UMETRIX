package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

class ConfigurationHelperGingerbread
{
  ConfigurationHelperGingerbread() {}
  
  static int getDensityDpi(@NonNull Resources paramResources)
  {
    return paramResources.getDisplayMetrics().densityDpi;
  }
  
  static int getScreenHeightDp(@NonNull Resources paramResources)
  {
    paramResources = paramResources.getDisplayMetrics();
    return (int)(paramResources.heightPixels / paramResources.density);
  }
  
  static int getScreenWidthDp(@NonNull Resources paramResources)
  {
    paramResources = paramResources.getDisplayMetrics();
    return (int)(paramResources.widthPixels / paramResources.density);
  }
  
  static int getSmallestScreenWidthDp(@NonNull Resources paramResources)
  {
    return Math.min(getScreenWidthDp(paramResources), getScreenHeightDp(paramResources));
  }
}
