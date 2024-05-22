package com.example.loadBalancer.web.http

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.util.StreamUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class CachedBodyHttpServletRequest(request: HttpServletRequest?) : HttpServletRequestWrapper(request) {

    private val cachedBody: ByteArray

    init {
        val requestInputStream = request?.inputStream
        cachedBody = StreamUtils.copyToByteArray(requestInputStream)
    }

    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(cachedBody);
    }

    override fun getReader(): BufferedReader {
        val byteArrayInputStream = ByteArrayInputStream(this.cachedBody);

        return BufferedReader(InputStreamReader(byteArrayInputStream));
    }
}