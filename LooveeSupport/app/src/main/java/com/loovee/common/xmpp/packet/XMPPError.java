package com.loovee.common.xmpp.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMPPError {
	public static final int CODE_TIME_OUT = 500;
	
	
	
	
	
	private int code;
	private Type type;
	private String condition;
	private String message;
	private List<PacketExtension> applicationExtensions = null;

	public XMPPError(Condition condition) {
		init(condition);
		this.message = null;
	}

	public XMPPError(Condition condition, String messageText) {
		init(condition);
		this.message = messageText;
	}

	/** @deprecated */
	public XMPPError(int code) {
		this.code = code;
		this.message = null;
	}


	public XMPPError(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public XMPPError(int code, Type type, String condition, String message,
			List<PacketExtension> extension) {
		this.code = code;
		this.type = type;
		this.condition = condition;
		this.message = message;
		this.applicationExtensions = extension;
	}

	private void init(Condition condition) {
		ErrorSpecification defaultErrorSpecification = ErrorSpecification
				.specFor(condition);
		this.condition = condition.value;
		if (defaultErrorSpecification != null) {
			this.type = defaultErrorSpecification.getType();
			this.code = defaultErrorSpecification.getCode();
		}
	}

	public String getCondition() {
		return this.condition;
	}

	public Type getType() {
		return this.type;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public String toXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<error code=\"").append(this.code).append("\"");
		if (this.type != null) {
			buf.append(" type=\"");
			buf.append(this.type.name());
			buf.append("\"");
		}
		buf.append(">");
		if (this.condition != null) {
			buf.append("<").append(this.condition);
			buf.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
		}
		if (this.message != null) {
			buf.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
			buf.append(this.message);
			buf.append("</text>");
		}
		for (PacketExtension element : getExtensions()) {
			buf.append(element.toXML());
		}
		buf.append("</error>");
		return buf.toString();
	}

	public String toString() {
		StringBuffer txt = new StringBuffer();
		if (this.condition != null) {
			txt.append(this.condition);
		}
		txt.append("(").append(this.code).append(")");
		if (this.message != null) {
			txt.append(" ").append(this.message);
		}
		return txt.toString();
	}

	public synchronized List<PacketExtension> getExtensions() {
		if (this.applicationExtensions == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.applicationExtensions);
	}

	public synchronized PacketExtension getExtension(String elementName,
													 String namespace) {
		if ((this.applicationExtensions == null) || (elementName == null)
				|| (namespace == null)) {
			return null;
		}
		for (PacketExtension ext : this.applicationExtensions) {
			if ((elementName.equals(ext.getElementName()))
					&& (namespace.equals(ext.getNamespace()))) {
				return ext;
			}
		}
		return null;
	}

	public synchronized void addExtension(PacketExtension extension) {
		if (this.applicationExtensions == null) {
			this.applicationExtensions = new ArrayList<PacketExtension>();
		}
		this.applicationExtensions.add(extension);
	}

	public synchronized void setExtension(List<PacketExtension> extension) {
		this.applicationExtensions = extension;
	}

	private static class ErrorSpecification {
		private int code;
		private XMPPError.Type type;
		private XMPPError.Condition condition;
		private static Map<Condition, ErrorSpecification> instances = errorSpecifications();

		private ErrorSpecification(XMPPError.Condition condition,
								   XMPPError.Type type, int code) {
			this.code = code;
			this.type = type;
			this.condition = condition;
		}

		private static Map<Condition, ErrorSpecification> errorSpecifications() {
			Map<Condition,ErrorSpecification>instances = new HashMap<Condition,ErrorSpecification>(22);
			instances.put(XMPPError.Condition.interna_server_error,
					new ErrorSpecification(
							XMPPError.Condition.interna_server_error,
							XMPPError.Type.WAIT, 500));

			instances.put(XMPPError.Condition.forbidden,
					new ErrorSpecification(XMPPError.Condition.forbidden,
							XMPPError.Type.AUTH, 403));

			instances.put(XMPPError.Condition.bad_request,
					new ErrorSpecification(XMPPError.Condition.bad_request,
							XMPPError.Type.MODIFY, 400));

			instances.put(XMPPError.Condition.item_not_found,
					new ErrorSpecification(XMPPError.Condition.item_not_found,
							XMPPError.Type.CANCEL, 404));

			instances.put(XMPPError.Condition.conflict, new ErrorSpecification(
					XMPPError.Condition.conflict, XMPPError.Type.CANCEL, 409));

			instances.put(XMPPError.Condition.feature_not_implemented,
					new ErrorSpecification(
							XMPPError.Condition.feature_not_implemented,
							XMPPError.Type.CANCEL, 501));

			instances.put(XMPPError.Condition.gone, new ErrorSpecification(
					XMPPError.Condition.gone, XMPPError.Type.MODIFY, 302));

			instances.put(XMPPError.Condition.jid_malformed,
					new ErrorSpecification(XMPPError.Condition.jid_malformed,
							XMPPError.Type.MODIFY, 400));

			instances.put(XMPPError.Condition.no_acceptable,
					new ErrorSpecification(XMPPError.Condition.no_acceptable,
							XMPPError.Type.MODIFY, 406));

			instances.put(XMPPError.Condition.not_allowed,
					new ErrorSpecification(XMPPError.Condition.not_allowed,
							XMPPError.Type.CANCEL, 405));

			instances.put(XMPPError.Condition.not_authorized,
					new ErrorSpecification(XMPPError.Condition.not_authorized,
							XMPPError.Type.AUTH, 401));

			instances.put(XMPPError.Condition.payment_required,
					new ErrorSpecification(
							XMPPError.Condition.payment_required,
							XMPPError.Type.AUTH, 402));

			instances.put(XMPPError.Condition.recipient_unavailable,
					new ErrorSpecification(
							XMPPError.Condition.recipient_unavailable,
							XMPPError.Type.WAIT, 404));

			instances.put(XMPPError.Condition.redirect, new ErrorSpecification(
					XMPPError.Condition.redirect, XMPPError.Type.MODIFY, 302));

			instances.put(XMPPError.Condition.registration_required,
					new ErrorSpecification(
							XMPPError.Condition.registration_required,
							XMPPError.Type.AUTH, 407));

			instances.put(XMPPError.Condition.remote_server_not_found,
					new ErrorSpecification(
							XMPPError.Condition.remote_server_not_found,
							XMPPError.Type.CANCEL, 404));

			instances.put(XMPPError.Condition.remote_server_timeout,
					new ErrorSpecification(
							XMPPError.Condition.remote_server_timeout,
							XMPPError.Type.WAIT, 504));

			instances.put(XMPPError.Condition.remote_server_error,
					new ErrorSpecification(
							XMPPError.Condition.remote_server_error,
							XMPPError.Type.CANCEL, 502));

			instances.put(XMPPError.Condition.resource_constraint,
					new ErrorSpecification(
							XMPPError.Condition.resource_constraint,
							XMPPError.Type.WAIT, 500));

			instances.put(XMPPError.Condition.service_unavailable,
					new ErrorSpecification(
							XMPPError.Condition.service_unavailable,
							XMPPError.Type.CANCEL, 503));

			instances.put(XMPPError.Condition.subscription_required,
					new ErrorSpecification(
							XMPPError.Condition.subscription_required,
							XMPPError.Type.AUTH, 407));

			instances.put(XMPPError.Condition.undefined_condition,
					new ErrorSpecification(
							XMPPError.Condition.undefined_condition,
							XMPPError.Type.WAIT, 500));

			instances.put(XMPPError.Condition.unexpected_condition,
					new ErrorSpecification(
							XMPPError.Condition.unexpected_condition,
							XMPPError.Type.WAIT, 400));

			instances.put(XMPPError.Condition.request_timeout,
					new ErrorSpecification(XMPPError.Condition.request_timeout,
							XMPPError.Type.CANCEL, 408));

			return instances;
		}

		protected static ErrorSpecification specFor(
				XMPPError.Condition condition) {
			return (ErrorSpecification) instances.get(condition);
		}

		protected XMPPError.Condition getCondition() {
			return this.condition;
		}

		protected XMPPError.Type getType() {
			return this.type;
		}

		protected int getCode() {
			return this.code;
		}
	}

	public static class Condition {
		public static final Condition interna_server_error = new Condition("internal-server-error");
		public static final Condition forbidden = new Condition("forbidden");
		public static final Condition bad_request = new Condition("bad-request");
		public static final Condition conflict = new Condition("conflict");
		public static final Condition feature_not_implemented = new Condition("feature-not-implemented");
		public static final Condition gone = new Condition("gone");
		public static final Condition item_not_found = new Condition("item-not-found");
		public static final Condition jid_malformed = new Condition("jid-malformed");
		public static final Condition no_acceptable = new Condition("not-acceptable");
		public static final Condition not_allowed = new Condition("not-allowed");
		public static final Condition not_authorized = new Condition("not-authorized");
		public static final Condition payment_required = new Condition("payment-required");
		public static final Condition recipient_unavailable = new Condition("recipient-unavailable");
		public static final Condition redirect = new Condition("redirect");
		public static final Condition registration_required = new Condition("registration-required");
		public static final Condition remote_server_error = new Condition("remote-server-error");
		public static final Condition remote_server_not_found = new Condition("remote-server-not-found");
		public static final Condition remote_server_timeout = new Condition("remote-server-timeout");
		public static final Condition resource_constraint = new Condition("resource-constraint");
		public static final Condition service_unavailable = new Condition("service-unavailable");
		public static final Condition subscription_required = new Condition("subscription-required");
		public static final Condition undefined_condition = new Condition("undefined-condition");
		public static final Condition unexpected_condition = new Condition("unexpected-condition");
		public static final Condition request_timeout = new Condition("request-timeout");
		
		private String value;
		public Condition(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}

	public static enum Type {
		WAIT, CANCEL, MODIFY, AUTH, CONTINUE;
	}
}