package com.projectboated.backend.web.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.web.exception.dto.BasicErrorResponse;
import com.projectboated.backend.web.exception.dto.BasicFieldErrorResponse;
import com.projectboated.backend.web.exception.dto.ExceptionMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BasicErrorResponse> handleBaseBusinessException(BusinessException e) {
        return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode()
                        .getStatus()))
                .body(new BasicErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessageResponse> handleHttpMessageNotReadableException() {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionMessageResponse("Message Parsing Error, please send right value"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicFieldErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                                         Locale locale) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BasicFieldErrorResponse(e.getBindingResult(), messageSource, locale));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BasicFieldErrorResponse> handleBindException(BindException e,
                                                                       Locale locale) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BasicFieldErrorResponse(e.getBindingResult(), messageSource, locale));
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<String> handleAmazonS3Exception(AmazonS3Exception e) {
        return ResponseEntity.badRequest().body("Amazon S3 Error");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerError(Exception e) throws JsonProcessingException {
        log.error("server internal error", e);

        // slack연결, 오류가 나면 알람울리게

        return ResponseEntity.badRequest()
                .body(objectMapper.writeValueAsString(new ExceptionMessageResponse("Server Internal Error")));
    }
}
