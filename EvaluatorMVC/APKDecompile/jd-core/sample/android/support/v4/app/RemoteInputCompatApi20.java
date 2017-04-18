package android.support.v4.app;

import android.app.RemoteInput;
import android.app.RemoteInput.Builder;
import android.content.Intent;
import android.os.Bundle;

class RemoteInputCompatApi20
{
  RemoteInputCompatApi20() {}
  
  static void addResultsToIntent(RemoteInputCompatBase.RemoteInput[] paramArrayOfRemoteInput, Intent paramIntent, Bundle paramBundle)
  {
    RemoteInput.addResultsToIntent(fromCompat(paramArrayOfRemoteInput), paramIntent, paramBundle);
  }
  
  static RemoteInput[] fromCompat(RemoteInputCompatBase.RemoteInput[] paramArrayOfRemoteInput)
  {
    Object localObject;
    if (paramArrayOfRemoteInput == null)
    {
      localObject = null;
      return localObject;
    }
    RemoteInput[] arrayOfRemoteInput = new RemoteInput[paramArrayOfRemoteInput.length];
    int i = 0;
    for (;;)
    {
      localObject = arrayOfRemoteInput;
      if (i >= paramArrayOfRemoteInput.length) {
        break;
      }
      localObject = paramArrayOfRemoteInput[i];
      arrayOfRemoteInput[i] = new RemoteInput.Builder(((RemoteInputCompatBase.RemoteInput)localObject).getResultKey()).setLabel(((RemoteInputCompatBase.RemoteInput)localObject).getLabel()).setChoices(((RemoteInputCompatBase.RemoteInput)localObject).getChoices()).setAllowFreeFormInput(((RemoteInputCompatBase.RemoteInput)localObject).getAllowFreeFormInput()).addExtras(((RemoteInputCompatBase.RemoteInput)localObject).getExtras()).build();
      i += 1;
    }
  }
  
  static Bundle getResultsFromIntent(Intent paramIntent)
  {
    return RemoteInput.getResultsFromIntent(paramIntent);
  }
  
  static RemoteInputCompatBase.RemoteInput[] toCompat(RemoteInput[] paramArrayOfRemoteInput, RemoteInputCompatBase.RemoteInput.Factory paramFactory)
  {
    Object localObject;
    if (paramArrayOfRemoteInput == null)
    {
      localObject = null;
      return localObject;
    }
    RemoteInputCompatBase.RemoteInput[] arrayOfRemoteInput = paramFactory.newArray(paramArrayOfRemoteInput.length);
    int i = 0;
    for (;;)
    {
      localObject = arrayOfRemoteInput;
      if (i >= paramArrayOfRemoteInput.length) {
        break;
      }
      localObject = paramArrayOfRemoteInput[i];
      arrayOfRemoteInput[i] = paramFactory.build(((RemoteInput)localObject).getResultKey(), ((RemoteInput)localObject).getLabel(), ((RemoteInput)localObject).getChoices(), ((RemoteInput)localObject).getAllowFreeFormInput(), ((RemoteInput)localObject).getExtras());
      i += 1;
    }
  }
}
