package android.support.v4.graphics;

import android.graphics.Bitmap;

class BitmapCompatJellybeanMR2
{
  BitmapCompatJellybeanMR2() {}
  
  public static boolean hasMipMap(Bitmap paramBitmap)
  {
    return paramBitmap.hasMipMap();
  }
  
  public static void setHasMipMap(Bitmap paramBitmap, boolean paramBoolean)
  {
    paramBitmap.setHasMipMap(paramBoolean);
  }
}
