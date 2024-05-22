package com.example.loadBalancer.service.loadBalancer

import com.example.loadBalancer.service.forwarder.HttpForwarder
import com.example.loadBalancer.service.healthCheck.HealthCheckerOrchestrator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service("loadBalancingRandom")
class LoadBalancingServiceRandom @Autowired constructor(private val httpForwarder: HttpForwarder, private val healthCheckerOrchestrator: HealthCheckerOrchestrator) : LoadBalancingService(httpForwarder, healthCheckerOrchestrator) {

    private val randomNumberGenerator: Random = Random.Default;

    override fun getNextTargetEndpoint(): String {
        val randomIndex = randomNumberGenerator.nextInt(getActiveServerEndpoints().size);

        return getActiveServerEndpoints()[randomIndex];
    }

}