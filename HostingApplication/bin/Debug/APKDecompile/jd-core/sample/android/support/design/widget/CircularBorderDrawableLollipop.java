package android.support.design.widget;

import android.graphics.Outline;

class CircularBorderDrawableLollipop
  extends CircularBorderDrawable
{
  CircularBorderDrawableLollipop() {}
  
  public void getOutline(Outline paramOutline)
  {
    copyBounds(this.mRect);
    paramOutline.setOval(this.mRect);
  }
}
