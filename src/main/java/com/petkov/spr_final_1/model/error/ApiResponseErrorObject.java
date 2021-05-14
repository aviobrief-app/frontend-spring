package com.petkov.spr_final_1.model.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiResponseErrorObject {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
  //  private String debugMessage;
    private List<ApiBindingError> errors;

    private ApiResponseErrorObject() {
        timestamp = LocalDateTime.now();
    }

    public ApiResponseErrorObject(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiResponseErrorObject(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
  //      this.debugMessage = ex.getMessage();
    }

    public ApiResponseErrorObject(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
  //      this.debugMessage = ex.getMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiBindingError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiBindingError> errors) {
        this.errors = errors;
    }
}