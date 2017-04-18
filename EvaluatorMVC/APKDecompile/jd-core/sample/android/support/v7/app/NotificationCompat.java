package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat.BuilderExtender;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.media.session.MediaSessionCompat.Token;

public class NotificationCompat
  extends android.support.v4.app.NotificationCompat
{
  public NotificationCompat() {}
  
  private static void addBigMediaStyleToBuilderJellybean(Notification paramNotification, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof MediaStyle))
    {
      MediaStyle localMediaStyle = (MediaStyle)paramBuilder.mStyle;
      NotificationCompatImplBase.overrideBigContentView(paramNotification, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.mNotification.when, paramBuilder.mActions, localMediaStyle.mShowCancelButton, localMediaStyle.mCancelButtonIntent);
      paramNotification = getExtras(paramNotification);
      if (localMediaStyle.mToken != null) {
        BundleCompat.putBinder(paramNotification, "android.mediaSession", (IBinder)localMediaStyle.mToken.getToken());
      }
      if (localMediaStyle.mActionsToShowInCompact != null) {
        paramNotification.putIntArray("android.compactActions", localMediaStyle.mActionsToShowInCompact);
      }
    }
  }
  
  private static void addMediaStyleToBuilderIcs(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, android.support.v4.app.NotificationCompat.Builder paramBuilder)
  {
    if ((paramBuilder.mStyle instanceof MediaStyle))
    {
      MediaStyle localMediaStyle = (MediaStyle)paramBuilder.mStyle;
      NotificationCompatImplBase.overrideContentView(paramNotificationBuilderWithBuilderAccessor, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.mNotification.when, paramBuilder.mActions, localMediaStyle.mActionsToShowInCompact, localMediaStyle.mShowCancelButton, localMediaStyle.mCancelButtonIntent);
    }
  }
  
  private static void addMediaStyleToBuilderLollipop(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, NotificationCompat.Style paramStyle)
  {
    int[] arrayOfInt;
    if ((paramStyle instanceof MediaStyle))
    {
      paramStyle = (MediaStyle)paramStyle;
      arrayOfInt = paramStyle.mActionsToShowInCompact;
      if (paramStyle.mToken == null) {
        break label39;
      }
    }
    label39:
    for (paramStyle = paramStyle.mToken.getToken();; paramStyle = null)
    {
      NotificationCompatImpl21.addMediaStyle(paramNotificationBuilderWithBuilderAccessor, arrayOfInt, paramStyle);
      return;
    }
  }
  
  public static MediaSessionCompat.Token getMediaSession(Notification paramNotification)
  {
    paramNotification = getExtras(paramNotification);
    if (paramNotification != null) {
      if (Build.VERSION.SDK_INT >= 21)
      {
        paramNotification = paramNotification.getParcelable("android.mediaSession");
        if (paramNotification != null) {
          return MediaSessionCompat.Token.fromToken(paramNotification);
        }
      }
      else
      {
        Object localObject = BundleCompat.getBinder(paramNotification, "android.mediaSession");
        if (localObject != null)
        {
          paramNotification = Parcel.obtain();
          paramNotification.writeStrongBinder((IBinder)localObject);
          paramNotification.setDataPosition(0);
          localObject = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(paramNotification);
          paramNotification.recycle();
          return localObject;
        }
      }
    }
    return null;
  }
  
  public static class Builder
    extends android.support.v4.app.NotificationCompat.Builder
  {
    public Builder(Context paramContext)
    {
      super();
    }
    
    protected NotificationCompat.BuilderExtender getExtender()
    {
      if (Build.VERSION.SDK_INT >= 21) {
        return new NotificationCompat.LollipopExtender(null);
      }
      if (Build.VERSION.SDK_INT >= 16) {
        return new NotificationCompat.JellybeanExtender(null);
      }
      if (Build.VERSION.SDK_INT >= 14) {
        return new NotificationCompat.IceCreamSandwichExtender(null);
      }
      return super.getExtender();
    }
  }
  
  private static class IceCreamSandwichExtender
    extends NotificationCompat.BuilderExtender
  {
    private IceCreamSandwichExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      NotificationCompat.addMediaStyleToBuilderIcs(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      return paramNotificationBuilderWithBuilderAccessor.build();
    }
  }
  
  private static class JellybeanExtender
    extends NotificationCompat.BuilderExtender
  {
    private JellybeanExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      NotificationCompat.addMediaStyleToBuilderIcs(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      paramNotificationBuilderWithBuilderAccessor = paramNotificationBuilderWithBuilderAccessor.build();
      NotificationCompat.addBigMediaStyleToBuilderJellybean(paramNotificationBuilderWithBuilderAccessor, paramBuilder);
      return paramNotificationBuilderWithBuilderAccessor;
    }
  }
  
  private static class LollipopExtender
    extends NotificationCompat.BuilderExtender
  {
    private LollipopExtender() {}
    
    public Notification build(android.support.v4.app.NotificationCompat.Builder paramBuilder, NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor)
    {
      NotificationCompat.addMediaStyleToBuilderLollipop(paramNotificationBuilderWithBuilderAccessor, paramBuilder.mStyle);
      return paramNotificationBuilderWithBuilderAccessor.build();
    }
  }
  
  public static class MediaStyle
    extends NotificationCompat.Style
  {
    int[] mActionsToShowInCompact = null;
    PendingIntent mCancelButtonIntent;
    boolean mShowCancelButton;
    MediaSessionCompat.Token mToken;
    
    public MediaStyle() {}
    
    public MediaStyle(android.support.v4.app.NotificationCompat.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }
    
    public MediaStyle setCancelButtonIntent(PendingIntent paramPendingIntent)
    {
      this.mCancelButtonIntent = paramPendingIntent;
      return this;
    }
    
    public MediaStyle setMediaSession(MediaSessionCompat.Token paramToken)
    {
      this.mToken = paramToken;
      return this;
    }
    
    public MediaStyle setShowActionsInCompactView(int... paramVarArgs)
    {
      this.mActionsToShowInCompact = paramVarArgs;
      return this;
    }
    
    public MediaStyle setShowCancelButton(boolean paramBoolean)
    {
      this.mShowCancelButton = paramBoolean;
      return this;
    }
  }
}
