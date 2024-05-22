package com.example.loadBalancer.service.healthCheck

import com.example.loadBalancer.service.healthCheck.scheduling.repo.HealthCheckJobRepository
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.impl.StdSchedulerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArrayList

@Service
class HealthCheckerOrchestrator @Autowired constructor(private val jobRepository: HealthCheckJobRepository) {

    private val logger = KotlinLogging.logger {}

    private lateinit var taskScheduler: Scheduler

    private lateinit var activeServerEndpoints: MutableList<String>

    @PostConstruct
    private fun init() {
        try {
            this.activeServerEndpoints = CopyOnWriteArrayList()
            startHealthCheckTasks()
        } catch (ex: Exception) {
            logger.error("Error during health checker initialization", ex)

            throw RuntimeException("Error during health checker initialization", ex)
        }
    }

    private fun startHealthCheckTasks() {
        val schedulerFactory: SchedulerFactory = StdSchedulerFactory();
        this.taskScheduler = schedulerFactory.scheduler;
        this.taskScheduler.start();

        val jobDetails: List<JobDetail>? = jobRepository.getJobDetails();

        for (job in jobDetails!!) {
            job.jobDataMap[HealthCheckJobRepository.ACTIVE_ENDPOINTS_KEY] = activeServerEndpoints
            taskScheduler.scheduleJob(job, jobRepository.buildTriggerForJobDetails(job))
        }
    }

    fun getActiveServerEndpoints(): MutableList<String> {
        return activeServerEndpoints;
    }

}