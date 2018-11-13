package com.dcloudframework.logging.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dcloudframework.logging.aop.LoggerAnnotationAdvisor;
import com.dcloudframework.logging.aop.LoggerInterceptor;
import com.dcloudframework.logging.utils.LoggerUtils;

@Configuration
public class LoggerAutoConfiguration {

	@Bean
	public LoggerUtils loggerUtils() {
		return new LoggerUtils();
	}

	@Bean
	@ConditionalOnMissingBean
	public LoggerInterceptor loggerInterceptor(LoggerUtils loggerUtils) {
		return new LoggerInterceptor(loggerUtils);
	}

	@Bean
	@ConditionalOnMissingBean
	public LoggerAnnotationAdvisor loggerAnnotationAdvisor(LoggerInterceptor loggerInterceptor) {
		return new LoggerAnnotationAdvisor(loggerInterceptor);
	}

}