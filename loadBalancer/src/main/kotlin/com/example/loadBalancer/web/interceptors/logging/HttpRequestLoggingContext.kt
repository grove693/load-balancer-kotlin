package com.example.loadBalancer.web.interceptors.logging

import jakarta.servlet.http.HttpServletRequest

class HttpRequestLoggingContext(request: HttpServletRequest) {
    val clientIpAddress = request.remoteAddr
    val userAgent = request.getHeader("User-Agent")
    val host = request.getHeader("Host")
    val httpMethod = request.method
    val protocol = request.protocol
    val path = request.servletPath

}