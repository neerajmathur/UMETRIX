package android.support.v4.os;

import android.content.Context;

public class UserManagerCompat
{
  private UserManagerCompat() {}
  
  @Deprecated
  public static boolean isUserRunningAndLocked(Context paramContext)
  {
    return !isUserUnlocked(paramContext);
  }
  
  @Deprecated
  public static boolean isUserRunningAndUnlocked(Context paramContext)
  {
    return isUserUnlocked(paramContext);
  }
  
  public static boolean isUserUnlocked(Context paramContext)
  {
    if (BuildCompat.isAtLeastN()) {
      return UserManagerCompatApi24.isUserUnlocked(paramContext);
    }
    return true;
  }
}
