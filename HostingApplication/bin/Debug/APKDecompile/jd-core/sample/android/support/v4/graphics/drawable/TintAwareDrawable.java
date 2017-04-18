package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.support.annotation.ColorInt;

public abstract interface TintAwareDrawable
{
  public abstract void setTint(@ColorInt int paramInt);
  
  public abstract void setTintList(ColorStateList paramColorStateList);
  
  public abstract void setTintMode(PorterDuff.Mode paramMode);
}
