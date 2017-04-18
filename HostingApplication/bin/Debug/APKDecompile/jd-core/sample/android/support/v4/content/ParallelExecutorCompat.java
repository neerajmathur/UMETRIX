package android.support.v4.content;

import android.os.Build.VERSION;
import java.util.concurrent.Executor;

public final class ParallelExecutorCompat
{
  private ParallelExecutorCompat() {}
  
  public static Executor getParallelExecutor()
  {
    if (Build.VERSION.SDK_INT >= 11) {
      return ExecutorCompatHoneycomb.getParallelExecutor();
    }
    return ModernAsyncTask.THREAD_POOL_EXECUTOR;
  }
}
