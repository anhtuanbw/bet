// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ErrorType type;
    private final String message;
    private final Map<String, String> fieldErrors;

    public ErrorInfo(ErrorType type) {
        this(type, null);
    }

    public ErrorInfo(ErrorType type, String message) {
        this(type, message, null);
    }

    public ErrorInfo(ErrorType type, String message, Map<String, String> fieldErrors) {
        this.type = type;
        this.message = message;
        this.fieldErrors = (fieldErrors != null)? fieldErrors : new HashMap<>();
    }

    public void add(String field, String message) {
        fieldErrors.put(field, message);
    }

    public ErrorType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
