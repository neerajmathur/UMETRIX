package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.color;
import android.support.v7.appcompat.R.drawable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class AppCompatDrawableManager
{
  private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
  private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
  private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
  private static final ColorFilterLruCache COLOR_FILTER_CACHE;
  private static final boolean DEBUG = false;
  private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
  private static AppCompatDrawableManager INSTANCE;
  private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
  private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
  private static final String TAG = "AppCompatDrawableManager";
  private static final int[] TINT_CHECKABLE_BUTTON_LIST = { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
  private static final int[] TINT_COLOR_CONTROL_NORMAL;
  private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
  private ArrayMap<String, InflateDelegate> mDelegates;
  private final Object mDrawableCacheLock = new Object();
  private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap(0);
  private boolean mHasCheckedVectorDrawableSetup;
  private SparseArray<String> mKnownDrawableIdTags;
  private WeakHashMap<Context, SparseArray<ColorStateList>> mTintLists;
  private TypedValue mTypedValue;
  
  static
  {
    COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
    TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha };
    COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_alpha, R.drawable.abc_text_select_handle_middle_mtrl_alpha, R.drawable.abc_text_select_handle_right_mtrl_alpha };
    COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
    TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material };
  }
  
  public AppCompatDrawableManager() {}
  
  private void addDelegate(@NonNull String paramString, @NonNull InflateDelegate paramInflateDelegate)
  {
    if (this.mDelegates == null) {
      this.mDelegates = new ArrayMap();
    }
    this.mDelegates.put(paramString, paramInflateDelegate);
  }
  
  private boolean addDrawableToCache(@NonNull Context paramContext, long paramLong, @NonNull Drawable paramDrawable)
  {
    Drawable.ConstantState localConstantState = paramDrawable.getConstantState();
    if (localConstantState != null) {
      synchronized (this.mDrawableCacheLock)
      {
        LongSparseArray localLongSparseArray = (LongSparseArray)this.mDrawableCaches.get(paramContext);
        paramDrawable = localLongSparseArray;
        if (localLongSparseArray == null)
        {
          paramDrawable = new LongSparseArray();
          this.mDrawableCaches.put(paramContext, paramDrawable);
        }
        paramDrawable.put(paramLong, new WeakReference(localConstantState));
        return true;
      }
    }
    return false;
  }
  
  private void addTintListToCache(@NonNull Context paramContext, @DrawableRes int paramInt, @NonNull ColorStateList paramColorStateList)
  {
    if (this.mTintLists == null) {
      this.mTintLists = new WeakHashMap();
    }
    SparseArray localSparseArray2 = (SparseArray)this.mTintLists.get(paramContext);
    SparseArray localSparseArray1 = localSparseArray2;
    if (localSparseArray2 == null)
    {
      localSparseArray1 = new SparseArray();
      this.mTintLists.put(paramContext, localSparseArray1);
    }
    localSparseArray1.append(paramInt, paramColorStateList);
  }
  
  private static boolean arrayContains(int[] paramArrayOfInt, int paramInt)
  {
    boolean bool2 = false;
    int j = paramArrayOfInt.length;
    int i = 0;
    for (;;)
    {
      boolean bool1 = bool2;
      if (i < j)
      {
        if (paramArrayOfInt[i] == paramInt) {
          bool1 = true;
        }
      }
      else {
        return bool1;
      }
      i += 1;
    }
  }
  
  private void checkVectorDrawableSetup(@NonNull Context paramContext)
  {
    if (this.mHasCheckedVectorDrawableSetup) {}
    do
    {
      return;
      this.mHasCheckedVectorDrawableSetup = true;
      paramContext = getDrawable(paramContext, R.drawable.abc_vector_test);
    } while ((paramContext != null) && (isVectorDrawable(paramContext)));
    this.mHasCheckedVectorDrawableSetup = false;
    throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
  }
  
  private ColorStateList createBorderlessButtonColorStateList(@NonNull Context paramContext, @Nullable ColorStateList paramColorStateList)
  {
    return createButtonColorStateList(paramContext, 0, null);
  }
  
  private ColorStateList createButtonColorStateList(@NonNull Context paramContext, @ColorInt int paramInt, @Nullable ColorStateList paramColorStateList)
  {
    int[][] arrayOfInt = new int[4][];
    int[] arrayOfInt1 = new int[4];
    int j = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlHighlight);
    int i = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorButtonNormal);
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    int k;
    if (paramColorStateList == null)
    {
      arrayOfInt1[0] = i;
      k = 0 + 1;
      arrayOfInt[k] = ThemeUtils.PRESSED_STATE_SET;
      if (paramColorStateList != null) {
        break label161;
      }
      i = paramInt;
      label66:
      arrayOfInt1[k] = ColorUtils.compositeColors(j, i);
      k += 1;
      arrayOfInt[k] = ThemeUtils.FOCUSED_STATE_SET;
      if (paramColorStateList != null) {
        break label176;
      }
      i = paramInt;
      label99:
      arrayOfInt1[k] = ColorUtils.compositeColors(j, i);
      i = k + 1;
      arrayOfInt[i] = ThemeUtils.EMPTY_STATE_SET;
      if (paramColorStateList != null) {
        break label191;
      }
    }
    for (;;)
    {
      arrayOfInt1[i] = paramInt;
      return new ColorStateList(arrayOfInt, arrayOfInt1);
      i = paramColorStateList.getColorForState(arrayOfInt[0], 0);
      break;
      label161:
      i = paramColorStateList.getColorForState(arrayOfInt[k], 0);
      break label66;
      label176:
      i = paramColorStateList.getColorForState(arrayOfInt[k], 0);
      break label99;
      label191:
      paramInt = paramColorStateList.getColorForState(arrayOfInt[i], 0);
    }
  }
  
  private static long createCacheKey(TypedValue paramTypedValue)
  {
    return paramTypedValue.assetCookie << 32 | paramTypedValue.data;
  }
  
  private ColorStateList createColoredButtonColorStateList(@NonNull Context paramContext, @Nullable ColorStateList paramColorStateList)
  {
    return createButtonColorStateList(paramContext, ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorAccent), paramColorStateList);
  }
  
  private ColorStateList createDefaultButtonColorStateList(@NonNull Context paramContext, @Nullable ColorStateList paramColorStateList)
  {
    return createButtonColorStateList(paramContext, ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorButtonNormal), paramColorStateList);
  }
  
  private Drawable createDrawableIfNeeded(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    if (this.mTypedValue == null) {
      this.mTypedValue = new TypedValue();
    }
    TypedValue localTypedValue = this.mTypedValue;
    paramContext.getResources().getValue(paramInt, localTypedValue, true);
    long l = createCacheKey(localTypedValue);
    Object localObject = getCachedDrawable(paramContext, l);
    if (localObject != null) {
      return localObject;
    }
    if (paramInt == R.drawable.abc_cab_background_top_material) {
      localObject = new LayerDrawable(new Drawable[] { getDrawable(paramContext, R.drawable.abc_cab_background_internal_bg), getDrawable(paramContext, R.drawable.abc_cab_background_top_mtrl_alpha) });
    }
    if (localObject != null)
    {
      ((Drawable)localObject).setChangingConfigurations(localTypedValue.changingConfigurations);
      addDrawableToCache(paramContext, l, (Drawable)localObject);
    }
    return localObject;
  }
  
  private static PorterDuffColorFilter createTintFilter(ColorStateList paramColorStateList, PorterDuff.Mode paramMode, int[] paramArrayOfInt)
  {
    if ((paramColorStateList == null) || (paramMode == null)) {
      return null;
    }
    return getPorterDuffColorFilter(paramColorStateList.getColorForState(paramArrayOfInt, 0), paramMode);
  }
  
  public static AppCompatDrawableManager get()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new AppCompatDrawableManager();
      installDefaultInflateDelegates(INSTANCE);
    }
    return INSTANCE;
  }
  
  private Drawable getCachedDrawable(@NonNull Context paramContext, long paramLong)
  {
    LongSparseArray localLongSparseArray;
    synchronized (this.mDrawableCacheLock)
    {
      localLongSparseArray = (LongSparseArray)this.mDrawableCaches.get(paramContext);
      if (localLongSparseArray == null) {
        return null;
      }
      Object localObject2 = (WeakReference)localLongSparseArray.get(paramLong);
      if (localObject2 == null) {
        break label90;
      }
      localObject2 = (Drawable.ConstantState)((WeakReference)localObject2).get();
      if (localObject2 != null)
      {
        paramContext = ((Drawable.ConstantState)localObject2).newDrawable(paramContext.getResources());
        return paramContext;
      }
    }
    localLongSparseArray.delete(paramLong);
    label90:
    return null;
  }
  
  public static PorterDuffColorFilter getPorterDuffColorFilter(int paramInt, PorterDuff.Mode paramMode)
  {
    PorterDuffColorFilter localPorterDuffColorFilter2 = COLOR_FILTER_CACHE.get(paramInt, paramMode);
    PorterDuffColorFilter localPorterDuffColorFilter1 = localPorterDuffColorFilter2;
    if (localPorterDuffColorFilter2 == null)
    {
      localPorterDuffColorFilter1 = new PorterDuffColorFilter(paramInt, paramMode);
      COLOR_FILTER_CACHE.put(paramInt, paramMode, localPorterDuffColorFilter1);
    }
    return localPorterDuffColorFilter1;
  }
  
  private ColorStateList getTintListFromCache(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    Object localObject2 = null;
    Object localObject1 = localObject2;
    if (this.mTintLists != null)
    {
      paramContext = (SparseArray)this.mTintLists.get(paramContext);
      localObject1 = localObject2;
      if (paramContext != null) {
        localObject1 = (ColorStateList)paramContext.get(paramInt);
      }
    }
    return localObject1;
  }
  
  static PorterDuff.Mode getTintMode(int paramInt)
  {
    PorterDuff.Mode localMode = null;
    if (paramInt == R.drawable.abc_switch_thumb_material) {
      localMode = PorterDuff.Mode.MULTIPLY;
    }
    return localMode;
  }
  
  private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager paramAppCompatDrawableManager)
  {
    int i = Build.VERSION.SDK_INT;
    if (i < 23)
    {
      paramAppCompatDrawableManager.addDelegate("vector", new VdcInflateDelegate(null));
      if (i >= 11) {
        paramAppCompatDrawableManager.addDelegate("animated-vector", new AvdcInflateDelegate(null));
      }
    }
  }
  
  private static boolean isVectorDrawable(@NonNull Drawable paramDrawable)
  {
    return ((paramDrawable instanceof VectorDrawableCompat)) || ("android.graphics.drawable.VectorDrawable".equals(paramDrawable.getClass().getName()));
  }
  
  private Drawable loadDrawableFromDelegates(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    if ((this.mDelegates != null) && (!this.mDelegates.isEmpty()))
    {
      Object localObject1;
      Object localObject2;
      if (this.mKnownDrawableIdTags != null)
      {
        localObject1 = (String)this.mKnownDrawableIdTags.get(paramInt);
        if ((!"appcompat_skip_skip".equals(localObject1)) && ((localObject1 == null) || (this.mDelegates.get(localObject1) != null))) {
          break label81;
        }
        localObject2 = null;
      }
      label81:
      TypedValue localTypedValue;
      Object localObject4;
      long l;
      do
      {
        return localObject2;
        this.mKnownDrawableIdTags = new SparseArray();
        if (this.mTypedValue == null) {
          this.mTypedValue = new TypedValue();
        }
        localTypedValue = this.mTypedValue;
        localObject4 = paramContext.getResources();
        ((Resources)localObject4).getValue(paramInt, localTypedValue, true);
        l = createCacheKey(localTypedValue);
        localObject1 = getCachedDrawable(paramContext, l);
        localObject2 = localObject1;
      } while (localObject1 != null);
      Object localObject3 = localObject1;
      AttributeSet localAttributeSet;
      if (localTypedValue.string != null)
      {
        localObject3 = localObject1;
        if (localTypedValue.string.toString().endsWith(".xml"))
        {
          localObject3 = localObject1;
          try
          {
            localObject4 = ((Resources)localObject4).getXml(paramInt);
            localObject3 = localObject1;
            localAttributeSet = Xml.asAttributeSet((XmlPullParser)localObject4);
            int i;
            do
            {
              localObject3 = localObject1;
              i = ((XmlPullParser)localObject4).next();
            } while ((i != 2) && (i != 1));
            if (i != 2)
            {
              localObject3 = localObject1;
              throw new XmlPullParserException("No start tag found");
            }
          }
          catch (Exception paramContext)
          {
            Log.e("AppCompatDrawableManager", "Exception while inflating drawable", paramContext);
          }
        }
      }
      for (;;)
      {
        localObject2 = localObject3;
        if (localObject3 != null) {
          break;
        }
        this.mKnownDrawableIdTags.append(paramInt, "appcompat_skip_skip");
        return localObject3;
        localObject3 = localObject1;
        localObject2 = ((XmlPullParser)localObject4).getName();
        localObject3 = localObject1;
        this.mKnownDrawableIdTags.append(paramInt, localObject2);
        localObject3 = localObject1;
        InflateDelegate localInflateDelegate = (InflateDelegate)this.mDelegates.get(localObject2);
        localObject2 = localObject1;
        if (localInflateDelegate != null)
        {
          localObject3 = localObject1;
          localObject2 = localInflateDelegate.createFromXmlInner(paramContext, (XmlPullParser)localObject4, localAttributeSet, paramContext.getTheme());
        }
        localObject3 = localObject2;
        if (localObject2 != null)
        {
          localObject3 = localObject2;
          ((Drawable)localObject2).setChangingConfigurations(localTypedValue.changingConfigurations);
          localObject3 = localObject2;
          boolean bool = addDrawableToCache(paramContext, l, (Drawable)localObject2);
          localObject3 = localObject2;
          if (bool) {
            localObject3 = localObject2;
          }
        }
      }
    }
    return null;
  }
  
  private void removeDelegate(@NonNull String paramString, @NonNull InflateDelegate paramInflateDelegate)
  {
    if ((this.mDelegates != null) && (this.mDelegates.get(paramString) == paramInflateDelegate)) {
      this.mDelegates.remove(paramString);
    }
  }
  
  private static void setPorterDuffColorFilter(Drawable paramDrawable, int paramInt, PorterDuff.Mode paramMode)
  {
    Drawable localDrawable = paramDrawable;
    if (DrawableUtils.canSafelyMutateDrawable(paramDrawable)) {
      localDrawable = paramDrawable.mutate();
    }
    paramDrawable = paramMode;
    if (paramMode == null) {
      paramDrawable = DEFAULT_MODE;
    }
    localDrawable.setColorFilter(getPorterDuffColorFilter(paramInt, paramDrawable));
  }
  
  private Drawable tintDrawable(@NonNull Context paramContext, @DrawableRes int paramInt, boolean paramBoolean, @NonNull Drawable paramDrawable)
  {
    Object localObject = getTintList(paramContext, paramInt);
    if (localObject != null)
    {
      paramContext = paramDrawable;
      if (DrawableUtils.canSafelyMutateDrawable(paramDrawable)) {
        paramContext = paramDrawable.mutate();
      }
      paramContext = DrawableCompat.wrap(paramContext);
      DrawableCompat.setTintList(paramContext, (ColorStateList)localObject);
      paramDrawable = getTintMode(paramInt);
      localObject = paramContext;
      if (paramDrawable != null)
      {
        DrawableCompat.setTintMode(paramContext, paramDrawable);
        localObject = paramContext;
      }
    }
    do
    {
      do
      {
        return localObject;
        if (paramInt == R.drawable.abc_seekbar_track_material)
        {
          localObject = (LayerDrawable)paramDrawable;
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal), DEFAULT_MODE);
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal), DEFAULT_MODE);
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated), DEFAULT_MODE);
          return paramDrawable;
        }
        if ((paramInt == R.drawable.abc_ratingbar_material) || (paramInt == R.drawable.abc_ratingbar_indicator_material) || (paramInt == R.drawable.abc_ratingbar_small_material))
        {
          localObject = (LayerDrawable)paramDrawable;
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlNormal), DEFAULT_MODE);
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated), DEFAULT_MODE);
          setPorterDuffColorFilter(((LayerDrawable)localObject).findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated), DEFAULT_MODE);
          return paramDrawable;
        }
        localObject = paramDrawable;
      } while (tintDrawableUsingColorFilter(paramContext, paramInt, paramDrawable));
      localObject = paramDrawable;
    } while (!paramBoolean);
    return null;
  }
  
  static void tintDrawable(Drawable paramDrawable, TintInfo paramTintInfo, int[] paramArrayOfInt)
  {
    if ((DrawableUtils.canSafelyMutateDrawable(paramDrawable)) && (paramDrawable.mutate() != paramDrawable)) {
      Log.d("AppCompatDrawableManager", "Mutated drawable is not the same instance as the input.");
    }
    label63:
    label91:
    label103:
    for (;;)
    {
      return;
      ColorStateList localColorStateList;
      if ((paramTintInfo.mHasTintList) || (paramTintInfo.mHasTintMode)) {
        if (paramTintInfo.mHasTintList)
        {
          localColorStateList = paramTintInfo.mTintList;
          if (!paramTintInfo.mHasTintMode) {
            break label91;
          }
          paramTintInfo = paramTintInfo.mTintMode;
          paramDrawable.setColorFilter(createTintFilter(localColorStateList, paramTintInfo, paramArrayOfInt));
        }
      }
      for (;;)
      {
        if (Build.VERSION.SDK_INT > 23) {
          break label103;
        }
        paramDrawable.invalidateSelf();
        return;
        localColorStateList = null;
        break;
        paramTintInfo = DEFAULT_MODE;
        break label63;
        paramDrawable.clearColorFilter();
      }
    }
  }
  
  static boolean tintDrawableUsingColorFilter(@NonNull Context paramContext, @DrawableRes int paramInt, @NonNull Drawable paramDrawable)
  {
    Object localObject2 = DEFAULT_MODE;
    int j = 0;
    int i = 0;
    int m = -1;
    Object localObject1;
    int k;
    if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, paramInt))
    {
      i = R.attr.colorControlNormal;
      j = 1;
      localObject1 = localObject2;
      k = m;
    }
    while (j != 0)
    {
      localObject2 = paramDrawable;
      if (DrawableUtils.canSafelyMutateDrawable(paramDrawable)) {
        localObject2 = paramDrawable.mutate();
      }
      ((Drawable)localObject2).setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(paramContext, i), (PorterDuff.Mode)localObject1));
      if (k != -1) {
        ((Drawable)localObject2).setAlpha(k);
      }
      return true;
      if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, paramInt))
      {
        i = R.attr.colorControlActivated;
        j = 1;
        k = m;
        localObject1 = localObject2;
      }
      else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, paramInt))
      {
        i = 16842801;
        j = 1;
        localObject1 = PorterDuff.Mode.MULTIPLY;
        k = m;
      }
      else if (paramInt == R.drawable.abc_list_divider_mtrl_alpha)
      {
        i = 16842800;
        j = 1;
        k = Math.round(40.8F);
        localObject1 = localObject2;
      }
      else
      {
        k = m;
        localObject1 = localObject2;
        if (paramInt == R.drawable.abc_dialog_material_background)
        {
          i = 16842801;
          j = 1;
          k = m;
          localObject1 = localObject2;
        }
      }
    }
    return false;
  }
  
  public Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    return getDrawable(paramContext, paramInt, false);
  }
  
  Drawable getDrawable(@NonNull Context paramContext, @DrawableRes int paramInt, boolean paramBoolean)
  {
    checkVectorDrawableSetup(paramContext);
    Object localObject2 = loadDrawableFromDelegates(paramContext, paramInt);
    Object localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = createDrawableIfNeeded(paramContext, paramInt);
    }
    localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = ContextCompat.getDrawable(paramContext, paramInt);
    }
    localObject1 = localObject2;
    if (localObject2 != null) {
      localObject1 = tintDrawable(paramContext, paramInt, paramBoolean, (Drawable)localObject2);
    }
    if (localObject1 != null) {
      DrawableUtils.fixDrawable((Drawable)localObject1);
    }
    return localObject1;
  }
  
  ColorStateList getTintList(@NonNull Context paramContext, @DrawableRes int paramInt)
  {
    return getTintList(paramContext, paramInt, null);
  }
  
  ColorStateList getTintList(@NonNull Context paramContext, @DrawableRes int paramInt, @Nullable ColorStateList paramColorStateList)
  {
    int i;
    ColorStateList localColorStateList1;
    label20:
    ColorStateList localColorStateList2;
    if (paramColorStateList == null)
    {
      i = 1;
      if (i == 0) {
        break label78;
      }
      localColorStateList1 = getTintListFromCache(paramContext, paramInt);
      localColorStateList2 = localColorStateList1;
      if (localColorStateList1 == null)
      {
        if (paramInt != R.drawable.abc_edit_text_material) {
          break label84;
        }
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_edittext);
      }
    }
    for (;;)
    {
      localColorStateList2 = paramColorStateList;
      if (i != 0)
      {
        localColorStateList2 = paramColorStateList;
        if (paramColorStateList != null)
        {
          addTintListToCache(paramContext, paramInt, paramColorStateList);
          localColorStateList2 = paramColorStateList;
        }
      }
      return localColorStateList2;
      i = 0;
      break;
      label78:
      localColorStateList1 = null;
      break label20;
      label84:
      if (paramInt == R.drawable.abc_switch_track_mtrl_alpha)
      {
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_switch_track);
      }
      else if (paramInt == R.drawable.abc_switch_thumb_material)
      {
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_switch_thumb);
      }
      else if (paramInt == R.drawable.abc_btn_default_mtrl_shape)
      {
        paramColorStateList = createDefaultButtonColorStateList(paramContext, paramColorStateList);
      }
      else if (paramInt == R.drawable.abc_btn_borderless_material)
      {
        paramColorStateList = createBorderlessButtonColorStateList(paramContext, paramColorStateList);
      }
      else if (paramInt == R.drawable.abc_btn_colored_material)
      {
        paramColorStateList = createColoredButtonColorStateList(paramContext, paramColorStateList);
      }
      else if ((paramInt == R.drawable.abc_spinner_mtrl_am_alpha) || (paramInt == R.drawable.abc_spinner_textfield_background_material))
      {
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_spinner);
      }
      else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, paramInt))
      {
        paramColorStateList = ThemeUtils.getThemeAttrColorStateList(paramContext, R.attr.colorControlNormal);
      }
      else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, paramInt))
      {
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_default);
      }
      else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, paramInt))
      {
        paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_btn_checkable);
      }
      else
      {
        paramColorStateList = localColorStateList1;
        if (paramInt == R.drawable.abc_seekbar_thumb_material) {
          paramColorStateList = AppCompatResources.getColorStateList(paramContext, R.color.abc_tint_seek_thumb);
        }
      }
    }
  }
  
  public void onConfigurationChanged(@NonNull Context paramContext)
  {
    synchronized (this.mDrawableCacheLock)
    {
      paramContext = (LongSparseArray)this.mDrawableCaches.get(paramContext);
      if (paramContext != null) {
        paramContext.clear();
      }
      return;
    }
  }
  
  Drawable onDrawableLoadedFromResources(@NonNull Context paramContext, @NonNull VectorEnabledTintResources paramVectorEnabledTintResources, @DrawableRes int paramInt)
  {
    Drawable localDrawable2 = loadDrawableFromDelegates(paramContext, paramInt);
    Drawable localDrawable1 = localDrawable2;
    if (localDrawable2 == null) {
      localDrawable1 = paramVectorEnabledTintResources.superGetDrawable(paramInt);
    }
    if (localDrawable1 != null) {
      return tintDrawable(paramContext, paramInt, false, localDrawable1);
    }
    return null;
  }
  
  private static class AvdcInflateDelegate
    implements AppCompatDrawableManager.InflateDelegate
  {
    private AvdcInflateDelegate() {}
    
    public Drawable createFromXmlInner(@NonNull Context paramContext, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme)
    {
      try
      {
        paramContext = AnimatedVectorDrawableCompat.createFromXmlInner(paramContext, paramContext.getResources(), paramXmlPullParser, paramAttributeSet, paramTheme);
        return paramContext;
      }
      catch (Exception paramContext)
      {
        Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", paramContext);
      }
      return null;
    }
  }
  
  private static class ColorFilterLruCache
    extends LruCache<Integer, PorterDuffColorFilter>
  {
    public ColorFilterLruCache(int paramInt)
    {
      super();
    }
    
    private static int generateCacheKey(int paramInt, PorterDuff.Mode paramMode)
    {
      return (paramInt + 31) * 31 + paramMode.hashCode();
    }
    
    PorterDuffColorFilter get(int paramInt, PorterDuff.Mode paramMode)
    {
      return (PorterDuffColorFilter)get(Integer.valueOf(generateCacheKey(paramInt, paramMode)));
    }
    
    PorterDuffColorFilter put(int paramInt, PorterDuff.Mode paramMode, PorterDuffColorFilter paramPorterDuffColorFilter)
    {
      return (PorterDuffColorFilter)put(Integer.valueOf(generateCacheKey(paramInt, paramMode)), paramPorterDuffColorFilter);
    }
  }
  
  private static abstract interface InflateDelegate
  {
    public abstract Drawable createFromXmlInner(@NonNull Context paramContext, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme);
  }
  
  private static class VdcInflateDelegate
    implements AppCompatDrawableManager.InflateDelegate
  {
    private VdcInflateDelegate() {}
    
    public Drawable createFromXmlInner(@NonNull Context paramContext, @NonNull XmlPullParser paramXmlPullParser, @NonNull AttributeSet paramAttributeSet, @Nullable Resources.Theme paramTheme)
    {
      try
      {
        paramContext = VectorDrawableCompat.createFromXmlInner(paramContext.getResources(), paramXmlPullParser, paramAttributeSet, paramTheme);
        return paramContext;
      }
      catch (Exception paramContext)
      {
        Log.e("VdcInflateDelegate", "Exception while inflating <vector>", paramContext);
      }
      return null;
    }
  }
}
