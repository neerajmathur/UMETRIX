package android.support.v4.view;

import android.view.MotionEvent;

class MotionEventCompatHoneycombMr1
{
  MotionEventCompatHoneycombMr1() {}
  
  static float getAxisValue(MotionEvent paramMotionEvent, int paramInt)
  {
    return paramMotionEvent.getAxisValue(paramInt);
  }
  
  static float getAxisValue(MotionEvent paramMotionEvent, int paramInt1, int paramInt2)
  {
    return paramMotionEvent.getAxisValue(paramInt1, paramInt2);
  }
}
