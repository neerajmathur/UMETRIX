package android.support.v4.app;

import android.app.NotificationManager;

class NotificationManagerCompatApi24
{
  NotificationManagerCompatApi24() {}
  
  public static boolean areNotificationsEnabled(NotificationManager paramNotificationManager)
  {
    return paramNotificationManager.areNotificationsEnabled();
  }
  
  public static int getImportance(NotificationManager paramNotificationManager)
  {
    return paramNotificationManager.getImportance();
  }
}
