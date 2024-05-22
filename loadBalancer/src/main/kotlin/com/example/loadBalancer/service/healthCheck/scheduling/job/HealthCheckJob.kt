package com.example.loadBalancer.service.healthCheck.scheduling.job

import com.example.loadBalancer.service.healthCheck.HealthChecker
import com.example.loadBalancer.service.healthCheck.scheduling.repo.HealthCheckJobRepository.JobAttributes.ACTIVE_ENDPOINTS_KEY
import com.example.loadBalancer.service.healthCheck.scheduling.repo.HealthCheckJobRepository.JobAttributes.HEALTH_CHECKER_KEY
import com.example.loadBalancer.service.healthCheck.scheduling.repo.HealthCheckJobRepository.JobAttributes.HEALTH_CHECK_PATH_KEY
import com.example.loadBalancer.service.healthCheck.scheduling.repo.HealthCheckJobRepository.JobAttributes.TARGET_ENDPOINT_KEY
import mu.KotlinLogging
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext

class HealthCheckJob : Job {

    private val logger = KotlinLogging.logger {}

    override fun execute(jobExecutionContext: JobExecutionContext?) {
        val dataMap: JobDataMap? = jobExecutionContext?.jobDetail?.jobDataMap
        val jobName: String? = jobExecutionContext?.jobDetail?.key?.name

        val targetEndpoint = dataMap!!.getString(TARGET_ENDPOINT_KEY)
        val healthCheckPath = dataMap!!.getString(HEALTH_CHECK_PATH_KEY)
        val healthChecker: HealthChecker? = dataMap[HEALTH_CHECKER_KEY] as HealthChecker?
        val activeEndpoints = dataMap[ACTIVE_ENDPOINTS_KEY] as MutableList<String>?

        if (healthChecker!!.isServerHealthy(targetEndpoint, healthCheckPath)) {
            if (!activeEndpoints!!.contains(targetEndpoint)) {
                logger.info("{} Health check passed. Adding {} as active endpoint", jobName, targetEndpoint)

                activeEndpoints.add(targetEndpoint)
            }
        } else {
            logger.info("{} Health check FAILED. Removing {} from active endpoints", jobName, targetEndpoint)
            activeEndpoints!!.remove(targetEndpoint)
        }
    }

}