package com.example.projectcompass.web.common.exception;

import com.example.projectcompass.domain.common.exception.BaseBusinessException;
import com.example.projectcompass.domain.common.exception.ErrorCode;
import com.example.projectcompass.web.common.exception.dto.BasicErrorResponse;
import com.example.projectcompass.web.common.exception.dto.BasicFieldErrorResponse;
import com.example.projectcompass.web.common.exception.dto.ExceptionMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<BasicErrorResponse> handleBaseBusinessException(BaseBusinessException e) {
        BasicErrorResponse errorResponse = new BasicErrorResponse(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(e.getErrorCode().getStatus())).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionMessageResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) throws JsonProcessingException {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionMessageResponse("Message Parsing Error, please send right value"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasicFieldErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) throws JsonProcessingException {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        new BasicFieldErrorResponse(
                                e.getBindingResult(),
                                messageSource,
                                locale
                        )
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerError(Exception e) throws JsonProcessingException {
        log.error("server internal error", e);

        return ResponseEntity
                .badRequest()
                .body(objectMapper.writeValueAsString(
                                new ExceptionMessageResponse("Server Internal Error")
                        )
                );
    }
}
