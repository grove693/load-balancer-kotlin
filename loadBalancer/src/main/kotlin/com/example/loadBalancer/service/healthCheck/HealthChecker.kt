package com.example.loadBalancer.service.healthCheck

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HealthChecker @Autowired constructor(private val httpClient: OkHttpClient) {

    private val logger = KotlinLogging.logger {}

    fun isServerHealthy(serverEndpoint: String, healthCheckPath: String): Boolean {
        val targetUrl = serverEndpoint + healthCheckPath;

        try {
            val request: Request = Request.Builder()
                    .url(targetUrl)
                    .build();

            logger.info("Performing health check on {}", targetUrl);

            this.httpClient.newCall(request).execute().use { response ->
                if (response != null) {
                    return response.isSuccessful;
                }
                return false;
            }
        } catch (ex: Exception) {
            logger.error("Encountered exception during health checking the following url {}", targetUrl, ex);

            return false;
        }
    }

}