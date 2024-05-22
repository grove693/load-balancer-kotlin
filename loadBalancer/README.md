# Load balancer Application

Description
----------------------

Load balancer application which also performs periodical health checks to dynamically manage the backend server endpoints so that the requests are always routed to an active backend server.


Inspired by the following code challenge: https://codingchallenges.fyi/challenges/challenge-load-balancer



Tech Stack
-----------------------
Kotlin 1.9

Spring Boot

Quartz library for task scheduling

OkHTTP Http Client


Usage
-----------------------

This application is configurable from the <b>application.properties</b> file.

Relevant properties:

    - loadBalancing.endpoints
        - endpoints to which the requests are load balanced
    - loadBalancing.forwardedHeaders
        - HTTP headers to be forwarded along with the request
    - loadBalancing.healthCheck.interval.seconds
        - Health check interval
    - loadBalancing.healthCheck.path
        - Health check path

TODOs
------------------------

- improved logging
- improved concurrency