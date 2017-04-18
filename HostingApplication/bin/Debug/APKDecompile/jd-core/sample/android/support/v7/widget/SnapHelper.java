package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public abstract class SnapHelper
  extends RecyclerView.OnFlingListener
{
  private static final float MILLISECONDS_PER_INCH = 100.0F;
  private Scroller mGravityScroller;
  private RecyclerView mRecyclerView;
  private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener()
  {
    public void onScrollStateChanged(RecyclerView paramAnonymousRecyclerView, int paramAnonymousInt)
    {
      super.onScrollStateChanged(paramAnonymousRecyclerView, paramAnonymousInt);
      if (paramAnonymousInt == 0) {
        SnapHelper.this.snapToTargetExistingView();
      }
    }
  };
  
  public SnapHelper() {}
  
  @Nullable
  private LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager paramLayoutManager)
  {
    if (!(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
      return null;
    }
    new LinearSmoothScroller(this.mRecyclerView.getContext())
    {
      protected float calculateSpeedPerPixel(DisplayMetrics paramAnonymousDisplayMetrics)
      {
        return 100.0F / paramAnonymousDisplayMetrics.densityDpi;
      }
      
      protected void onTargetFound(View paramAnonymousView, RecyclerView.State paramAnonymousState, RecyclerView.SmoothScroller.Action paramAnonymousAction)
      {
        paramAnonymousView = SnapHelper.this.calculateDistanceToFinalSnap(SnapHelper.this.mRecyclerView.getLayoutManager(), paramAnonymousView);
        int i = paramAnonymousView[0];
        int j = paramAnonymousView[1];
        int k = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(j)));
        if (k > 0) {
          paramAnonymousAction.update(i, j, k, this.mDecelerateInterpolator);
        }
      }
    };
  }
  
  private void destroyCallbacks()
  {
    this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
    this.mRecyclerView.setOnFlingListener(null);
  }
  
  private void setupCallbacks()
    throws IllegalStateException
  {
    if (this.mRecyclerView.getOnFlingListener() != null) {
      throw new IllegalStateException("An instance of OnFlingListener already set.");
    }
    this.mRecyclerView.addOnScrollListener(this.mScrollListener);
    this.mRecyclerView.setOnFlingListener(this);
  }
  
  private boolean snapFromFling(@NonNull RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2)
  {
    if (!(paramLayoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {}
    LinearSmoothScroller localLinearSmoothScroller;
    do
    {
      do
      {
        return false;
        localLinearSmoothScroller = createSnapScroller(paramLayoutManager);
      } while (localLinearSmoothScroller == null);
      paramInt1 = findTargetSnapPosition(paramLayoutManager, paramInt1, paramInt2);
    } while (paramInt1 == -1);
    localLinearSmoothScroller.setTargetPosition(paramInt1);
    paramLayoutManager.startSmoothScroll(localLinearSmoothScroller);
    return true;
  }
  
  private void snapToTargetExistingView()
  {
    if (this.mRecyclerView == null) {}
    Object localObject;
    do
    {
      View localView;
      do
      {
        do
        {
          return;
          localObject = this.mRecyclerView.getLayoutManager();
        } while (localObject == null);
        localView = findSnapView((RecyclerView.LayoutManager)localObject);
      } while (localView == null);
      localObject = calculateDistanceToFinalSnap((RecyclerView.LayoutManager)localObject, localView);
    } while ((localObject[0] == 0) && (localObject[1] == 0));
    this.mRecyclerView.smoothScrollBy(localObject[0], localObject[1]);
  }
  
  public void attachToRecyclerView(@Nullable RecyclerView paramRecyclerView)
    throws IllegalStateException
  {
    if (this.mRecyclerView == paramRecyclerView) {}
    do
    {
      return;
      if (this.mRecyclerView != null) {
        destroyCallbacks();
      }
      this.mRecyclerView = paramRecyclerView;
    } while (this.mRecyclerView == null);
    setupCallbacks();
    this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
    snapToTargetExistingView();
  }
  
  @Nullable
  public abstract int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager paramLayoutManager, @NonNull View paramView);
  
  public int[] calculateScrollDistance(int paramInt1, int paramInt2)
  {
    this.mGravityScroller.fling(0, 0, paramInt1, paramInt2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
    return new int[] { this.mGravityScroller.getFinalX(), this.mGravityScroller.getFinalY() };
  }
  
  @Nullable
  public abstract View findSnapView(RecyclerView.LayoutManager paramLayoutManager);
  
  public abstract int findTargetSnapPosition(RecyclerView.LayoutManager paramLayoutManager, int paramInt1, int paramInt2);
  
  public boolean onFling(int paramInt1, int paramInt2)
  {
    RecyclerView.LayoutManager localLayoutManager = this.mRecyclerView.getLayoutManager();
    if (localLayoutManager == null) {}
    int i;
    do
    {
      do
      {
        return false;
      } while (this.mRecyclerView.getAdapter() == null);
      i = this.mRecyclerView.getMinFlingVelocity();
    } while (((Math.abs(paramInt2) <= i) && (Math.abs(paramInt1) <= i)) || (!snapFromFling(localLayoutManager, paramInt1, paramInt2)));
    return true;
  }
}
