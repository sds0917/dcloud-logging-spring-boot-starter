package com.dcloudframework.logging.aop;

import java.lang.reflect.Method;

public class LoggerExpressionCacheKey {

	private String el;
	private Method method;

	public LoggerExpressionCacheKey(String el, Method method) {
		this.el = el;
		this.method = method;
	}

	public String getEl() {
		return el;
	}

	public void setEl(String el) {
		this.el = el;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((el == null) ? 0 : el.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoggerExpressionCacheKey other = (LoggerExpressionCacheKey) obj;
		if (el == null) {
			if (other.el != null)
				return false;
		} else if (!el.equals(other.el))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoggerExpressionCacheKey [el=" + el + ", method=" + method + "]";
	}

}