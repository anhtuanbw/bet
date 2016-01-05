package vn.kms.ngaythobet.web.error;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import vn.kms.ngaythobet.domain.util.DataInvalidException;
import vn.kms.ngaythobet.domain.util.DataNotFoundException;
import vn.kms.ngaythobet.domain.util.NgaythobetException;
import vn.kms.ngaythobet.infras.security.xauth.TokenProvider;

@RestController
@ControllerAdvice(annotations = RestController.class)
public class WebsocketExceptionHandler {

    private final MessageSource messageSource;
    private final TokenProvider tokenProvider;
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebsocketExceptionHandler(MessageSource messageSource, TokenProvider tokenProvider,
            SimpMessageSendingOperations messagingTemplate) {
        this.messageSource = messageSource;
        this.tokenProvider = tokenProvider;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageExceptionHandler(NgaythobetException.class)
    public void handleNgaythobetException(NgaythobetException exception, SimpMessageHeaderAccessor headerAccessor) {
        String message = getLocalizedMessage(exception);
        sendMessageToUser(headerAccessor, message);
    }

    @MessageExceptionHandler(DataNotFoundException.class)
    public void handleDataNotFoundException(DataNotFoundException exception, SimpMessageHeaderAccessor headerAccessor) {
        String message = getLocalizedMessage(exception);
        sendMessageToUser(headerAccessor, message);
    }

    @MessageExceptionHandler
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, SimpMessageHeaderAccessor headerAccessor) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorInfo errorInfo = handleFieldErrors(fieldErrors);
        sendMessageToUser(headerAccessor, errorInfo);
    }

    @MessageExceptionHandler(DataInvalidException.class)
    public void handleDataInvalidException(DataInvalidException exception, SimpMessageHeaderAccessor headerAccessor) {
        String message = getLocalizedMessage(exception);
        sendMessageToUser(headerAccessor, message);
    }

    private void sendMessageToUser(SimpMessageHeaderAccessor headerAccessor, Object message) {
        tokenProvider.setAuthenticationFromHeader(headerAccessor);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (username != null) {
            messagingTemplate.convertAndSend("/topic/errors/" + username, message);
        }
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
