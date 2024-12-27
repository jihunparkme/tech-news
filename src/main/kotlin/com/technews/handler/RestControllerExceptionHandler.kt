package com.technews.handler

import com.technews.common.constant.Result
import com.technews.common.dto.BasicResponse
import mu.KotlinLogging
import org.springframework.beans.factory.BeanCreationNotAllowedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import java.net.BindException
import java.nio.file.AccessDeniedException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice(annotations = [RestController::class])
class RestControllerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<*> {
        val errorMessage = ex.bindingResult.allErrors.firstOrNull()?.defaultMessage ?: Result.FAIL.message
        return BasicResponse.clientError(errorMessage)
    }

    @ExceptionHandler(
        HttpMessageNotReadableException::class,
        MethodArgumentTypeMismatchException::class,
        BindException::class,
        IllegalArgumentException::class,
        MissingServletRequestParameterException::class,
        AccessDeniedException::class,
    )
    fun handleBadRequest(ex: Exception): ResponseEntity<*> =
        BasicResponse.clientError(ex.message.orEmpty())

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<*> {
        logger.error("Exception. ", ex)
        return BasicResponse.internalServerError("An error occurred while processing the request. Please try again.")
    }

    @ExceptionHandler(BeanCreationNotAllowedException::class)
    fun handleBeanCreationNotAllowedException(ex: Exception): ResponseEntity<*> {
        logger.error("BeanCreationNotAllowedException. ", ex)
        return ResponseEntity<Any>("BeanCreationNotAllowedException", HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(ex: Exception): ResponseEntity<*> {
        logger.error("NoHandlerFoundException.", ex)
        return BasicResponse.notFound("NoHandlerFoundException")
    }
}
