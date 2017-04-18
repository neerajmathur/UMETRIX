package android.support.v4.os;

import android.content.Context;
import android.os.UserManager;

public class UserManagerCompatApi24
{
  public UserManagerCompatApi24() {}
  
  public static boolean isUserUnlocked(Context paramContext)
  {
    return ((UserManager)paramContext.getSystemService(UserManager.class)).isUserUnlocked();
  }
}
