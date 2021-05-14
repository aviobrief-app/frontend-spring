package com.petkov.spr_final_1.exception;

import com.petkov.spr_final_1.model.error.ApiResponseErrorObject;
import com.petkov.spr_final_1.model.error.ApiBindingError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = RestController.class)
public class RestControllersGlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {

        List<ApiBindingError> apiBindingError = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> new ApiBindingError(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .distinct()
                .collect(Collectors.toList());

        ApiResponseErrorObject apiResponseErrorObject = new ApiResponseErrorObject(status, "Binding Validation Error", exception);
        apiResponseErrorObject.setErrors(apiBindingError);

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(apiResponseErrorObject);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ApiResponseErrorObject apiResponseErrorObject = new ApiResponseErrorObject(status, "Malformed JSON request", exception);

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(apiResponseErrorObject);

    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());

    }


}
