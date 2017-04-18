package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.widget.CompoundButton;

class CompoundButtonCompatLollipop
{
  CompoundButtonCompatLollipop() {}
  
  static ColorStateList getButtonTintList(CompoundButton paramCompoundButton)
  {
    return paramCompoundButton.getButtonTintList();
  }
  
  static PorterDuff.Mode getButtonTintMode(CompoundButton paramCompoundButton)
  {
    return paramCompoundButton.getButtonTintMode();
  }
  
  static void setButtonTintList(CompoundButton paramCompoundButton, ColorStateList paramColorStateList)
  {
    paramCompoundButton.setButtonTintList(paramColorStateList);
  }
  
  static void setButtonTintMode(CompoundButton paramCompoundButton, PorterDuff.Mode paramMode)
  {
    paramCompoundButton.setButtonTintMode(paramMode);
  }
}
