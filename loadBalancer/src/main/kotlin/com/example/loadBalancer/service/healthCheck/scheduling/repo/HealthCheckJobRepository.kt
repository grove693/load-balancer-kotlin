package com.example.loadBalancer.service.healthCheck.scheduling.repo

import com.example.loadBalancer.service.healthCheck.HealthChecker
import com.example.loadBalancer.service.healthCheck.scheduling.job.HealthCheckJob
import jakarta.annotation.PostConstruct
import org.quartz.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Map

@Service
class HealthCheckJobRepository(
        @Value("#{'\${loadBalancing.endpoints}'.split(',')}")
        private var serverEndpoints: List<String>,
        @Value("\${loadBalancing.healthCheck.interval.seconds}")
        private val healthCheckInterval: Int,
        @Value("\${loadBalancing.healthCheck.path}")
        private val healthCheckPath: String,
        @Autowired private var healthChecker: HealthChecker
) {

    companion object JobAttributes {

        var TARGET_ENDPOINT_KEY: String = "targetEndpoint";
        var HEALTH_CHECKER_KEY: String = "healthChecker";
        var ACTIVE_ENDPOINTS_KEY: String = "activeEndpoints";
        var HEALTH_CHECK_PATH_KEY: String = "healthCheckPath";
    }

    private var jobDetails: List<JobDetail>? = null;


    @PostConstruct
    private fun createJobDetails() {
        this.jobDetails = serverEndpoints.stream().map<JobDetail> { endpoint: String ->
            val jobData = Map.of<String?, Any?>(
                    TARGET_ENDPOINT_KEY, endpoint,
                    HEALTH_CHECKER_KEY, this.healthChecker,
                    HEALTH_CHECK_PATH_KEY, healthCheckPath
            )
            JobBuilder.newJob(HealthCheckJob::class.java)
                    .withIdentity("health check $endpoint", "healthChecking")
                    .usingJobData(JobDataMap(jobData))
                    .build()
        }
                .toList()
    }

    fun buildTriggerForJobDetails(jobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
                .withIdentity("healthCheckTrigger" + jobDetail.key.name, "healthChecking")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(healthCheckInterval)
                        .repeatForever())
                .build()
    }

    fun getJobDetails(): List<JobDetail>? {
        return this.jobDetails;
    }

}