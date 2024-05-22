package com.example.loadBalancer.service.forwarder

import mu.KotlinLogging
import okhttp3.Response

class HttpResponseContext private constructor(
        val body: ByteArray,
        val statusCode: Int,
        val protocol: String,
        val originEndpoint: String
) {
    companion object {
        private val LOGGER = KotlinLogging.logger {}

        fun from(httpResponse: Response?, originEndpoint: String): HttpResponseContext {
            httpResponse ?: throw IllegalStateException("Http Response should not be null")

            return try {
                val responseBody = httpResponse.body
                val statusCode = httpResponse.code

                val responseBodyBytes = responseBody?.bytes() ?: ByteArray(0)

                HttpResponseContext(responseBodyBytes, statusCode, httpResponse.protocol.name, originEndpoint)
            } catch (ex: Exception) {
                LOGGER.error("Exception during http response parsing", ex)

                throw RuntimeException("Exception during http response parsing", ex)
            }
        }
    }
}
