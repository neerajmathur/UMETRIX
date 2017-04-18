package android.support.v4.net;

import android.net.TrafficStats;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TrafficStatsCompatApi24
{
  public TrafficStatsCompatApi24() {}
  
  public static void tagDatagramSocket(DatagramSocket paramDatagramSocket)
    throws SocketException
  {
    TrafficStats.tagDatagramSocket(paramDatagramSocket);
  }
  
  public static void untagDatagramSocket(DatagramSocket paramDatagramSocket)
    throws SocketException
  {
    TrafficStats.untagDatagramSocket(paramDatagramSocket);
  }
}
