package com.example.loadBalancer.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class BeanConfigs {

    @Bean
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.MILLISECONDS)
                .build()
    }
}