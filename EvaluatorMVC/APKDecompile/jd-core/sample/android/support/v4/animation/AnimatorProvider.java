package android.support.v4.animation;

import android.view.View;

abstract interface AnimatorProvider
{
  public abstract void clearInterpolator(View paramView);
  
  public abstract ValueAnimatorCompat emptyValueAnimator();
}
