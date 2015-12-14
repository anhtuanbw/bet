// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.web.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.DataNotFoundException;
import vn.kms.ngaythobet.domain.util.NgaythobetException;

import java.util.List;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestController
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorInfo handleConcurencyError(ConcurrencyFailureException ex) {
        return new ErrorInfo(ErrorType.CONCURRENCY_FAILURE, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleBadInput(HttpMessageNotReadableException ex) {
        return new ErrorInfo(ErrorType.BAD_INPUT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return handleFieldErrors(fieldErrors);
    }

    @ExceptionHandler(DataInvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleDataInvalidError(DataInvalidException ex) {
        return new ErrorInfo(ErrorType.VALIDATION_FAILURE, getLocalizedMessage(ex));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo handleDataNotFoundError(DataNotFoundException ex) {
        return new ErrorInfo(ErrorType.DATA_NOT_FOUND, getLocalizedMessage(ex));
    }

    @ExceptionHandler(NgaythobetException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo handleDataNotFoundError(NgaythobetException ex) {
        logger.error("Error!", ex);
        return new ErrorInfo(ErrorType.SERVER_ERROR, getLocalizedMessage(ex));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo handleGlobalError(Exception ex) {
        logger.error("Error!", ex);
        return new ErrorInfo(ErrorType.SERVER_ERROR, ex.getMessage());
    }

    private ErrorInfo handleFieldErrors(List<FieldError> fieldErrors) {
        ErrorInfo error = new ErrorInfo(ErrorType.VALIDATION_FAILURE);

        for (FieldError fieldError : fieldErrors) {
            String message = messageSource.getMessage(fieldError, getLocale());
            error.add(fieldError.getField(), message);
        }

        return error;
    }

    private String getLocalizedMessage(NgaythobetException ex) {
        return messageSource.getMessage(ex.getMessageKey(), ex.getMessageArgs(), getLocale());
    }
}
