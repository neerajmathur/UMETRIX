package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

class DrawableCompatApi23
{
  DrawableCompatApi23() {}
  
  public static int getLayoutDirection(Drawable paramDrawable)
  {
    return paramDrawable.getLayoutDirection();
  }
  
  public static boolean setLayoutDirection(Drawable paramDrawable, int paramInt)
  {
    return paramDrawable.setLayoutDirection(paramInt);
  }
}
