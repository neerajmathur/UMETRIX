package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

public class ButtonBarLayout
  extends LinearLayout
{
  private static final int ALLOW_STACKING_MIN_HEIGHT_DP = 320;
  private boolean mAllowStacking;
  private int mLastWidthSize = -1;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (ConfigurationHelper.getScreenHeightDp(getResources()) >= 320) {}
    for (boolean bool = true;; bool = false)
    {
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
      this.mAllowStacking = paramContext.getBoolean(R.styleable.ButtonBarLayout_allowStacking, bool);
      paramContext.recycle();
      return;
    }
  }
  
  private boolean isStacked()
  {
    return getOrientation() == 1;
  }
  
  private void setStacked(boolean paramBoolean)
  {
    label17:
    View localView;
    if (paramBoolean)
    {
      i = 1;
      setOrientation(i);
      if (!paramBoolean) {
        break label78;
      }
      i = 5;
      setGravity(i);
      localView = findViewById(R.id.spacer);
      if (localView != null) {
        if (!paramBoolean) {
          break label84;
        }
      }
    }
    label78:
    label84:
    for (int i = 8;; i = 4)
    {
      localView.setVisibility(i);
      i = getChildCount() - 2;
      while (i >= 0)
      {
        bringChildToFront(getChildAt(i));
        i -= 1;
      }
      i = 0;
      break;
      i = 80;
      break label17;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int m = 0;
    int n = 0;
    int i1 = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking)
    {
      if ((i1 > this.mLastWidthSize) && (isStacked())) {
        setStacked(false);
      }
      this.mLastWidthSize = i1;
    }
    int i = 0;
    int j;
    int k;
    if ((!isStacked()) && (View.MeasureSpec.getMode(paramInt1) == 1073741824))
    {
      j = View.MeasureSpec.makeMeasureSpec(i1, Integer.MIN_VALUE);
      i = 1;
      super.onMeasure(j, paramInt2);
      k = i;
      if (this.mAllowStacking)
      {
        k = i;
        if (!isStacked())
        {
          if (Build.VERSION.SDK_INT < 11) {
            break label163;
          }
          j = n;
          if ((ViewCompat.getMeasuredWidthAndState(this) & 0xFF000000) == 16777216) {
            j = 1;
          }
        }
      }
    }
    for (;;)
    {
      k = i;
      if (j != 0)
      {
        setStacked(true);
        k = 1;
      }
      if (k != 0) {
        super.onMeasure(paramInt1, paramInt2);
      }
      return;
      j = paramInt1;
      break;
      label163:
      k = 0;
      j = 0;
      n = getChildCount();
      while (j < n)
      {
        k += getChildAt(j).getMeasuredWidth();
        j += 1;
      }
      j = m;
      if (getPaddingLeft() + k + getPaddingRight() > i1) {
        j = 1;
      }
    }
  }
  
  public void setAllowStacking(boolean paramBoolean)
  {
    if (this.mAllowStacking != paramBoolean)
    {
      this.mAllowStacking = paramBoolean;
      if ((!this.mAllowStacking) && (getOrientation() == 1)) {
        setStacked(false);
      }
      requestLayout();
    }
  }
}
