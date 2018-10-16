package com.dcloudframework.logging.aop;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.dcloudframework.logging.annotation.Logger;
import com.dcloudframework.logging.annotation.Loggers;

public class LoggerAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

	private static final long serialVersionUID = -2268540020710520458L;
	private Advice advice;
	private Pointcut pointcut;

	public LoggerAnnotationAdvisor(LoggerInterceptor loggerInterceptor) {
		this.advice = loggerInterceptor;
		Set<Class<? extends Annotation>> loggerAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>(2);
		loggerAnnotationTypes.add(Loggers.class);
		loggerAnnotationTypes.add(Logger.class);
		this.pointcut = buildPointcut(loggerAnnotationTypes);
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.advice instanceof BeanFactoryAware) {
			((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
		}
	}

	private Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes) {
		ComposablePointcut result = null;
		for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes) {
			Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
			Pointcut mpc = new AnnotationMatchingPointcut(null, asyncAnnotationType, true);
			if (result == null) {
				result = new ComposablePointcut(cpc);
			} else {
				result.union(cpc);
			}
			result = result.union(mpc);
		}
		return (result != null ? result : Pointcut.TRUE);
	}

}