package com.dcloudframework.logging.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.dcloudframework.logging.utils.LoggerUtils;

public class LoggerInterceptor implements MethodInterceptor {

	public LoggerInterceptor() {
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		return LoggerUtils.info(invocation);
	}

}