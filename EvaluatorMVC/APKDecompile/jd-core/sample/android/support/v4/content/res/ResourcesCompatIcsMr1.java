package android.support.v4.content.res;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;

class ResourcesCompatIcsMr1
{
  ResourcesCompatIcsMr1() {}
  
  public static Drawable getDrawableForDensity(Resources paramResources, int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    return paramResources.getDrawableForDensity(paramInt1, paramInt2);
  }
}
