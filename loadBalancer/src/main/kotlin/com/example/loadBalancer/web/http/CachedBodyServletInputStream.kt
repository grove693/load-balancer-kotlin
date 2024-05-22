package com.example.loadBalancer.web.http

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream

class CachedBodyServletInputStream(private val cacheBody: ByteArray): ServletInputStream() {

    private val cachedBodyInputStream: InputStream = ByteArrayInputStream(this.cacheBody)

    override fun read(): Int {
       return cachedBodyInputStream.read();
    }

    override fun isFinished(): Boolean {
        return cachedBodyInputStream.available() == 0;
    }

    override fun isReady(): Boolean {
        return true;
    }

    override fun setReadListener(listener: ReadListener?) {
        TODO("Not yet implemented")
    }
}