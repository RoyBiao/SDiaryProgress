package com.loovee.common.xmpp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface XMLElement {
	/**
	 * 节点名称,默认为类名
	 * @return
	 */
	public String value() default "";
}
