package android.support.design.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.R.attr;
import android.support.design.R.style;
import android.support.design.R.styleable;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@CoordinatorLayout.DefaultBehavior(Behavior.class)
public class AppBarLayout
  extends LinearLayout
{
  private static final int INVALID_SCROLL_RANGE = -1;
  private static final int PENDING_ACTION_ANIMATE_ENABLED = 4;
  private static final int PENDING_ACTION_COLLAPSED = 2;
  private static final int PENDING_ACTION_EXPANDED = 1;
  private static final int PENDING_ACTION_NONE = 0;
  private boolean mCollapsed;
  private boolean mCollapsible;
  private int mDownPreScrollRange = -1;
  private int mDownScrollRange = -1;
  private boolean mHaveChildWithInterpolator;
  private WindowInsetsCompat mLastInsets;
  private List<OnOffsetChangedListener> mListeners;
  private int mPendingAction = 0;
  private final int[] mTmpStatesArray = new int[2];
  private int mTotalScrollRange = -1;
  
  public AppBarLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AppBarLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    ThemeUtils.checkAppCompatTheme(paramContext);
    if (Build.VERSION.SDK_INT >= 21)
    {
      ViewUtilsLollipop.setBoundsViewOutlineProvider(this);
      ViewUtilsLollipop.setStateListAnimatorFromAttrs(this, paramAttributeSet, 0, R.style.Widget_Design_AppBarLayout);
    }
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AppBarLayout, 0, R.style.Widget_Design_AppBarLayout);
    setBackgroundDrawable(paramContext.getDrawable(R.styleable.AppBarLayout_android_background));
    if (paramContext.hasValue(R.styleable.AppBarLayout_expanded)) {
      setExpanded(paramContext.getBoolean(R.styleable.AppBarLayout_expanded, false));
    }
    if ((Build.VERSION.SDK_INT >= 21) && (paramContext.hasValue(R.styleable.AppBarLayout_elevation))) {
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, paramContext.getDimensionPixelSize(R.styleable.AppBarLayout_elevation, 0));
    }
    paramContext.recycle();
    ViewCompat.setOnApplyWindowInsetsListener(this, new OnApplyWindowInsetsListener()
    {
      public WindowInsetsCompat onApplyWindowInsets(View paramAnonymousView, WindowInsetsCompat paramAnonymousWindowInsetsCompat)
      {
        return AppBarLayout.this.onWindowInsetChanged(paramAnonymousWindowInsetsCompat);
      }
    });
  }
  
  private void dispatchOffsetUpdates(int paramInt)
  {
    if (this.mListeners != null)
    {
      int i = 0;
      int j = this.mListeners.size();
      while (i < j)
      {
        OnOffsetChangedListener localOnOffsetChangedListener = (OnOffsetChangedListener)this.mListeners.get(i);
        if (localOnOffsetChangedListener != null) {
          localOnOffsetChangedListener.onOffsetChanged(this, paramInt);
        }
        i += 1;
      }
    }
  }
  
  private int getDownNestedPreScrollRange()
  {
    if (this.mDownPreScrollRange != -1) {
      return this.mDownPreScrollRange;
    }
    int k = 0;
    int j = getChildCount() - 1;
    if (j >= 0)
    {
      View localView = getChildAt(j);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      i = localView.getMeasuredHeight();
      int m = localLayoutParams.mScrollFlags;
      if ((m & 0x5) == 5)
      {
        k += localLayoutParams.topMargin + localLayoutParams.bottomMargin;
        if ((m & 0x8) != 0) {
          i = k + ViewCompat.getMinimumHeight(localView);
        }
      }
      do
      {
        for (;;)
        {
          j -= 1;
          k = i;
          break;
          if ((m & 0x2) != 0) {
            i = k + (i - ViewCompat.getMinimumHeight(localView));
          } else {
            i = k + i;
          }
        }
        i = k;
      } while (k <= 0);
    }
    int i = Math.max(0, k);
    this.mDownPreScrollRange = i;
    return i;
  }
  
  private int getDownNestedScrollRange()
  {
    if (this.mDownScrollRange != -1) {
      return this.mDownScrollRange;
    }
    int i = 0;
    int j = 0;
    int m = getChildCount();
    for (;;)
    {
      int k = i;
      if (j < m)
      {
        View localView = getChildAt(j);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i1 = localView.getMeasuredHeight();
        int i2 = localLayoutParams.topMargin;
        int i3 = localLayoutParams.bottomMargin;
        int n = localLayoutParams.mScrollFlags;
        k = i;
        if ((n & 0x1) != 0)
        {
          i += i1 + (i2 + i3);
          if ((n & 0x2) == 0) {
            break label129;
          }
          k = i - (ViewCompat.getMinimumHeight(localView) + getTopInset());
        }
      }
      i = Math.max(0, k);
      this.mDownScrollRange = i;
      return i;
      label129:
      j += 1;
    }
  }
  
  private int getPendingAction()
  {
    return this.mPendingAction;
  }
  
  private int getUpNestedPreScrollRange()
  {
    return getTotalScrollRange();
  }
  
  private boolean hasChildWithInterpolator()
  {
    return this.mHaveChildWithInterpolator;
  }
  
  private boolean hasScrollableChildren()
  {
    return getTotalScrollRange() != 0;
  }
  
  private void invalidateScrollRanges()
  {
    this.mTotalScrollRange = -1;
    this.mDownPreScrollRange = -1;
    this.mDownScrollRange = -1;
  }
  
  private WindowInsetsCompat onWindowInsetChanged(WindowInsetsCompat paramWindowInsetsCompat)
  {
    WindowInsetsCompat localWindowInsetsCompat = null;
    if (ViewCompat.getFitsSystemWindows(this)) {
      localWindowInsetsCompat = paramWindowInsetsCompat;
    }
    if (!ViewUtils.objectEquals(this.mLastInsets, localWindowInsetsCompat))
    {
      this.mLastInsets = localWindowInsetsCompat;
      invalidateScrollRanges();
    }
    return paramWindowInsetsCompat;
  }
  
  private void resetPendingAction()
  {
    this.mPendingAction = 0;
  }
  
  private boolean setCollapsedState(boolean paramBoolean)
  {
    if (this.mCollapsed != paramBoolean)
    {
      this.mCollapsed = paramBoolean;
      refreshDrawableState();
      return true;
    }
    return false;
  }
  
  private boolean setCollapsibleState(boolean paramBoolean)
  {
    if (this.mCollapsible != paramBoolean)
    {
      this.mCollapsible = paramBoolean;
      refreshDrawableState();
      return true;
    }
    return false;
  }
  
  private void updateCollapsible()
  {
    boolean bool2 = false;
    int i = 0;
    int j = getChildCount();
    for (;;)
    {
      boolean bool1 = bool2;
      if (i < j)
      {
        if (((LayoutParams)getChildAt(i).getLayoutParams()).isCollapsible()) {
          bool1 = true;
        }
      }
      else
      {
        setCollapsibleState(bool1);
        return;
      }
      i += 1;
    }
  }
  
  public void addOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener)
  {
    if (this.mListeners == null) {
      this.mListeners = new ArrayList();
    }
    if ((paramOnOffsetChangedListener != null) && (!this.mListeners.contains(paramOnOffsetChangedListener))) {
      this.mListeners.add(paramOnOffsetChangedListener);
    }
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LinearLayout.LayoutParams)) {
      return new LayoutParams((LinearLayout.LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  final int getMinimumHeightForVisibleOverlappingContent()
  {
    int j = getTopInset();
    int i = ViewCompat.getMinimumHeight(this);
    if (i != 0) {
      return i * 2 + j;
    }
    i = getChildCount();
    if (i >= 1) {}
    for (i = ViewCompat.getMinimumHeight(getChildAt(i - 1)); i != 0; i = 0) {
      return i * 2 + j;
    }
    return getHeight() / 3;
  }
  
  @Deprecated
  public float getTargetElevation()
  {
    return 0.0F;
  }
  
  @VisibleForTesting
  final int getTopInset()
  {
    if (this.mLastInsets != null) {
      return this.mLastInsets.getSystemWindowInsetTop();
    }
    return 0;
  }
  
  public final int getTotalScrollRange()
  {
    if (this.mTotalScrollRange != -1) {
      return this.mTotalScrollRange;
    }
    int i = 0;
    int j = 0;
    int m = getChildCount();
    for (;;)
    {
      int k = i;
      if (j < m)
      {
        View localView = getChildAt(j);
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i1 = localView.getMeasuredHeight();
        int n = localLayoutParams.mScrollFlags;
        k = i;
        if ((n & 0x1) != 0)
        {
          i += localLayoutParams.topMargin + i1 + localLayoutParams.bottomMargin;
          if ((n & 0x2) == 0) {
            break label121;
          }
          k = i - ViewCompat.getMinimumHeight(localView);
        }
      }
      i = Math.max(0, k - getTopInset());
      this.mTotalScrollRange = i;
      return i;
      label121:
      j += 1;
    }
  }
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt1 = this.mTmpStatesArray;
    int[] arrayOfInt2 = super.onCreateDrawableState(arrayOfInt1.length + paramInt);
    if (this.mCollapsible)
    {
      paramInt = R.attr.state_collapsible;
      arrayOfInt1[0] = paramInt;
      if ((!this.mCollapsible) || (!this.mCollapsed)) {
        break label65;
      }
    }
    label65:
    for (paramInt = R.attr.state_collapsed;; paramInt = -R.attr.state_collapsed)
    {
      arrayOfInt1[1] = paramInt;
      return mergeDrawableStates(arrayOfInt2, arrayOfInt1);
      paramInt = -R.attr.state_collapsible;
      break;
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    invalidateScrollRanges();
    this.mHaveChildWithInterpolator = false;
    paramInt1 = 0;
    paramInt2 = getChildCount();
    for (;;)
    {
      if (paramInt1 < paramInt2)
      {
        if (((LayoutParams)getChildAt(paramInt1).getLayoutParams()).getScrollInterpolator() != null) {
          this.mHaveChildWithInterpolator = true;
        }
      }
      else
      {
        updateCollapsible();
        return;
      }
      paramInt1 += 1;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    invalidateScrollRanges();
  }
  
  public void removeOnOffsetChangedListener(OnOffsetChangedListener paramOnOffsetChangedListener)
  {
    if ((this.mListeners != null) && (paramOnOffsetChangedListener != null)) {
      this.mListeners.remove(paramOnOffsetChangedListener);
    }
  }
  
  public void setExpanded(boolean paramBoolean)
  {
    setExpanded(paramBoolean, ViewCompat.isLaidOut(this));
  }
  
  public void setExpanded(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1)
    {
      i = 1;
      if (!paramBoolean2) {
        break label31;
      }
    }
    label31:
    for (int j = 4;; j = 0)
    {
      this.mPendingAction = (j | i);
      requestLayout();
      return;
      i = 2;
      break;
    }
  }
  
  public void setOrientation(int paramInt)
  {
    if (paramInt != 1) {
      throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
    }
    super.setOrientation(paramInt);
  }
  
  @Deprecated
  public void setTargetElevation(float paramFloat)
  {
    if (Build.VERSION.SDK_INT >= 21) {
      ViewUtilsLollipop.setDefaultAppBarLayoutStateListAnimator(this, paramFloat);
    }
  }
  
  public static class Behavior
    extends HeaderBehavior<AppBarLayout>
  {
    private static final int INVALID_POSITION = -1;
    private static final int MAX_OFFSET_ANIMATION_DURATION = 600;
    private WeakReference<View> mLastNestedScrollingChildRef;
    private ValueAnimatorCompat mOffsetAnimator;
    private int mOffsetDelta;
    private int mOffsetToChildIndexOnLayout = -1;
    private boolean mOffsetToChildIndexOnLayoutIsMinHeight;
    private float mOffsetToChildIndexOnLayoutPerc;
    private DragCallback mOnDragCallback;
    private boolean mSkipNestedPreScroll;
    private boolean mWasNestedFlung;
    
    public Behavior() {}
    
    public Behavior(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    private void animateOffsetTo(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, int paramInt, float paramFloat)
    {
      int i = Math.abs(getTopBottomOffsetForScrollingSibling() - paramInt);
      paramFloat = Math.abs(paramFloat);
      if (paramFloat > 0.0F) {}
      for (i = Math.round(1000.0F * (i / paramFloat)) * 3;; i = (int)((1.0F + i / paramAppBarLayout.getHeight()) * 150.0F))
      {
        animateOffsetWithDuration(paramCoordinatorLayout, paramAppBarLayout, paramInt, i);
        return;
      }
    }
    
    private void animateOffsetWithDuration(final CoordinatorLayout paramCoordinatorLayout, final AppBarLayout paramAppBarLayout, int paramInt1, int paramInt2)
    {
      int i = getTopBottomOffsetForScrollingSibling();
      if (i == paramInt1)
      {
        if ((this.mOffsetAnimator != null) && (this.mOffsetAnimator.isRunning())) {
          this.mOffsetAnimator.cancel();
        }
        return;
      }
      if (this.mOffsetAnimator == null)
      {
        this.mOffsetAnimator = ViewUtils.createAnimator();
        this.mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
        this.mOffsetAnimator.addUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener()
        {
          public void onAnimationUpdate(ValueAnimatorCompat paramAnonymousValueAnimatorCompat)
          {
            AppBarLayout.Behavior.this.setHeaderTopBottomOffset(paramCoordinatorLayout, paramAppBarLayout, paramAnonymousValueAnimatorCompat.getAnimatedIntValue());
          }
        });
      }
      for (;;)
      {
        this.mOffsetAnimator.setDuration(Math.min(paramInt2, 600));
        this.mOffsetAnimator.setIntValues(i, paramInt1);
        this.mOffsetAnimator.start();
        return;
        this.mOffsetAnimator.cancel();
      }
    }
    
    private static boolean checkFlag(int paramInt1, int paramInt2)
    {
      return (paramInt1 & paramInt2) == paramInt2;
    }
    
    private static View getAppBarChildOnOffset(AppBarLayout paramAppBarLayout, int paramInt)
    {
      int i = Math.abs(paramInt);
      paramInt = 0;
      int j = paramAppBarLayout.getChildCount();
      while (paramInt < j)
      {
        View localView = paramAppBarLayout.getChildAt(paramInt);
        if ((i >= localView.getTop()) && (i <= localView.getBottom())) {
          return localView;
        }
        paramInt += 1;
      }
      return null;
    }
    
    private int getChildIndexOnOffset(AppBarLayout paramAppBarLayout, int paramInt)
    {
      int i = 0;
      int j = paramAppBarLayout.getChildCount();
      while (i < j)
      {
        View localView = paramAppBarLayout.getChildAt(i);
        if ((localView.getTop() <= -paramInt) && (localView.getBottom() >= -paramInt)) {
          return i;
        }
        i += 1;
      }
      return -1;
    }
    
    private int interpolateOffset(AppBarLayout paramAppBarLayout, int paramInt)
    {
      int k = Math.abs(paramInt);
      int j = 0;
      int m = paramAppBarLayout.getChildCount();
      for (;;)
      {
        int i = paramInt;
        if (j < m)
        {
          View localView = paramAppBarLayout.getChildAt(j);
          AppBarLayout.LayoutParams localLayoutParams = (AppBarLayout.LayoutParams)localView.getLayoutParams();
          Interpolator localInterpolator = localLayoutParams.getScrollInterpolator();
          if ((k < localView.getTop()) || (k > localView.getBottom())) {
            break label203;
          }
          i = paramInt;
          if (localInterpolator != null)
          {
            i = 0;
            m = localLayoutParams.getScrollFlags();
            if ((m & 0x1) != 0)
            {
              j = 0 + (localView.getHeight() + localLayoutParams.topMargin + localLayoutParams.bottomMargin);
              i = j;
              if ((m & 0x2) != 0) {
                i = j - ViewCompat.getMinimumHeight(localView);
              }
            }
            j = i;
            if (ViewCompat.getFitsSystemWindows(localView)) {
              j = i - paramAppBarLayout.getTopInset();
            }
            i = paramInt;
            if (j > 0)
            {
              i = localView.getTop();
              i = Math.round(j * localInterpolator.getInterpolation((k - i) / j));
              i = Integer.signum(paramInt) * (localView.getTop() + i);
            }
          }
        }
        return i;
        label203:
        j += 1;
      }
    }
    
    private boolean shouldJumpElevationState(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout)
    {
      boolean bool2 = false;
      paramCoordinatorLayout = paramCoordinatorLayout.getDependents(paramAppBarLayout);
      int i = 0;
      int j = paramCoordinatorLayout.size();
      for (;;)
      {
        boolean bool1 = bool2;
        if (i < j)
        {
          paramAppBarLayout = ((CoordinatorLayout.LayoutParams)((View)paramCoordinatorLayout.get(i)).getLayoutParams()).getBehavior();
          if (!(paramAppBarLayout instanceof AppBarLayout.ScrollingViewBehavior)) {
            break label76;
          }
          bool1 = bool2;
          if (((AppBarLayout.ScrollingViewBehavior)paramAppBarLayout).getOverlayTop() != 0) {
            bool1 = true;
          }
        }
        return bool1;
        label76:
        i += 1;
      }
    }
    
    private void snapToChildIfNeeded(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout)
    {
      int n = getTopBottomOffsetForScrollingSibling();
      int k = getChildIndexOnOffset(paramAppBarLayout, n);
      View localView;
      int i1;
      int m;
      int i;
      int j;
      if (k >= 0)
      {
        localView = paramAppBarLayout.getChildAt(k);
        i1 = ((AppBarLayout.LayoutParams)localView.getLayoutParams()).getScrollFlags();
        if ((i1 & 0x11) == 17)
        {
          m = -localView.getTop();
          i = -localView.getBottom();
          j = i;
          if (k == paramAppBarLayout.getChildCount() - 1) {
            j = i + paramAppBarLayout.getTopInset();
          }
          if (!checkFlag(i1, 2)) {
            break label139;
          }
          i = j + ViewCompat.getMinimumHeight(localView);
          k = m;
          if (n >= (i + k) / 2) {
            break label186;
          }
        }
      }
      for (;;)
      {
        animateOffsetTo(paramCoordinatorLayout, paramAppBarLayout, MathUtils.constrain(i, -paramAppBarLayout.getTotalScrollRange(), 0), 0.0F);
        return;
        label139:
        i = j;
        k = m;
        if (!checkFlag(i1, 5)) {
          break;
        }
        i = j + ViewCompat.getMinimumHeight(localView);
        if (n < i)
        {
          k = i;
          i = j;
          break;
        }
        k = m;
        break;
        label186:
        i = k;
      }
    }
    
    private void updateAppBarLayoutDrawableState(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, int paramInt1, int paramInt2)
    {
      View localView = getAppBarChildOnOffset(paramAppBarLayout, paramInt1);
      int i;
      boolean bool2;
      int j;
      if (localView != null)
      {
        i = ((AppBarLayout.LayoutParams)localView.getLayoutParams()).getScrollFlags();
        bool2 = false;
        bool1 = bool2;
        if ((i & 0x1) != 0)
        {
          j = ViewCompat.getMinimumHeight(localView);
          if ((paramInt2 <= 0) || ((i & 0xC) == 0)) {
            break label117;
          }
          if (-paramInt1 < localView.getBottom() - j - paramAppBarLayout.getTopInset()) {
            break label111;
          }
          bool1 = true;
        }
      }
      label111:
      label117:
      do
      {
        for (;;)
        {
          if ((paramAppBarLayout.setCollapsedState(bool1)) && (Build.VERSION.SDK_INT >= 11) && (shouldJumpElevationState(paramCoordinatorLayout, paramAppBarLayout))) {
            paramAppBarLayout.jumpDrawablesToCurrentState();
          }
          return;
          bool1 = false;
        }
        bool1 = bool2;
      } while ((i & 0x2) == 0);
      if (-paramInt1 >= localView.getBottom() - j - paramAppBarLayout.getTopInset()) {}
      for (boolean bool1 = true;; bool1 = false) {
        break;
      }
    }
    
    boolean canDragView(AppBarLayout paramAppBarLayout)
    {
      boolean bool2 = true;
      boolean bool1;
      if (this.mOnDragCallback != null) {
        bool1 = this.mOnDragCallback.canDrag(paramAppBarLayout);
      }
      do
      {
        do
        {
          return bool1;
          bool1 = bool2;
        } while (this.mLastNestedScrollingChildRef == null);
        paramAppBarLayout = (View)this.mLastNestedScrollingChildRef.get();
        if ((paramAppBarLayout == null) || (!paramAppBarLayout.isShown())) {
          break;
        }
        bool1 = bool2;
      } while (!ViewCompat.canScrollVertically(paramAppBarLayout, -1));
      return false;
    }
    
    int getMaxDragOffset(AppBarLayout paramAppBarLayout)
    {
      return -paramAppBarLayout.getDownNestedScrollRange();
    }
    
    int getScrollRangeForDragFling(AppBarLayout paramAppBarLayout)
    {
      return paramAppBarLayout.getTotalScrollRange();
    }
    
    int getTopBottomOffsetForScrollingSibling()
    {
      return getTopAndBottomOffset() + this.mOffsetDelta;
    }
    
    @VisibleForTesting
    boolean isOffsetAnimatorRunning()
    {
      return (this.mOffsetAnimator != null) && (this.mOffsetAnimator.isRunning());
    }
    
    void onFlingFinished(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout)
    {
      snapToChildIfNeeded(paramCoordinatorLayout, paramAppBarLayout);
    }
    
    public boolean onLayoutChild(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, int paramInt)
    {
      boolean bool = super.onLayoutChild(paramCoordinatorLayout, paramAppBarLayout, paramInt);
      int i = paramAppBarLayout.getPendingAction();
      if (i != 0) {
        if ((i & 0x4) != 0)
        {
          paramInt = 1;
          if ((i & 0x2) == 0) {
            break label111;
          }
          i = -paramAppBarLayout.getUpNestedPreScrollRange();
          if (paramInt == 0) {
            break label99;
          }
          animateOffsetTo(paramCoordinatorLayout, paramAppBarLayout, i, 0.0F);
        }
      }
      label99:
      label111:
      while (this.mOffsetToChildIndexOnLayout < 0) {
        for (;;)
        {
          paramAppBarLayout.resetPendingAction();
          this.mOffsetToChildIndexOnLayout = -1;
          setTopAndBottomOffset(MathUtils.constrain(getTopAndBottomOffset(), -paramAppBarLayout.getTotalScrollRange(), 0));
          paramAppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
          return bool;
          paramInt = 0;
          continue;
          setHeaderTopBottomOffset(paramCoordinatorLayout, paramAppBarLayout, i);
          continue;
          if ((i & 0x1) != 0) {
            if (paramInt != 0) {
              animateOffsetTo(paramCoordinatorLayout, paramAppBarLayout, 0, 0.0F);
            } else {
              setHeaderTopBottomOffset(paramCoordinatorLayout, paramAppBarLayout, 0);
            }
          }
        }
      }
      paramCoordinatorLayout = paramAppBarLayout.getChildAt(this.mOffsetToChildIndexOnLayout);
      paramInt = -paramCoordinatorLayout.getBottom();
      if (this.mOffsetToChildIndexOnLayoutIsMinHeight) {
        paramInt += ViewCompat.getMinimumHeight(paramCoordinatorLayout);
      }
      for (;;)
      {
        setTopAndBottomOffset(paramInt);
        break;
        paramInt += Math.round(paramCoordinatorLayout.getHeight() * this.mOffsetToChildIndexOnLayoutPerc);
      }
    }
    
    public boolean onMeasureChild(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (((CoordinatorLayout.LayoutParams)paramAppBarLayout.getLayoutParams()).height == -2)
      {
        paramCoordinatorLayout.onMeasureChild(paramAppBarLayout, paramInt1, paramInt2, View.MeasureSpec.makeMeasureSpec(0, 0), paramInt4);
        return true;
      }
      return super.onMeasureChild(paramCoordinatorLayout, paramAppBarLayout, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public boolean onNestedFling(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
    {
      boolean bool = false;
      if (!paramBoolean) {
        paramBoolean = fling(paramCoordinatorLayout, paramAppBarLayout, -paramAppBarLayout.getTotalScrollRange(), 0, -paramFloat2);
      }
      for (;;)
      {
        this.mWasNestedFlung = paramBoolean;
        return paramBoolean;
        int i;
        if (paramFloat2 < 0.0F)
        {
          i = -paramAppBarLayout.getTotalScrollRange() + paramAppBarLayout.getDownNestedPreScrollRange();
          paramBoolean = bool;
          if (getTopBottomOffsetForScrollingSibling() < i)
          {
            animateOffsetTo(paramCoordinatorLayout, paramAppBarLayout, i, paramFloat2);
            paramBoolean = true;
          }
        }
        else
        {
          i = -paramAppBarLayout.getUpNestedPreScrollRange();
          paramBoolean = bool;
          if (getTopBottomOffsetForScrollingSibling() > i)
          {
            animateOffsetTo(paramCoordinatorLayout, paramAppBarLayout, i, paramFloat2);
            paramBoolean = true;
          }
        }
      }
    }
    
    public void onNestedPreScroll(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt)
    {
      if ((paramInt2 != 0) && (!this.mSkipNestedPreScroll))
      {
        if (paramInt2 >= 0) {
          break label50;
        }
        paramInt1 = -paramAppBarLayout.getTotalScrollRange();
      }
      for (int i = paramInt1 + paramAppBarLayout.getDownNestedPreScrollRange();; i = 0)
      {
        paramArrayOfInt[1] = scroll(paramCoordinatorLayout, paramAppBarLayout, paramInt2, paramInt1, i);
        return;
        label50:
        paramInt1 = -paramAppBarLayout.getUpNestedPreScrollRange();
      }
    }
    
    public void onNestedScroll(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      if (paramInt4 < 0)
      {
        scroll(paramCoordinatorLayout, paramAppBarLayout, paramInt4, -paramAppBarLayout.getDownNestedScrollRange(), 0);
        this.mSkipNestedPreScroll = true;
        return;
      }
      this.mSkipNestedPreScroll = false;
    }
    
    public void onRestoreInstanceState(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, Parcelable paramParcelable)
    {
      if ((paramParcelable instanceof SavedState))
      {
        paramParcelable = (SavedState)paramParcelable;
        super.onRestoreInstanceState(paramCoordinatorLayout, paramAppBarLayout, paramParcelable.getSuperState());
        this.mOffsetToChildIndexOnLayout = paramParcelable.firstVisibleChildIndex;
        this.mOffsetToChildIndexOnLayoutPerc = paramParcelable.firstVisibleChildPercentageShown;
        this.mOffsetToChildIndexOnLayoutIsMinHeight = paramParcelable.firstVisibleChildAtMinimumHeight;
        return;
      }
      super.onRestoreInstanceState(paramCoordinatorLayout, paramAppBarLayout, paramParcelable);
      this.mOffsetToChildIndexOnLayout = -1;
    }
    
    public Parcelable onSaveInstanceState(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout)
    {
      Parcelable localParcelable = super.onSaveInstanceState(paramCoordinatorLayout, paramAppBarLayout);
      int j = getTopAndBottomOffset();
      int i = 0;
      int k = paramAppBarLayout.getChildCount();
      while (i < k)
      {
        paramCoordinatorLayout = paramAppBarLayout.getChildAt(i);
        int m = paramCoordinatorLayout.getBottom() + j;
        if ((paramCoordinatorLayout.getTop() + j <= 0) && (m >= 0))
        {
          paramAppBarLayout = new SavedState(localParcelable);
          paramAppBarLayout.firstVisibleChildIndex = i;
          if (m == ViewCompat.getMinimumHeight(paramCoordinatorLayout)) {}
          for (boolean bool = true;; bool = false)
          {
            paramAppBarLayout.firstVisibleChildAtMinimumHeight = bool;
            paramAppBarLayout.firstVisibleChildPercentageShown = (m / paramCoordinatorLayout.getHeight());
            return paramAppBarLayout;
          }
        }
        i += 1;
      }
      return localParcelable;
    }
    
    public boolean onStartNestedScroll(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, View paramView1, View paramView2, int paramInt)
    {
      if (((paramInt & 0x2) != 0) && (paramAppBarLayout.hasScrollableChildren()) && (paramCoordinatorLayout.getHeight() - paramView1.getHeight() <= paramAppBarLayout.getHeight())) {}
      for (boolean bool = true;; bool = false)
      {
        if ((bool) && (this.mOffsetAnimator != null)) {
          this.mOffsetAnimator.cancel();
        }
        this.mLastNestedScrollingChildRef = null;
        return bool;
      }
    }
    
    public void onStopNestedScroll(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, View paramView)
    {
      if (!this.mWasNestedFlung) {
        snapToChildIfNeeded(paramCoordinatorLayout, paramAppBarLayout);
      }
      this.mSkipNestedPreScroll = false;
      this.mWasNestedFlung = false;
      this.mLastNestedScrollingChildRef = new WeakReference(paramView);
    }
    
    public void setDragCallback(@Nullable DragCallback paramDragCallback)
    {
      this.mOnDragCallback = paramDragCallback;
    }
    
    int setHeaderTopBottomOffset(CoordinatorLayout paramCoordinatorLayout, AppBarLayout paramAppBarLayout, int paramInt1, int paramInt2, int paramInt3)
    {
      int j = getTopBottomOffsetForScrollingSibling();
      int i = 0;
      if ((paramInt2 != 0) && (j >= paramInt2) && (j <= paramInt3))
      {
        paramInt2 = MathUtils.constrain(paramInt1, paramInt2, paramInt3);
        paramInt1 = i;
        if (j != paramInt2)
        {
          if (!paramAppBarLayout.hasChildWithInterpolator()) {
            break label133;
          }
          paramInt1 = interpolateOffset(paramAppBarLayout, paramInt2);
          boolean bool = setTopAndBottomOffset(paramInt1);
          paramInt3 = j - paramInt2;
          this.mOffsetDelta = (paramInt2 - paramInt1);
          if ((!bool) && (paramAppBarLayout.hasChildWithInterpolator())) {
            paramCoordinatorLayout.dispatchDependentViewsChanged(paramAppBarLayout);
          }
          paramAppBarLayout.dispatchOffsetUpdates(getTopAndBottomOffset());
          if (paramInt2 >= j) {
            break label139;
          }
        }
        label133:
        label139:
        for (paramInt1 = -1;; paramInt1 = 1)
        {
          updateAppBarLayoutDrawableState(paramCoordinatorLayout, paramAppBarLayout, paramInt2, paramInt1);
          paramInt1 = paramInt3;
          return paramInt1;
          paramInt1 = paramInt2;
          break;
        }
      }
      this.mOffsetDelta = 0;
      return 0;
    }
    
    public static abstract class DragCallback
    {
      public DragCallback() {}
      
      public abstract boolean canDrag(@NonNull AppBarLayout paramAppBarLayout);
    }
    
    protected static class SavedState
      extends AbsSavedState
    {
      public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
      {
        public AppBarLayout.Behavior.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
        {
          return new AppBarLayout.Behavior.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
        }
        
        public AppBarLayout.Behavior.SavedState[] newArray(int paramAnonymousInt)
        {
          return new AppBarLayout.Behavior.SavedState[paramAnonymousInt];
        }
      });
      boolean firstVisibleChildAtMinimumHeight;
      int firstVisibleChildIndex;
      float firstVisibleChildPercentageShown;
      
      public SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
      {
        super(paramClassLoader);
        this.firstVisibleChildIndex = paramParcel.readInt();
        this.firstVisibleChildPercentageShown = paramParcel.readFloat();
        if (paramParcel.readByte() != 0) {}
        for (boolean bool = true;; bool = false)
        {
          this.firstVisibleChildAtMinimumHeight = bool;
          return;
        }
      }
      
      public SavedState(Parcelable paramParcelable)
      {
        super();
      }
      
      public void writeToParcel(Parcel paramParcel, int paramInt)
      {
        super.writeToParcel(paramParcel, paramInt);
        paramParcel.writeInt(this.firstVisibleChildIndex);
        paramParcel.writeFloat(this.firstVisibleChildPercentageShown);
        if (this.firstVisibleChildAtMinimumHeight) {}
        for (paramInt = 1;; paramInt = 0)
        {
          paramParcel.writeByte((byte)paramInt);
          return;
        }
      }
    }
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    static final int COLLAPSIBLE_FLAGS = 10;
    static final int FLAG_QUICK_RETURN = 5;
    static final int FLAG_SNAP = 17;
    public static final int SCROLL_FLAG_ENTER_ALWAYS = 4;
    public static final int SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED = 8;
    public static final int SCROLL_FLAG_EXIT_UNTIL_COLLAPSED = 2;
    public static final int SCROLL_FLAG_SCROLL = 1;
    public static final int SCROLL_FLAG_SNAP = 16;
    int mScrollFlags = 1;
    Interpolator mScrollInterpolator;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2, float paramFloat)
    {
      super(paramInt2, paramFloat);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AppBarLayout_Layout);
      this.mScrollFlags = paramAttributeSet.getInt(R.styleable.AppBarLayout_Layout_layout_scrollFlags, 0);
      if (paramAttributeSet.hasValue(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator)) {
        this.mScrollInterpolator = android.view.animation.AnimationUtils.loadInterpolator(paramContext, paramAttributeSet.getResourceId(R.styleable.AppBarLayout_Layout_layout_scrollInterpolator, 0));
      }
      paramAttributeSet.recycle();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      this.mScrollFlags = paramLayoutParams.mScrollFlags;
      this.mScrollInterpolator = paramLayoutParams.mScrollInterpolator;
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
    
    public LayoutParams(LinearLayout.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    private boolean isCollapsible()
    {
      return ((this.mScrollFlags & 0x1) == 1) && ((this.mScrollFlags & 0xA) != 0);
    }
    
    public int getScrollFlags()
    {
      return this.mScrollFlags;
    }
    
    public Interpolator getScrollInterpolator()
    {
      return this.mScrollInterpolator;
    }
    
    public void setScrollFlags(int paramInt)
    {
      this.mScrollFlags = paramInt;
    }
    
    public void setScrollInterpolator(Interpolator paramInterpolator)
    {
      this.mScrollInterpolator = paramInterpolator;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ScrollFlags {}
  }
  
  public static abstract interface OnOffsetChangedListener
  {
    public abstract void onOffsetChanged(AppBarLayout paramAppBarLayout, int paramInt);
  }
  
  public static class ScrollingViewBehavior
    extends HeaderScrollingViewBehavior
  {
    public ScrollingViewBehavior() {}
    
    public ScrollingViewBehavior(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ScrollingViewBehavior_Layout);
      setOverlayTop(paramContext.getDimensionPixelSize(R.styleable.ScrollingViewBehavior_Layout_behavior_overlapTop, 0));
      paramContext.recycle();
    }
    
    private static int getAppBarLayoutOffset(AppBarLayout paramAppBarLayout)
    {
      paramAppBarLayout = ((CoordinatorLayout.LayoutParams)paramAppBarLayout.getLayoutParams()).getBehavior();
      if ((paramAppBarLayout instanceof AppBarLayout.Behavior)) {
        return ((AppBarLayout.Behavior)paramAppBarLayout).getTopBottomOffsetForScrollingSibling();
      }
      return 0;
    }
    
    private void offsetChildAsNeeded(CoordinatorLayout paramCoordinatorLayout, View paramView1, View paramView2)
    {
      paramCoordinatorLayout = ((CoordinatorLayout.LayoutParams)paramView2.getLayoutParams()).getBehavior();
      if ((paramCoordinatorLayout instanceof AppBarLayout.Behavior))
      {
        paramCoordinatorLayout = (AppBarLayout.Behavior)paramCoordinatorLayout;
        ViewCompat.offsetTopAndBottom(paramView1, paramView2.getBottom() - paramView1.getTop() + paramCoordinatorLayout.mOffsetDelta + getVerticalLayoutGap() - getOverlapPixelsForOffset(paramView2));
      }
    }
    
    AppBarLayout findFirstDependency(List<View> paramList)
    {
      int i = 0;
      int j = paramList.size();
      while (i < j)
      {
        View localView = (View)paramList.get(i);
        if ((localView instanceof AppBarLayout)) {
          return (AppBarLayout)localView;
        }
        i += 1;
      }
      return null;
    }
    
    float getOverlapRatioForOffset(View paramView)
    {
      int j;
      int k;
      int i;
      if ((paramView instanceof AppBarLayout))
      {
        paramView = (AppBarLayout)paramView;
        j = paramView.getTotalScrollRange();
        k = paramView.getDownNestedPreScrollRange();
        i = getAppBarLayoutOffset(paramView);
        if ((k == 0) || (j + i > k)) {
          break label43;
        }
      }
      label43:
      do
      {
        return 0.0F;
        j -= k;
      } while (j == 0);
      return 1.0F + i / j;
    }
    
    int getScrollRange(View paramView)
    {
      if ((paramView instanceof AppBarLayout)) {
        return ((AppBarLayout)paramView).getTotalScrollRange();
      }
      return super.getScrollRange(paramView);
    }
    
    public boolean layoutDependsOn(CoordinatorLayout paramCoordinatorLayout, View paramView1, View paramView2)
    {
      return paramView2 instanceof AppBarLayout;
    }
    
    public boolean onDependentViewChanged(CoordinatorLayout paramCoordinatorLayout, View paramView1, View paramView2)
    {
      offsetChildAsNeeded(paramCoordinatorLayout, paramView1, paramView2);
      return false;
    }
    
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout paramCoordinatorLayout, View paramView, Rect paramRect, boolean paramBoolean)
    {
      AppBarLayout localAppBarLayout = findFirstDependency(paramCoordinatorLayout.getDependencies(paramView));
      if (localAppBarLayout != null)
      {
        paramRect.offset(paramView.getLeft(), paramView.getTop());
        paramView = this.mTempRect1;
        paramView.set(0, 0, paramCoordinatorLayout.getWidth(), paramCoordinatorLayout.getHeight());
        if (!paramView.contains(paramRect))
        {
          if (!paramBoolean) {}
          for (paramBoolean = true;; paramBoolean = false)
          {
            localAppBarLayout.setExpanded(false, paramBoolean);
            return true;
          }
        }
      }
      return false;
    }
  }
}
