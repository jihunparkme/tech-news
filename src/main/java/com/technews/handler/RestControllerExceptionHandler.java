package com.technews.handler;

import com.technews.common.dto.BasicResponse;
import com.technews.common.exception.FirebaseQuerySnapshotException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;
import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestControllerExceptionHandler {

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            BindException.class,
            IllegalArgumentException.class,
            MissingServletRequestParameterException.class,
            AccessDeniedException.class
    })
    public ResponseEntity handleBadRequest(Exception ex) {
        return BasicResponse.clientError(ex.getMessage());
    }

    @ExceptionHandler({
            FirebaseQuerySnapshotException.class,
    })
    public ResponseEntity handleCustomNotFoundException(Exception ex) {
        return BasicResponse.internalServerError(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error("Exception. ", ex);
        return BasicResponse.internalServerError("An error occurred while processing the request. Please try again.");
    }

    @ExceptionHandler(BeanCreationNotAllowedException.class)
    public ResponseEntity handleBeanCreationNotAllowedException(Exception ex) {
        log.error("BeanCreationNotAllowedException. ", ex);
        return new ResponseEntity("BeanCreationNotAllowedException", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(Exception ex) {
        log.error("NoHandlerFoundException. ", ex);
        return BasicResponse.notFound("NoHandlerFoundException");
    }
}