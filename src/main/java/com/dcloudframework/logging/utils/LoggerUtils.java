package com.dcloudframework.logging.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import com.dcloudframework.logging.annotation.Logger;
import com.dcloudframework.logging.annotation.Loggers;
import com.dcloudframework.logging.aop.LoggerExpressionCacheKey;

public class LoggerUtils implements EmbeddedValueResolverAware {

	private EmbeddedValueResolver resolver;
	private static final ExpressionParser PARSER = new SpelExpressionParser();
	private static final Log LOGGER = LogFactory.getLog(SpringApplication.class);
	private static final Map<String, Log> LOGGER_CACHE = new ConcurrentHashMap<>();
	private static final Map<LoggerExpressionCacheKey, Expression> CACHE = new ConcurrentHashMap<>();
	private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

	public Object info(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		String className = method.getDeclaringClass().getName();
		Log log = getLogger(className);
		if (!log.isInfoEnabled()) {
			return invocation.proceed();
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = invocation.proceed();
		stopWatch.stop();
		// log.info(getMessage(method, className, stopWatch));
		for (Logger logger : getAnnotation(method)) {
			log.info(formatLogger(method, invocation.getArguments(), logger, result));
		}
		return result;
	}

	private String formatLogger(Method method, Object[] arguments, Logger logger, Object result) {
		StringBuilder sb = new StringBuilder();
		if (null == logger) {
			return sb.toString();
		}
		EvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments, NAME_DISCOVERER);
		context.setVariable(method.getName(), result);
		sb.append(parseSpel(logger.value(), method, context));
		return sb.toString();
	}

	private String parseSpel(String[] els, Method method, EvaluationContext context) {
		List<String> values = new ArrayList<String>(els.length);
		for (String el : els) {
			if (StringUtils.isEmpty(el)) {
				continue;
			}
			LoggerExpressionCacheKey cacheKey = new LoggerExpressionCacheKey(el, method);
			Expression expression = CACHE.get(cacheKey);
			if (null == expression) {
				CACHE.put(cacheKey, expression = PARSER.parseExpression(resolver.resolveStringValue(el)));
			}
			values.add(String.valueOf(expression.getValue(context)));
		}
		return StringUtils.collectionToDelimitedString(values, "      ", "", "");
	}

	private Log getLogger(String name) {
		if (StringUtils.isEmpty(name)) {
			return LOGGER;
		}
		Log log = LOGGER_CACHE.get(name);
		if (null == log) {
			LOGGER_CACHE.put(name, log = LogFactory.getLog(name));
		}
		return log;
	}

	private Set<Logger> getAnnotation(Method method) {
		Set<Logger> logs = new HashSet<Logger>();
		Loggers loggers = method.getAnnotation(Loggers.class);
		if (null != loggers) {
			logs.addAll(Arrays.asList(loggers.value()));
		}
		Logger logger = method.getAnnotation(Logger.class);
		if (null != logger) {
			logs.add(logger);
		}
		return logs;
	}

	/*
	 * private String argumentsToString(Class<?>[] classes) { if (null == classes ||
	 * classes.length == 0) { return ""; } String clas[] = new
	 * String[classes.length]; for (int i = 0; i < classes.length; i++) { clas[i] =
	 * classes[i].getName(); } return Arrays.toString(clas).replace("[",
	 * "(").replace("]", ")"); }
	 * 
	 * private StringBuilder getMessage(Method method, String className, StopWatch
	 * stopWatch) { return new
	 * StringBuilder(className).append(".").append(method.getName()).append(
	 * argumentsToString(method.getParameterTypes())).append("方法执行时长").append(
	 * stopWatch.getTotalTimeSeconds()).append("秒"); }
	 */

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.resolver = (EmbeddedValueResolver) resolver;
	}

}