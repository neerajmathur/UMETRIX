package android.support.design.widget;

import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.Log;
import java.lang.reflect.Method;

class DrawableUtils
{
  private static final String LOG_TAG = "DrawableUtils";
  private static Method sSetConstantStateMethod;
  private static boolean sSetConstantStateMethodFetched;
  
  private DrawableUtils() {}
  
  static boolean setContainerConstantState(DrawableContainer paramDrawableContainer, Drawable.ConstantState paramConstantState)
  {
    return setContainerConstantStateV9(paramDrawableContainer, paramConstantState);
  }
  
  private static boolean setContainerConstantStateV9(DrawableContainer paramDrawableContainer, Drawable.ConstantState paramConstantState)
  {
    if (!sSetConstantStateMethodFetched) {}
    try
    {
      sSetConstantStateMethod = DrawableContainer.class.getDeclaredMethod("setConstantState", new Class[] { DrawableContainer.DrawableContainerState.class });
      sSetConstantStateMethod.setAccessible(true);
      sSetConstantStateMethodFetched = true;
      if (sSetConstantStateMethod == null) {}
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        try
        {
          sSetConstantStateMethod.invoke(paramDrawableContainer, new Object[] { paramConstantState });
          return true;
        }
        catch (Exception paramDrawableContainer)
        {
          Log.e("DrawableUtils", "Could not invoke setConstantState(). Oh well.");
        }
        localNoSuchMethodException = localNoSuchMethodException;
        Log.e("DrawableUtils", "Could not fetch setConstantState(). Oh well.");
      }
    }
    return false;
  }
}
