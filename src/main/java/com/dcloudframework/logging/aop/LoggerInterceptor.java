package com.dcloudframework.logging.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.dcloudframework.logging.utils.LoggerUtils;

public class LoggerInterceptor implements MethodInterceptor {
	private final LoggerUtils loggerUtils;

	public LoggerInterceptor(LoggerUtils loggerUtils) {
		this.loggerUtils = loggerUtils;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return loggerUtils.info(invocation);
	}

}