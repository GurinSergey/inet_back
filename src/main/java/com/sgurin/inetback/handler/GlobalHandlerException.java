package com.sgurin.inetback.handler;

import com.google.common.base.Throwables;
import com.sgurin.inetback.exeption.*;
import com.sgurin.inetback.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity requestHandlingNoHandlerFound(final NoHandlerFoundException ex) {
        return GenericResponse.errorHttp("Not found resource!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity handlerUnauthorizedException(final UnauthorizedException ex) {
        log.error(ex.getLocalizedMessage());
        return GenericResponse.errorHttp(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(AuthenticationException.class)
//    @ResponseStatus(value = HttpStatus.FORBIDDEN)
//    @ResponseBody
//    public ResponseEntity handlerAuthenticationException(final AuthenticationException ex) {
//        log.error(ex.getLocalizedMessage());
//        return GenericResponse.errorHttp(ex.getMessage(), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public ResponseEntity handlerRuntimeException(final Exception ex) {
        ex.printStackTrace();
        log.error(ex.getLocalizedMessage());
        return GenericResponse.errorHttp(ex.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public ResponseEntity requestHandlingNotFound(final NotFoundException ex) {
        return GenericResponse.errorHttp(ex.getLocalizedMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({CustomWarningException.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public ResponseEntity requestHandlingNotFound(final CustomWarningException ex) {
        return GenericResponse.errorHttp(ex.getLocalizedMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({ValidateException.class})
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public ResponseEntity requestHandlingValidate(final ValidateException ex) {
        return GenericResponse.errorHttp(ex.getLocalizedMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error(e.getLocalizedMessage());
        String exText = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessageTemplate)
                .collect(Collectors.joining("; "));

        return GenericResponse.errorHttp(exText, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity customHandlePermission(Exception ex, WebRequest request) {

        return GenericResponse.warning(ex.getLocalizedMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity handlerNullPointerException(NullPointerException ex) {
        String error = Objects.isNull(ex.getLocalizedMessage()) ? "Please try later!" : ex.getLocalizedMessage();
        return GenericResponse.errorHttp(error, HttpStatus.I_AM_A_TEAPOT);
    }
}
