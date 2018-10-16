package com.dcloudframework.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Repeatable(value = Loggers.class)
public @interface Logger {

	String[] value() default {};

	LoggerType type() default LoggerType.AFTER;

	public enum LoggerType {
		BEFORE, AFTER
	}

}