package com.loovee.common.xmpp.core;

import com.loovee.common.xmpp.packet.StreamError;
import com.loovee.common.xmpp.packet.XMPPError;

import java.io.PrintStream;
import java.io.PrintWriter;


public class XMPPException extends Exception {

	private StreamError streamError = null;
	private XMPPError error = null;
	private Throwable wrappedThrowable = null;

	public XMPPException() {
	}

	public XMPPException(String message) {
		super(message);
	}

	public XMPPException(Throwable wrappedThrowable) {
		this.wrappedThrowable = wrappedThrowable;
	}

	public XMPPException(StreamError streamError) {
		this.streamError = streamError;
	}

	public XMPPException(XMPPError error) {
		this.error = error;
	}

	public XMPPException(String message, Throwable wrappedThrowable) {
		super(message);
		this.wrappedThrowable = wrappedThrowable;
	}

	public XMPPException(String message, XMPPError error,
			Throwable wrappedThrowable) {
		super(message);
		this.error = error;
		this.wrappedThrowable = wrappedThrowable;
	}

	public XMPPException(String message, XMPPError error) {
		super(message);
		this.error = error;
	}

	public XMPPError getXMPPError() {
		return this.error;
	}

	public StreamError getStreamError() {
		return this.streamError;
	}

	public Throwable getWrappedThrowable() {
		return this.wrappedThrowable;
	}

	public void printStackTrace() {
		printStackTrace(System.err);
	}

	public void printStackTrace(PrintStream out) {
		super.printStackTrace(out);
		if (this.wrappedThrowable != null) {
			out.println("Nested Exception: ");
			this.wrappedThrowable.printStackTrace(out);
		}
	}

	public void printStackTrace(PrintWriter out) {
		super.printStackTrace(out);
		if (this.wrappedThrowable != null) {
			out.println("Nested Exception: ");
			this.wrappedThrowable.printStackTrace(out);
		}
	}

	public String getMessage() {
		String msg = super.getMessage();

		if ((msg == null) && (this.error != null)) {
			return this.error.toString();
		}
		if ((msg == null) && (this.streamError != null)) {
			return this.streamError.toString();
		}
		return msg;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		String message = super.getMessage();
		if (message != null) {
			buf.append(message).append(": ");
		}
		if (this.error != null) {
			buf.append(this.error);
		}
		if (this.streamError != null) {
			buf.append(this.streamError);
		}
		if (this.wrappedThrowable != null) {
			buf.append("\n  -- caused by: ").append(this.wrappedThrowable);
		}

		return buf.toString();
	}
}