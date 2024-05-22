package com.example.loadBalancer.config

import com.example.loadBalancer.web.interceptors.logging.RequestLoggingInterceptor
import com.example.loadBalancer.web.interceptors.logging.RequestTraceIdGeneratorInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig @Autowired constructor(private val requestLoggingInterceptor: RequestLoggingInterceptor, private val requestTraceIdGeneratorInterceptor: RequestTraceIdGeneratorInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestTraceIdGeneratorInterceptor)
        registry.addInterceptor(requestLoggingInterceptor);
    }
}