package android.support.v4.media.session;

import android.media.session.MediaSession;

class MediaSessionCompatApi22
{
  MediaSessionCompatApi22() {}
  
  public static void setRatingType(Object paramObject, int paramInt)
  {
    ((MediaSession)paramObject).setRatingType(paramInt);
  }
}
