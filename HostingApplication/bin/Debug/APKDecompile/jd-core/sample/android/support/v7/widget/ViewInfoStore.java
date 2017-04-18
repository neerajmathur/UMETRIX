package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;

class ViewInfoStore
{
  private static final boolean DEBUG = false;
  @VisibleForTesting
  final ArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
  @VisibleForTesting
  final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray();
  
  ViewInfoStore() {}
  
  private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder paramViewHolder, int paramInt)
  {
    Object localObject2 = null;
    int i = this.mLayoutHolderMap.indexOfKey(paramViewHolder);
    Object localObject1;
    if (i < 0) {
      localObject1 = localObject2;
    }
    InfoRecord localInfoRecord;
    do
    {
      do
      {
        return localObject1;
        localInfoRecord = (InfoRecord)this.mLayoutHolderMap.valueAt(i);
        localObject1 = localObject2;
      } while (localInfoRecord == null);
      localObject1 = localObject2;
    } while ((localInfoRecord.flags & paramInt) == 0);
    localInfoRecord.flags &= (paramInt ^ 0xFFFFFFFF);
    if (paramInt == 4) {}
    for (paramViewHolder = localInfoRecord.preInfo;; paramViewHolder = localInfoRecord.postInfo)
    {
      localObject1 = paramViewHolder;
      if ((localInfoRecord.flags & 0xC) != 0) {
        break;
      }
      this.mLayoutHolderMap.removeAt(i);
      InfoRecord.recycle(localInfoRecord);
      return paramViewHolder;
      if (paramInt != 8) {
        break label129;
      }
    }
    label129:
    throw new IllegalArgumentException("Must provide flag PRE or POST");
  }
  
  void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord1 = localInfoRecord2;
    if (localInfoRecord2 == null)
    {
      localInfoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, localInfoRecord1);
    }
    localInfoRecord1.flags |= 0x2;
    localInfoRecord1.preInfo = paramItemHolderInfo;
  }
  
  void addToDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    InfoRecord localInfoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord1 = localInfoRecord2;
    if (localInfoRecord2 == null)
    {
      localInfoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, localInfoRecord1);
    }
    localInfoRecord1.flags |= 0x1;
  }
  
  void addToOldChangeHolders(long paramLong, RecyclerView.ViewHolder paramViewHolder)
  {
    this.mOldChangedHolders.put(paramLong, paramViewHolder);
  }
  
  void addToPostLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord1 = localInfoRecord2;
    if (localInfoRecord2 == null)
    {
      localInfoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, localInfoRecord1);
    }
    localInfoRecord1.postInfo = paramItemHolderInfo;
    localInfoRecord1.flags |= 0x8;
  }
  
  void addToPreLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo)
  {
    InfoRecord localInfoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord localInfoRecord1 = localInfoRecord2;
    if (localInfoRecord2 == null)
    {
      localInfoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, localInfoRecord1);
    }
    localInfoRecord1.preInfo = paramItemHolderInfo;
    localInfoRecord1.flags |= 0x4;
  }
  
  void clear()
  {
    this.mLayoutHolderMap.clear();
    this.mOldChangedHolders.clear();
  }
  
  RecyclerView.ViewHolder getFromOldChangeHolders(long paramLong)
  {
    return (RecyclerView.ViewHolder)this.mOldChangedHolders.get(paramLong);
  }
  
  boolean isDisappearing(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    return (paramViewHolder != null) && ((paramViewHolder.flags & 0x1) != 0);
  }
  
  boolean isInPreLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    return (paramViewHolder != null) && ((paramViewHolder.flags & 0x4) != 0);
  }
  
  void onDetach() {}
  
  public void onViewDetached(RecyclerView.ViewHolder paramViewHolder)
  {
    removeFromDisappearedInLayout(paramViewHolder);
  }
  
  @Nullable
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    return popFromLayoutStep(paramViewHolder, 8);
  }
  
  @Nullable
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    return popFromLayoutStep(paramViewHolder, 4);
  }
  
  void process(ProcessCallback paramProcessCallback)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 29	android/support/v7/widget/ViewInfoStore:mLayoutHolderMap	Landroid/support/v4/util/ArrayMap;
    //   4: invokevirtual 128	android/support/v4/util/ArrayMap:size	()I
    //   7: iconst_1
    //   8: isub
    //   9: istore_2
    //   10: iload_2
    //   11: iflt +242 -> 253
    //   14: aload_0
    //   15: getfield 29	android/support/v7/widget/ViewInfoStore:mLayoutHolderMap	Landroid/support/v4/util/ArrayMap;
    //   18: iload_2
    //   19: invokevirtual 131	android/support/v4/util/ArrayMap:keyAt	(I)Ljava/lang/Object;
    //   22: checkcast 104	android/support/v7/widget/RecyclerView$ViewHolder
    //   25: astore_3
    //   26: aload_0
    //   27: getfield 29	android/support/v7/widget/ViewInfoStore:mLayoutHolderMap	Landroid/support/v4/util/ArrayMap;
    //   30: iload_2
    //   31: invokevirtual 56	android/support/v4/util/ArrayMap:removeAt	(I)Ljava/lang/Object;
    //   34: checkcast 6	android/support/v7/widget/ViewInfoStore$InfoRecord
    //   37: astore 4
    //   39: aload 4
    //   41: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   44: iconst_3
    //   45: iand
    //   46: iconst_3
    //   47: if_icmpne +22 -> 69
    //   50: aload_1
    //   51: aload_3
    //   52: invokeinterface 134 2 0
    //   57: aload 4
    //   59: invokestatic 60	android/support/v7/widget/ViewInfoStore$InfoRecord:recycle	(Landroid/support/v7/widget/ViewInfoStore$InfoRecord;)V
    //   62: iload_2
    //   63: iconst_1
    //   64: isub
    //   65: istore_2
    //   66: goto -56 -> 10
    //   69: aload 4
    //   71: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   74: iconst_1
    //   75: iand
    //   76: ifeq +41 -> 117
    //   79: aload 4
    //   81: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   84: ifnonnull +13 -> 97
    //   87: aload_1
    //   88: aload_3
    //   89: invokeinterface 134 2 0
    //   94: goto -37 -> 57
    //   97: aload_1
    //   98: aload_3
    //   99: aload 4
    //   101: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   104: aload 4
    //   106: getfield 63	android/support/v7/widget/ViewInfoStore$InfoRecord:postInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   109: invokeinterface 138 4 0
    //   114: goto -57 -> 57
    //   117: aload 4
    //   119: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   122: bipush 14
    //   124: iand
    //   125: bipush 14
    //   127: if_icmpne +23 -> 150
    //   130: aload_1
    //   131: aload_3
    //   132: aload 4
    //   134: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   137: aload 4
    //   139: getfield 63	android/support/v7/widget/ViewInfoStore$InfoRecord:postInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   142: invokeinterface 141 4 0
    //   147: goto -90 -> 57
    //   150: aload 4
    //   152: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   155: bipush 12
    //   157: iand
    //   158: bipush 12
    //   160: if_icmpne +23 -> 183
    //   163: aload_1
    //   164: aload_3
    //   165: aload 4
    //   167: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   170: aload 4
    //   172: getfield 63	android/support/v7/widget/ViewInfoStore$InfoRecord:postInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   175: invokeinterface 144 4 0
    //   180: goto -123 -> 57
    //   183: aload 4
    //   185: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   188: iconst_4
    //   189: iand
    //   190: ifeq +19 -> 209
    //   193: aload_1
    //   194: aload_3
    //   195: aload 4
    //   197: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   200: aconst_null
    //   201: invokeinterface 138 4 0
    //   206: goto -149 -> 57
    //   209: aload 4
    //   211: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   214: bipush 8
    //   216: iand
    //   217: ifeq +23 -> 240
    //   220: aload_1
    //   221: aload_3
    //   222: aload 4
    //   224: getfield 53	android/support/v7/widget/ViewInfoStore$InfoRecord:preInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   227: aload 4
    //   229: getfield 63	android/support/v7/widget/ViewInfoStore$InfoRecord:postInfo	Landroid/support/v7/widget/RecyclerView$ItemAnimator$ItemHolderInfo;
    //   232: invokeinterface 141 4 0
    //   237: goto -180 -> 57
    //   240: aload 4
    //   242: getfield 49	android/support/v7/widget/ViewInfoStore$InfoRecord:flags	I
    //   245: iconst_2
    //   246: iand
    //   247: ifeq -190 -> 57
    //   250: goto -193 -> 57
    //   253: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	254	0	this	ViewInfoStore
    //   0	254	1	paramProcessCallback	ProcessCallback
    //   9	57	2	i	int
    //   25	197	3	localViewHolder	RecyclerView.ViewHolder
    //   37	204	4	localInfoRecord	InfoRecord
  }
  
  void removeFromDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder)
  {
    paramViewHolder = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    if (paramViewHolder == null) {
      return;
    }
    paramViewHolder.flags &= 0xFFFFFFFE;
  }
  
  void removeViewHolder(RecyclerView.ViewHolder paramViewHolder)
  {
    int i = this.mOldChangedHolders.size() - 1;
    for (;;)
    {
      if (i >= 0)
      {
        if (paramViewHolder == this.mOldChangedHolders.valueAt(i)) {
          this.mOldChangedHolders.removeAt(i);
        }
      }
      else
      {
        paramViewHolder = (InfoRecord)this.mLayoutHolderMap.remove(paramViewHolder);
        if (paramViewHolder != null) {
          InfoRecord.recycle(paramViewHolder);
        }
        return;
      }
      i -= 1;
    }
  }
  
  static class InfoRecord
  {
    static final int FLAG_APPEAR = 2;
    static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
    static final int FLAG_APPEAR_PRE_AND_POST = 14;
    static final int FLAG_DISAPPEARED = 1;
    static final int FLAG_POST = 8;
    static final int FLAG_PRE = 4;
    static final int FLAG_PRE_AND_POST = 12;
    static Pools.Pool<InfoRecord> sPool = new Pools.SimplePool(20);
    int flags;
    @Nullable
    RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
    @Nullable
    RecyclerView.ItemAnimator.ItemHolderInfo preInfo;
    
    private InfoRecord() {}
    
    static void drainCache()
    {
      while (sPool.acquire() != null) {}
    }
    
    static InfoRecord obtain()
    {
      InfoRecord localInfoRecord2 = (InfoRecord)sPool.acquire();
      InfoRecord localInfoRecord1 = localInfoRecord2;
      if (localInfoRecord2 == null) {
        localInfoRecord1 = new InfoRecord();
      }
      return localInfoRecord1;
    }
    
    static void recycle(InfoRecord paramInfoRecord)
    {
      paramInfoRecord.flags = 0;
      paramInfoRecord.preInfo = null;
      paramInfoRecord.postInfo = null;
      sPool.release(paramInfoRecord);
    }
  }
  
  static abstract interface ProcessCallback
  {
    public abstract void processAppeared(RecyclerView.ViewHolder paramViewHolder, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void processDisappeared(RecyclerView.ViewHolder paramViewHolder, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, @Nullable RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void processPersistent(RecyclerView.ViewHolder paramViewHolder, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo1, @NonNull RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo2);
    
    public abstract void unused(RecyclerView.ViewHolder paramViewHolder);
  }
}
