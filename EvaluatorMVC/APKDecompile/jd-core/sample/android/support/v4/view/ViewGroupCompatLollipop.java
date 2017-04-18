package android.support.v4.view;

import android.view.ViewGroup;

class ViewGroupCompatLollipop
{
  ViewGroupCompatLollipop() {}
  
  public static int getNestedScrollAxes(ViewGroup paramViewGroup)
  {
    return paramViewGroup.getNestedScrollAxes();
  }
  
  public static boolean isTransitionGroup(ViewGroup paramViewGroup)
  {
    return paramViewGroup.isTransitionGroup();
  }
  
  public static void setTransitionGroup(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    paramViewGroup.setTransitionGroup(paramBoolean);
  }
}
