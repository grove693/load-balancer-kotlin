package com.example.loadBalancer.web.interceptors.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.lang.Exception
import java.util.UUID

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class RequestTraceIdGeneratorInterceptor: HandlerInterceptor {


    private val requestTraceId: ThreadLocal<String> = ThreadLocal()
    private val logger = KotlinLogging.logger {}

    companion object {
        var REQUEST_TRACE_ID_LOG_KEY: String = "reqTraceId";
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        logger.info("Starting Request Trace Id generator Interceptor...")
        val traceId: String = UUID.randomUUID().toString()
        logger.info("Generated trace id $traceId")
        requestTraceId.set(traceId);
        MDC.put(REQUEST_TRACE_ID_LOG_KEY, requestTraceId.get());

        return super.preHandle(request, response, handler)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        requestTraceId.remove();
        super.afterCompletion(request, response, handler, ex)
    }
}