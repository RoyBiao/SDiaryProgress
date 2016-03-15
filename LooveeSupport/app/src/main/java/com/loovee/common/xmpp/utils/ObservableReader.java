package com.loovee.common.xmpp.utils;


import com.loovee.common.util.LogUtils;

import java.io.IOException;
import java.io.Reader;

public class ObservableReader extends Reader
{
  Reader wrappedReader = null;

  public ObservableReader(Reader wrappedReader) {
    this.wrappedReader = wrappedReader;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    int count = this.wrappedReader.read(cbuf, off, len);
    if (count > 0) {
      String str = new String(cbuf, off, count);
      LogUtils.jLog().e(str);
    }
    return count;
  }

  @Override
  public void close() throws IOException {
    this.wrappedReader.close();
  }

  @Override
  public int read() throws IOException {
    return this.wrappedReader.read();
  }

  @Override
  public int read(char[] cbuf) throws IOException {
    return this.wrappedReader.read(cbuf);
  }

  @Override
  public long skip(long n) throws IOException {
    return this.wrappedReader.skip(n);
  }
  @Override
  public boolean ready() throws IOException {
    return this.wrappedReader.ready();
  }

  public boolean markSupported() {
    return this.wrappedReader.markSupported();
  }

  public void mark(int readAheadLimit) throws IOException {
    this.wrappedReader.mark(readAheadLimit);
  }

  public void reset() throws IOException {
    this.wrappedReader.reset();
  }

}