package com.example.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import com.example.exception.NoDataException;

@ControllerAdvice
public class RestErrorHandler {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);
 
    @ExceptionHandler(NoDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleTodoNotFoundException(NoDataException ex) {
        LOGGER.error("Handling No data error");
    }
}