package com.loovee.common.xmpp.core;


import com.loovee.common.xmpp.filter.PacketFilter;
import com.loovee.common.xmpp.packet.Packet;

import java.util.LinkedList;

public class PacketCollector
{
  private static final int MAX_PACKETS = 65536;
  private PacketFilter packetFilter;
  private LinkedList<Packet> resultQueue;
  private PacketReader packetReader;
  private boolean cancelled = false;

  protected PacketCollector(PacketReader packetReader, PacketFilter packetFilter)
  {
    this.packetReader = packetReader;
    this.packetFilter = packetFilter;
    this.resultQueue = new LinkedList<Packet>();
  }

  public void cancel()
  {
    if (!this.cancelled) {
      this.cancelled = true;
      this.packetReader.cancelPacketCollector(this);
    }
  }

  public PacketFilter getPacketFilter()
  {
    return this.packetFilter;
  }

  public synchronized Packet pollResult()
  {
    if (this.resultQueue.isEmpty()) {
      return null;
    }

    return (Packet)this.resultQueue.removeLast();
  }

  public synchronized Packet nextResult()
  {
    while (this.resultQueue.isEmpty()) {
      try {
        wait();
      }
      catch (InterruptedException ie)
      {
      }
    }
    return (Packet)this.resultQueue.removeLast();
  }

  public synchronized Packet nextResult(long timeout)
  {
    if (this.resultQueue.isEmpty()) {
      long waitTime = timeout;
      long start = System.currentTimeMillis();
      try
      {
        while ((this.resultQueue.isEmpty()) && 
          (waitTime > 0L))
        {
          wait(waitTime);
          long now = System.currentTimeMillis();
          waitTime -= now - start;
          start = now;
        }
      }
      catch (InterruptedException ie)
      {
      }

      if (this.resultQueue.isEmpty()) {
        return null;
      }

      return (Packet)this.resultQueue.removeLast();
    }

    return (Packet)this.resultQueue.removeLast();
  }

  protected synchronized void processPacket(Packet packet)
  {
    if (packet == null) {
      return;
    }
    if ((this.packetFilter == null) || (this.packetFilter.accept(packet)))
    {
      if (this.resultQueue.size() == 65536) {
        this.resultQueue.removeLast();
      }

      this.resultQueue.addFirst(packet);

      notifyAll();
    }
  }
}
