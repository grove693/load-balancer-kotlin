package com.example.loadBalancer.service.loadBalancer

import com.example.loadBalancer.service.forwarder.HttpForwarder
import com.example.loadBalancer.service.healthCheck.HealthCheckerOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("loadBalancingRoundRobin")
class LoadBalancingServiceRoundRobin @Autowired constructor(private val httpForwarder: HttpForwarder, private val healthCheckerOrchestrator: HealthCheckerOrchestrator) : LoadBalancingService(httpForwarder, healthCheckerOrchestrator) {

    private var currentEndpointIndex: Int = 0;

    @Synchronized
    override fun getNextTargetEndpoint(): String {
        val activeServers = getActiveServerEndpoints();
        if (activeServers.isEmpty()) {
            throw RuntimeException("No active servers found");
        }

        if (currentEndpointIndex >= activeServers.size) {
            currentEndpointIndex = activeServers.size - 1;
        }
        val targetEndpoint = activeServers[currentEndpointIndex]
        currentEndpointIndex = (currentEndpointIndex + 1) % activeServers.size;

        return targetEndpoint;
    }

}