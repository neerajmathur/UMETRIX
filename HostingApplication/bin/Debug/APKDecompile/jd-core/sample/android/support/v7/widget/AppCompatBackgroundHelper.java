package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper
{
  private int mBackgroundResId = -1;
  private BackgroundTintInfo mBackgroundTint;
  private final AppCompatDrawableManager mDrawableManager;
  private BackgroundTintInfo mInternalBackgroundTint;
  private BackgroundTintInfo mTmpInfo;
  private final View mView;
  
  AppCompatBackgroundHelper(View paramView)
  {
    this.mView = paramView;
    this.mDrawableManager = AppCompatDrawableManager.get();
  }
  
  private boolean applyFrameworkTintUsingColorFilter(@NonNull Drawable paramDrawable)
  {
    if (this.mTmpInfo == null) {
      this.mTmpInfo = new BackgroundTintInfo(null);
    }
    BackgroundTintInfo localBackgroundTintInfo = this.mTmpInfo;
    localBackgroundTintInfo.clear();
    Object localObject = ViewCompat.getBackgroundTintList(this.mView);
    if (localObject != null)
    {
      localBackgroundTintInfo.mHasTintList = true;
      localBackgroundTintInfo.mTintList = ((ColorStateList)localObject);
    }
    localObject = ViewCompat.getBackgroundTintMode(this.mView);
    if (localObject != null)
    {
      localBackgroundTintInfo.mHasTintMode = true;
      localBackgroundTintInfo.mTintMode = ((PorterDuff.Mode)localObject);
    }
    if ((localBackgroundTintInfo.mHasTintList) || (localBackgroundTintInfo.mHasTintMode))
    {
      AppCompatDrawableManager.tintDrawable(paramDrawable, localBackgroundTintInfo, this.mView.getDrawableState());
      return true;
    }
    return false;
  }
  
  private boolean updateBackgroundTint()
  {
    if ((this.mBackgroundTint != null) && (this.mBackgroundTint.mHasTintList))
    {
      if (this.mBackgroundResId >= 0)
      {
        ColorStateList localColorStateList = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId, this.mBackgroundTint.mOriginalTintList);
        if (localColorStateList != null)
        {
          this.mBackgroundTint.mTintList = localColorStateList;
          return true;
        }
      }
      if (this.mBackgroundTint.mTintList != this.mBackgroundTint.mOriginalTintList)
      {
        this.mBackgroundTint.mTintList = this.mBackgroundTint.mOriginalTintList;
        return true;
      }
    }
    return false;
  }
  
  void applySupportBackgroundTint()
  {
    Drawable localDrawable = this.mView.getBackground();
    if ((localDrawable == null) || ((Build.VERSION.SDK_INT == 21) && (applyFrameworkTintUsingColorFilter(localDrawable)))) {}
    do
    {
      return;
      if (this.mBackgroundTint != null)
      {
        AppCompatDrawableManager.tintDrawable(localDrawable, this.mBackgroundTint, this.mView.getDrawableState());
        return;
      }
    } while (this.mInternalBackgroundTint == null);
    AppCompatDrawableManager.tintDrawable(localDrawable, this.mInternalBackgroundTint, this.mView.getDrawableState());
  }
  
  ColorStateList getSupportBackgroundTintList()
  {
    if (this.mBackgroundTint != null) {
      return this.mBackgroundTint.mTintList;
    }
    return null;
  }
  
  PorterDuff.Mode getSupportBackgroundTintMode()
  {
    if (this.mBackgroundTint != null) {
      return this.mBackgroundTint.mTintMode;
    }
    return null;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    paramAttributeSet = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, R.styleable.ViewBackgroundHelper, paramInt, 0);
    try
    {
      if (paramAttributeSet.hasValue(R.styleable.ViewBackgroundHelper_android_background))
      {
        this.mBackgroundResId = paramAttributeSet.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1);
        ColorStateList localColorStateList = this.mDrawableManager.getTintList(this.mView.getContext(), this.mBackgroundResId);
        if (localColorStateList != null) {
          setInternalBackgroundTint(localColorStateList);
        }
      }
      if (paramAttributeSet.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint)) {
        ViewCompat.setBackgroundTintList(this.mView, paramAttributeSet.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint));
      }
      if (paramAttributeSet.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode)) {
        ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(paramAttributeSet.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null));
      }
      return;
    }
    finally
    {
      paramAttributeSet.recycle();
    }
  }
  
  void onSetBackgroundDrawable(Drawable paramDrawable)
  {
    this.mBackgroundResId = -1;
    setInternalBackgroundTint(null);
    if (updateBackgroundTint()) {
      applySupportBackgroundTint();
    }
  }
  
  void onSetBackgroundResource(int paramInt)
  {
    this.mBackgroundResId = paramInt;
    if (this.mDrawableManager != null) {}
    for (ColorStateList localColorStateList = this.mDrawableManager.getTintList(this.mView.getContext(), paramInt);; localColorStateList = null)
    {
      setInternalBackgroundTint(localColorStateList);
      if (updateBackgroundTint()) {
        applySupportBackgroundTint();
      }
      return;
    }
  }
  
  void setInternalBackgroundTint(ColorStateList paramColorStateList)
  {
    if (paramColorStateList != null)
    {
      if (this.mInternalBackgroundTint == null) {
        this.mInternalBackgroundTint = new BackgroundTintInfo(null);
      }
      this.mInternalBackgroundTint.mTintList = paramColorStateList;
      this.mInternalBackgroundTint.mHasTintList = true;
    }
    for (;;)
    {
      applySupportBackgroundTint();
      return;
      this.mInternalBackgroundTint = null;
    }
  }
  
  void setSupportBackgroundTintList(ColorStateList paramColorStateList)
  {
    if (this.mBackgroundTint == null) {
      this.mBackgroundTint = new BackgroundTintInfo(null);
    }
    this.mBackgroundTint.mOriginalTintList = paramColorStateList;
    this.mBackgroundTint.mTintList = null;
    this.mBackgroundTint.mHasTintList = true;
    if (updateBackgroundTint()) {
      applySupportBackgroundTint();
    }
  }
  
  void setSupportBackgroundTintMode(PorterDuff.Mode paramMode)
  {
    if (this.mBackgroundTint == null) {
      this.mBackgroundTint = new BackgroundTintInfo(null);
    }
    this.mBackgroundTint.mTintMode = paramMode;
    this.mBackgroundTint.mHasTintMode = true;
    applySupportBackgroundTint();
  }
  
  private static class BackgroundTintInfo
    extends TintInfo
  {
    public ColorStateList mOriginalTintList;
    
    private BackgroundTintInfo() {}
    
    void clear()
    {
      super.clear();
      this.mOriginalTintList = null;
    }
  }
}
