package android.support.v4.content;

import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

public final class SharedPreferencesCompat
{
  private SharedPreferencesCompat() {}
  
  public static final class EditorCompat
  {
    private static EditorCompat sInstance;
    private final Helper mHelper = new Helper(null);
    
    private EditorCompat() {}
    
    public static EditorCompat getInstance()
    {
      if (sInstance == null) {
        sInstance = new EditorCompat();
      }
      return sInstance;
    }
    
    public void apply(@NonNull SharedPreferences.Editor paramEditor)
    {
      this.mHelper.apply(paramEditor);
    }
    
    private static class Helper
    {
      private Helper() {}
      
      public void apply(@NonNull SharedPreferences.Editor paramEditor)
      {
        try
        {
          paramEditor.apply();
          return;
        }
        catch (AbstractMethodError localAbstractMethodError)
        {
          paramEditor.commit();
        }
      }
    }
  }
}
