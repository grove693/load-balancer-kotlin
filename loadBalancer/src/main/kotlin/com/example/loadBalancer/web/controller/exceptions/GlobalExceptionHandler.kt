package com.example.loadBalancer.web.controller.exceptions

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler
    fun handleExceptions(ex: Exception): ResponseEntity<Any> {
        logger.error("Exception caught by the global exception handler", ex)

        return ResponseEntity("Unexpected exception occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}