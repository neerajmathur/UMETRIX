package android.support.v4.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;

class PathInterpolatorCompatBase
{
  private PathInterpolatorCompatBase() {}
  
  public static Interpolator create(float paramFloat1, float paramFloat2)
  {
    return new PathInterpolatorGingerbread(paramFloat1, paramFloat2);
  }
  
  public static Interpolator create(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return new PathInterpolatorGingerbread(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public static Interpolator create(Path paramPath)
  {
    return new PathInterpolatorGingerbread(paramPath);
  }
}
