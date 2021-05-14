package com.petkov.spr_final_1.model.error;

public class ApiBindingError {

    private String fieldName;
    private Object rejectedValue;
    private String messageError;

    public ApiBindingError() {
    }

    public ApiBindingError(String fieldName, Object rejectedValue, String messageError) {
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.messageError = messageError;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
