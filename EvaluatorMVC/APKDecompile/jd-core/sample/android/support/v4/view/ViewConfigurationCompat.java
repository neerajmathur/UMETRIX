package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewConfiguration;

public final class ViewConfigurationCompat
{
  static final ViewConfigurationVersionImpl IMPL = new BaseViewConfigurationVersionImpl();
  
  static
  {
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new IcsViewConfigurationVersionImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new HoneycombViewConfigurationVersionImpl();
      return;
    }
  }
  
  private ViewConfigurationCompat() {}
  
  @Deprecated
  public static int getScaledPagingTouchSlop(ViewConfiguration paramViewConfiguration)
  {
    return paramViewConfiguration.getScaledPagingTouchSlop();
  }
  
  public static boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
  {
    return IMPL.hasPermanentMenuKey(paramViewConfiguration);
  }
  
  static class BaseViewConfigurationVersionImpl
    implements ViewConfigurationCompat.ViewConfigurationVersionImpl
  {
    BaseViewConfigurationVersionImpl() {}
    
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return true;
    }
  }
  
  static class HoneycombViewConfigurationVersionImpl
    extends ViewConfigurationCompat.BaseViewConfigurationVersionImpl
  {
    HoneycombViewConfigurationVersionImpl() {}
    
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return false;
    }
  }
  
  static class IcsViewConfigurationVersionImpl
    extends ViewConfigurationCompat.HoneycombViewConfigurationVersionImpl
  {
    IcsViewConfigurationVersionImpl() {}
    
    public boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration)
    {
      return ViewConfigurationCompatICS.hasPermanentMenuKey(paramViewConfiguration);
    }
  }
  
  static abstract interface ViewConfigurationVersionImpl
  {
    public abstract boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration);
  }
}
