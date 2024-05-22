package com.example.loadBalancer.service.loadBalancer

import com.example.loadBalancer.service.forwarder.HttpForwarder
import com.example.loadBalancer.service.forwarder.HttpRequestForwardingContext
import com.example.loadBalancer.service.forwarder.HttpResponseContext
import com.example.loadBalancer.service.healthCheck.HealthCheckerOrchestrator
import com.example.loadBalancer.web.http.CachedBodyHttpServletRequest
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
abstract class LoadBalancingService(
    private val httpForwarder: HttpForwarder,
    private val healthCheckerOrchestrator: HealthCheckerOrchestrator
) {
    @Value("#{'\${loadBalancing.forwardedHeaders}'.split(',')}")
    protected lateinit var headersToForward: Set<String>

    private val logger = KotlinLogging.logger {}

    private val MAX_RETRIES: Int = 3

    abstract fun getNextTargetEndpoint(): String

    fun forwardRequest(request: HttpServletRequest): HttpResponseContext {
        var retryCount: Int = 0;
        val cachedBodyHttpServletRequest = CachedBodyHttpServletRequest(request);

        while (true) {
            val targetEndpoint = getNextTargetEndpoint()
            try {
                val requestForwardingContext = HttpRequestForwardingContext(targetEndpoint, headersToForward)

                logger.info("Forwarding request to ${targetEndpoint}. Attempt: $retryCount")
                return httpForwarder.forwardRequest(cachedBodyHttpServletRequest, requestForwardingContext)
            } catch (ex: Exception) {
                logger.error("Exception encountered during forwarding request to $targetEndpoint")

                if (retryCount >= MAX_RETRIES) {
                    throw RuntimeException("Max retries reached for forwarding request");
                }

                retryCount++
                continue
            }
        }

    }

    protected fun getActiveServerEndpoints(): List<String> {
        return healthCheckerOrchestrator.getActiveServerEndpoints()
    }
}