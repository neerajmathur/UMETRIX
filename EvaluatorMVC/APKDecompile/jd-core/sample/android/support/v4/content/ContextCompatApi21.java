package android.support.v4.content;

import android.content.Context;
import android.graphics.drawable.Drawable;
import java.io.File;

class ContextCompatApi21
{
  ContextCompatApi21() {}
  
  public static File getCodeCacheDir(Context paramContext)
  {
    return paramContext.getCodeCacheDir();
  }
  
  public static Drawable getDrawable(Context paramContext, int paramInt)
  {
    return paramContext.getDrawable(paramInt);
  }
  
  public static File getNoBackupFilesDir(Context paramContext)
  {
    return paramContext.getNoBackupFilesDir();
  }
}
