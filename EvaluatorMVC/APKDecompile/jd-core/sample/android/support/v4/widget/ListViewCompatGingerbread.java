package android.support.v4.widget;

import android.view.View;
import android.widget.ListView;

class ListViewCompatGingerbread
{
  ListViewCompatGingerbread() {}
  
  static void scrollListBy(ListView paramListView, int paramInt)
  {
    int i = paramListView.getFirstVisiblePosition();
    if (i == -1) {}
    View localView;
    do
    {
      return;
      localView = paramListView.getChildAt(0);
    } while (localView == null);
    paramListView.setSelectionFromTop(i, localView.getTop() - paramInt);
  }
}
