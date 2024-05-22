package com.example.loadBalancer.web.controller

import com.example.loadBalancer.service.forwarder.HttpResponseContext
import com.example.loadBalancer.service.loadBalancer.LoadBalancingService
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class LoadBalancingController @Autowired constructor(@Qualifier("loadBalancingRoundRobin") private val loadBalancingService: LoadBalancingService) {

    private val logger = KotlinLogging.logger {}

    @RequestMapping(path = ["/**"], method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH])
    fun handleRequest(httpReq: HttpServletRequest): ResponseEntity<Any> {
        val responseContext: HttpResponseContext = loadBalancingService.forwardRequest(httpReq)
        logger.info("Response from {}  {} {}", responseContext.originEndpoint, responseContext.protocol, responseContext.statusCode)

        return ResponseEntity.status(responseContext.statusCode).body(responseContext.body)
    }

}