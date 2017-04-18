package android.support.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.util.AttributeSet;

@TargetApi(21)
abstract class VectorDrawableCommon
  extends Drawable
  implements TintAwareDrawable
{
  Drawable mDelegateDrawable;
  
  VectorDrawableCommon() {}
  
  static TypedArray obtainAttributes(Resources paramResources, Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt)
  {
    if (paramTheme == null) {
      return paramResources.obtainAttributes(paramAttributeSet, paramArrayOfInt);
    }
    return paramTheme.obtainStyledAttributes(paramAttributeSet, paramArrayOfInt, 0, 0);
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.applyTheme(this.mDelegateDrawable, paramTheme);
    }
  }
  
  public void clearColorFilter()
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.clearColorFilter();
      return;
    }
    super.clearColorFilter();
  }
  
  public ColorFilter getColorFilter()
  {
    if (this.mDelegateDrawable != null) {
      return DrawableCompat.getColorFilter(this.mDelegateDrawable);
    }
    return null;
  }
  
  public Drawable getCurrent()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getCurrent();
    }
    return super.getCurrent();
  }
  
  public int getLayoutDirection()
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.getLayoutDirection(this.mDelegateDrawable);
    }
    return 0;
  }
  
  public int getMinimumHeight()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getMinimumHeight();
    }
    return super.getMinimumHeight();
  }
  
  public int getMinimumWidth()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getMinimumWidth();
    }
    return super.getMinimumWidth();
  }
  
  public boolean getPadding(Rect paramRect)
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getPadding(paramRect);
    }
    return super.getPadding(paramRect);
  }
  
  public int[] getState()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getState();
    }
    return super.getState();
  }
  
  public Region getTransparentRegion()
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.getTransparentRegion();
    }
    return super.getTransparentRegion();
  }
  
  public boolean isAutoMirrored()
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
    }
    return false;
  }
  
  public void jumpToCurrentState()
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.jumpToCurrentState(this.mDelegateDrawable);
    }
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.setBounds(paramRect);
      return;
    }
    super.onBoundsChange(paramRect);
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.setLevel(paramInt);
    }
    return super.onLevelChange(paramInt);
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setAutoMirrored(this.mDelegateDrawable, paramBoolean);
    }
  }
  
  public void setChangingConfigurations(int paramInt)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.setChangingConfigurations(paramInt);
      return;
    }
    super.setChangingConfigurations(paramInt);
  }
  
  public void setColorFilter(int paramInt, PorterDuff.Mode paramMode)
  {
    if (this.mDelegateDrawable != null)
    {
      this.mDelegateDrawable.setColorFilter(paramInt, paramMode);
      return;
    }
    super.setColorFilter(paramInt, paramMode);
  }
  
  public void setFilterBitmap(boolean paramBoolean)
  {
    if (this.mDelegateDrawable != null) {
      this.mDelegateDrawable.setFilterBitmap(paramBoolean);
    }
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setHotspot(this.mDelegateDrawable, paramFloat1, paramFloat2);
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.mDelegateDrawable != null) {
      DrawableCompat.setHotspotBounds(this.mDelegateDrawable, paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public boolean setState(int[] paramArrayOfInt)
  {
    if (this.mDelegateDrawable != null) {
      return this.mDelegateDrawable.setState(paramArrayOfInt);
    }
    return super.setState(paramArrayOfInt);
  }
}
