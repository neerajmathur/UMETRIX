package android.support.v4.media;

import android.media.browse.MediaBrowser.MediaItem;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class ParceledListSliceAdapterApi21
{
  private static Constructor sConstructor;
  
  static
  {
    try
    {
      sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(new Class[] { List.class });
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;) {}
    }
  }
  
  ParceledListSliceAdapterApi21() {}
  
  static Object newInstance(List<MediaBrowser.MediaItem> paramList)
  {
    try
    {
      paramList = sConstructor.newInstance(new Object[] { paramList });
      return paramList;
    }
    catch (InstantiationException paramList)
    {
      paramList.printStackTrace();
      return null;
    }
    catch (IllegalAccessException paramList)
    {
      for (;;) {}
    }
    catch (InvocationTargetException paramList)
    {
      for (;;) {}
    }
  }
}
