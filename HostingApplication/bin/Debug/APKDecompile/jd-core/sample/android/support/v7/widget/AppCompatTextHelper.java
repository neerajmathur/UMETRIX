package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelper
{
  private TintInfo mDrawableBottomTint;
  private TintInfo mDrawableLeftTint;
  private TintInfo mDrawableRightTint;
  private TintInfo mDrawableTopTint;
  final TextView mView;
  
  AppCompatTextHelper(TextView paramTextView)
  {
    this.mView = paramTextView;
  }
  
  static AppCompatTextHelper create(TextView paramTextView)
  {
    if (Build.VERSION.SDK_INT >= 17) {
      return new AppCompatTextHelperV17(paramTextView);
    }
    return new AppCompatTextHelper(paramTextView);
  }
  
  protected static TintInfo createTintInfo(Context paramContext, AppCompatDrawableManager paramAppCompatDrawableManager, int paramInt)
  {
    paramContext = paramAppCompatDrawableManager.getTintList(paramContext, paramInt);
    if (paramContext != null)
    {
      paramAppCompatDrawableManager = new TintInfo();
      paramAppCompatDrawableManager.mHasTintList = true;
      paramAppCompatDrawableManager.mTintList = paramContext;
      return paramAppCompatDrawableManager;
    }
    return null;
  }
  
  final void applyCompoundDrawableTint(Drawable paramDrawable, TintInfo paramTintInfo)
  {
    if ((paramDrawable != null) && (paramTintInfo != null)) {
      AppCompatDrawableManager.tintDrawable(paramDrawable, paramTintInfo, this.mView.getDrawableState());
    }
  }
  
  void applyCompoundDrawablesTints()
  {
    if ((this.mDrawableLeftTint != null) || (this.mDrawableTopTint != null) || (this.mDrawableRightTint != null) || (this.mDrawableBottomTint != null))
    {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableLeftTint);
      applyCompoundDrawableTint(arrayOfDrawable[1], this.mDrawableTopTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableRightTint);
      applyCompoundDrawableTint(arrayOfDrawable[3], this.mDrawableBottomTint);
    }
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt)
  {
    Context localContext = this.mView.getContext();
    Object localObject = AppCompatDrawableManager.get();
    TintTypedArray localTintTypedArray1 = TintTypedArray.obtainStyledAttributes(localContext, paramAttributeSet, R.styleable.AppCompatTextHelper, paramInt, 0);
    int k = localTintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
    if (localTintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
      this.mDrawableLeftTint = createTintInfo(localContext, (AppCompatDrawableManager)localObject, localTintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
    }
    if (localTintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
      this.mDrawableTopTint = createTintInfo(localContext, (AppCompatDrawableManager)localObject, localTintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
    }
    if (localTintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
      this.mDrawableRightTint = createTintInfo(localContext, (AppCompatDrawableManager)localObject, localTintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
    }
    if (localTintTypedArray1.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
      this.mDrawableBottomTint = createTintInfo(localContext, (AppCompatDrawableManager)localObject, localTintTypedArray1.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
    }
    localTintTypedArray1.recycle();
    boolean bool3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
    boolean bool1 = false;
    boolean bool2 = false;
    int i = 0;
    int j = 0;
    localObject = null;
    localTintTypedArray1 = null;
    if (k != -1)
    {
      TintTypedArray localTintTypedArray2 = TintTypedArray.obtainStyledAttributes(localContext, k, R.styleable.TextAppearance);
      bool1 = bool2;
      i = j;
      if (!bool3)
      {
        bool1 = bool2;
        i = j;
        if (localTintTypedArray2.hasValue(R.styleable.TextAppearance_textAllCaps))
        {
          i = 1;
          bool1 = localTintTypedArray2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
        }
      }
      localObject = localTintTypedArray1;
      if (Build.VERSION.SDK_INT < 23)
      {
        localObject = localTintTypedArray1;
        if (localTintTypedArray2.hasValue(R.styleable.TextAppearance_android_textColor)) {
          localObject = localTintTypedArray2.getColorStateList(R.styleable.TextAppearance_android_textColor);
        }
      }
      localTintTypedArray2.recycle();
    }
    localTintTypedArray1 = TintTypedArray.obtainStyledAttributes(localContext, paramAttributeSet, R.styleable.TextAppearance, paramInt, 0);
    bool2 = bool1;
    paramInt = i;
    if (!bool3)
    {
      bool2 = bool1;
      paramInt = i;
      if (localTintTypedArray1.hasValue(R.styleable.TextAppearance_textAllCaps))
      {
        paramInt = 1;
        bool2 = localTintTypedArray1.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
      }
    }
    paramAttributeSet = (AttributeSet)localObject;
    if (Build.VERSION.SDK_INT < 23)
    {
      paramAttributeSet = (AttributeSet)localObject;
      if (localTintTypedArray1.hasValue(R.styleable.TextAppearance_android_textColor)) {
        paramAttributeSet = localTintTypedArray1.getColorStateList(R.styleable.TextAppearance_android_textColor);
      }
    }
    localTintTypedArray1.recycle();
    if (paramAttributeSet != null) {
      this.mView.setTextColor(paramAttributeSet);
    }
    if ((!bool3) && (paramInt != 0)) {
      setAllCaps(bool2);
    }
  }
  
  void onSetTextAppearance(Context paramContext, int paramInt)
  {
    paramContext = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    if (paramContext.hasValue(R.styleable.TextAppearance_textAllCaps)) {
      setAllCaps(paramContext.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
    }
    if ((Build.VERSION.SDK_INT < 23) && (paramContext.hasValue(R.styleable.TextAppearance_android_textColor)))
    {
      ColorStateList localColorStateList = paramContext.getColorStateList(R.styleable.TextAppearance_android_textColor);
      if (localColorStateList != null) {
        this.mView.setTextColor(localColorStateList);
      }
    }
    paramContext.recycle();
  }
  
  void setAllCaps(boolean paramBoolean)
  {
    TextView localTextView = this.mView;
    if (paramBoolean) {}
    for (AllCapsTransformationMethod localAllCapsTransformationMethod = new AllCapsTransformationMethod(this.mView.getContext());; localAllCapsTransformationMethod = null)
    {
      localTextView.setTransformationMethod(localAllCapsTransformationMethod);
      return;
    }
  }
}
