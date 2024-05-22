package com.example.loadBalancer.service.forwarder

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import kotlin.math.log

@Service
class HttpForwarder @Autowired constructor(httpClient: OkHttpClient) {

    private val logger = KotlinLogging.logger {}

    private val httpClient = httpClient;

    fun forwardRequest(request: HttpServletRequest, context: HttpRequestForwardingContext): HttpResponseContext {
        try {
            val requestToBeForwarded = buildRequestToBeForwarded(request, context)
            val targetUrl = requestToBeForwarded.url.toUrl().toString()
            logger.info("Forwarding request to {} ...", targetUrl)

            httpClient.newCall(requestToBeForwarded).execute().use { response ->
                return HttpResponseContext.from(response, targetUrl)
            }
        } catch (ex: Exception) {
            logger.error("Error during request forwarding", ex)

            throw RuntimeException("Exception during request forwarding", ex)
        }
    }

    private fun buildRequestToBeForwarded(originalRequest: HttpServletRequest, context: HttpRequestForwardingContext): Request {
        try {
            originalRequest.inputStream.use { inputStream ->
                val targetUrl: String = buildTargetUrl(originalRequest, context)
                val requestBuilder: Request.Builder = Request.Builder()
                        .url(targetUrl)

                addRequestHeaders(requestBuilder, originalRequest, context)

                val requestBodyBytes = inputStream.readAllBytes()
                val reqBody: RequestBody = requestBodyBytes.toRequestBody();

                if (HttpMethod.POST.name().equals(originalRequest.method, ignoreCase = true)) {
                    requestBuilder.post(reqBody)
                }

                if (HttpMethod.PUT.name().equals(originalRequest.method, ignoreCase = true)) {
                    requestBuilder.put(reqBody)
                }

                if (HttpMethod.PATCH.name().equals(originalRequest.method, ignoreCase = true)) {
                    requestBuilder.patch(reqBody)
                }

                if (HttpMethod.DELETE.name().equals(originalRequest.method, ignoreCase = true)) {
                    if (requestBodyBytes.isNotEmpty()) {
                        requestBuilder.delete(reqBody)
                    } else {
                        requestBuilder.delete()
                    }
                }
                return requestBuilder.build()
            }
        } catch (ex: Exception) {
            logger.error("Error during building the request to be forwarded...", ex)

            throw RuntimeException("Exception during request building....", ex)
        }
    }

    private fun buildTargetUrl(request: HttpServletRequest, context: HttpRequestForwardingContext): String {
        var targetUrl: String? = context.endpoint

        if (StringUtils.hasText(request.servletPath)) {
            targetUrl += request.servletPath
        }

        if (StringUtils.hasText(request.queryString)) {
            targetUrl += "?" + request.queryString
        }

        return targetUrl!!
    }

    private fun addRequestHeaders(reqBuilder: Request.Builder, req: HttpServletRequest, context: HttpRequestForwardingContext) {
        for (header in context.headersToBeForwarded) {
            if (StringUtils.hasText(req.getHeader(header))) {
                reqBuilder.addHeader(header, req.getHeader(header))
            }
        }
    }

}