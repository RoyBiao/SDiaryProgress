package com.loovee.common.xmpp.exception;

public class NoNetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  public NoNetworkException() {
	  }

	    /**
	     * Constructs a new {@code RuntimeException} with the current stack trace
	     * and the specified detail message.
	     *
	     * @param detailMessage
	     *            the detail message for this exception.
	     */
	    public NoNetworkException(String detailMessage) {
	        super(detailMessage);
	    }

}
