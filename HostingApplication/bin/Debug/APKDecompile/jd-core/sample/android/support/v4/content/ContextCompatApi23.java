package android.support.v4.content;

import android.content.Context;
import android.content.res.ColorStateList;

class ContextCompatApi23
{
  ContextCompatApi23() {}
  
  public static int getColor(Context paramContext, int paramInt)
  {
    return paramContext.getColor(paramInt);
  }
  
  public static ColorStateList getColorStateList(Context paramContext, int paramInt)
  {
    return paramContext.getColorStateList(paramInt);
  }
}
