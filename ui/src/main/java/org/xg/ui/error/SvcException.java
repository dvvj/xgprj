package org.xg.ui.error;

public class SvcException extends RuntimeException {
  private SvcException(String msg) {
    super(msg);
  }

  private SvcException(String msg, Exception innerException) {
    super(msg, innerException);
  }

  public static SvcException create(String msg) {
    return new SvcException(msg);
  }
}
