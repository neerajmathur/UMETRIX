package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper
{
  private int mNestedScrollAxes;
  private final ViewGroup mViewGroup;
  
  public NestedScrollingParentHelper(ViewGroup paramViewGroup)
  {
    this.mViewGroup = paramViewGroup;
  }
  
  public int getNestedScrollAxes()
  {
    return this.mNestedScrollAxes;
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    this.mNestedScrollAxes = paramInt;
  }
  
  public void onStopNestedScroll(View paramView)
  {
    this.mNestedScrollAxes = 0;
  }
}
