package com.dcloudframework.logging.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dcloudframework.logging.aop.LoggerAnnotationAdvisor;
import com.dcloudframework.logging.aop.LoggerInterceptor;

@Configuration
public class LoggerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public LoggerInterceptor loggerInterceptor() {
		return new LoggerInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean
	public LoggerAnnotationAdvisor loggerAnnotationAdvisor(LoggerInterceptor loggerInterceptor) {
		return new LoggerAnnotationAdvisor(loggerInterceptor);
	}

}