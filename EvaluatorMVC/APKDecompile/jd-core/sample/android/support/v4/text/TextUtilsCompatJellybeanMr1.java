package android.support.v4.text;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;

class TextUtilsCompatJellybeanMr1
{
  TextUtilsCompatJellybeanMr1() {}
  
  public static int getLayoutDirectionFromLocale(@Nullable Locale paramLocale)
  {
    return TextUtils.getLayoutDirectionFromLocale(paramLocale);
  }
  
  @NonNull
  public static String htmlEncode(@NonNull String paramString)
  {
    return TextUtils.htmlEncode(paramString);
  }
}
