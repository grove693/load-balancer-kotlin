package com.example.loadBalancer.service.forwarder

data class HttpRequestForwardingContext(val endpoint: String) {

    val headersToBeForwarded: MutableSet<String> = mutableSetOf("Accept", "Content-Type");

    constructor(endpoint: String, headersToForward: Set<String>) : this(endpoint) {
        if (headersToForward.isNotEmpty()) {
            this.headersToBeForwarded.addAll(headersToForward);
        }
    }
}
