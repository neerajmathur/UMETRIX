package android.support.v4.animation;

public abstract interface AnimatorListenerCompat
{
  public abstract void onAnimationCancel(ValueAnimatorCompat paramValueAnimatorCompat);
  
  public abstract void onAnimationEnd(ValueAnimatorCompat paramValueAnimatorCompat);
  
  public abstract void onAnimationRepeat(ValueAnimatorCompat paramValueAnimatorCompat);
  
  public abstract void onAnimationStart(ValueAnimatorCompat paramValueAnimatorCompat);
}
