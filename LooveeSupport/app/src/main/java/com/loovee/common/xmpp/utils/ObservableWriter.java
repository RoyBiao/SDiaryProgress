package com.loovee.common.xmpp.utils;


import com.loovee.common.util.LogUtils;

import java.io.IOException;
import java.io.Writer;

public class ObservableWriter extends Writer
{
  Writer wrappedWriter = null;

  public ObservableWriter(Writer wrappedWriter) {
    this.wrappedWriter = wrappedWriter;
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    this.wrappedWriter.write(cbuf, off, len);
    String str = new String(cbuf, off, len);
    LogUtils.jLog().e(str);
  }

  @Override
  public void flush() throws IOException {
    this.wrappedWriter.flush();
  }

  @Override
  public void close() throws IOException {
    this.wrappedWriter.close();
  }

  @Override
  public void write(int c) throws IOException {
    this.wrappedWriter.write(c);
  }

  @Override
  public void write(char[] cbuf) throws IOException {
    this.wrappedWriter.write(cbuf);
    String str = new String(cbuf);
    LogUtils.jLog().e(str);
  }

  public void write(String str) throws IOException {
    this.wrappedWriter.write(str);
    LogUtils.jLog().e(str);
  }

  public void write(String str, int off, int len) throws IOException {
    this.wrappedWriter.write(str, off, len);
    str = str.substring(off, off + len);
    LogUtils.jLog().e(str);
  }


}
