package com.example.loadBalancer.web.interceptors.logging

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RequestLoggingInterceptor : HandlerInterceptor {

    private val logger = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info("Starting Request Logging Interceptor...")

        logRequest(HttpRequestLoggingContext(request));

        return true;
    }

    private fun logRequest(requestCtx: HttpRequestLoggingContext) {
        logger.info("Request received from ${requestCtx.clientIpAddress}");
        logger.info("${requestCtx.httpMethod} ${requestCtx.path} ${requestCtx.protocol}");
        logger.info("Host ${requestCtx.host}");
        logger.info("User-Agent ${requestCtx.userAgent}");
    }
}