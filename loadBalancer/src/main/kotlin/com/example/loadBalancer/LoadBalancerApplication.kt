package com.example.loadBalancer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoadBalancerApplication

fun main(args: Array<String>) {
    runApplication<LoadBalancerApplication>(*args)
}
