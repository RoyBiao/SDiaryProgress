package com.loovee.common.xmpp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XMLAttr {

	/**
	 * 默认为成员变量名称
	 * @return
	 */
	String value() default "";
}
