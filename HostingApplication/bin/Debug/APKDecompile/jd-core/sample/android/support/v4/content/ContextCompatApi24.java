package android.support.v4.content;

import android.content.Context;
import java.io.File;

class ContextCompatApi24
{
  ContextCompatApi24() {}
  
  public static Context createDeviceProtectedStorageContext(Context paramContext)
  {
    return paramContext.createDeviceProtectedStorageContext();
  }
  
  public static File getDataDir(Context paramContext)
  {
    return paramContext.getDataDir();
  }
  
  public static boolean isDeviceProtectedStorage(Context paramContext)
  {
    return paramContext.isDeviceProtectedStorage();
  }
}
