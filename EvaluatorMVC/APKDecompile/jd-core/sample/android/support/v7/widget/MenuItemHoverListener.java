package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuItem;

public abstract interface MenuItemHoverListener
{
  public abstract void onItemHoverEnter(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem);
  
  public abstract void onItemHoverExit(@NonNull MenuBuilder paramMenuBuilder, @NonNull MenuItem paramMenuItem);
}
