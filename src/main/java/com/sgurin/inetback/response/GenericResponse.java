package com.sgurin.inetback.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class GenericResponse<T> implements Serializable {
    private String result;
    private T payload;
    private String securityToken;

    public GenericResponse() {
    }

    public GenericResponse(String result, T payload, String securityToken) {
        this.result = result;
        this.payload = payload;
        this.securityToken = securityToken;
    }

    public static <T> GenericResponse<T> ok(T payload) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setPayload(payload);
        response.setResult(HttpStatus.OK.name());
        return response;
    }

    public static <T> GenericResponse<T> ok(T payload, String securityToken) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setPayload(payload);
        response.setResult(HttpStatus.OK.name());
        response.setSecurityToken(securityToken);
        return response;
    }

    public static <T> ResponseEntity<GenericResponse<T>> success(T message) {
        return new ResponseEntity<>(GenericResponse.ok(message),
                HttpStatus.OK);
    }

    public static <T> ResponseEntity<GenericResponse<T>> successWithToken(T message, String securityToken) {
        return new ResponseEntity<>(GenericResponse.ok(message, securityToken),
                HttpStatus.OK);

    }
    public static <T> ResponseEntity<GenericResponse<T>> serverError(T errorMessage) {
        return errorHttp(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T>  ResponseEntity<GenericResponse<T>> unauthorized(T errorMessage) {
        return errorHttp(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    public static <T> ResponseEntity<GenericResponse<T>> warning(T errorMessage) {
        return new ResponseEntity<>(GenericResponse.error(errorMessage, HttpStatus.I_AM_A_TEAPOT),
                HttpStatus.I_AM_A_TEAPOT);
    }

    public static <T> ResponseEntity<GenericResponse<T>> errorHttp(T errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(GenericResponse.error(errorMessage, httpStatus),
                httpStatus);
    }

    public static <T> ResponseEntity<GenericResponse<T>> errorHttp(T errorMessage, HttpStatus httpStatus, HttpStatus objectCode) {
        return new ResponseEntity<>(GenericResponse.error(errorMessage, objectCode),
                httpStatus);
    }

    public static <T> GenericResponse<T> error(T errorMessage) {
        return error(errorMessage, HttpStatus.I_AM_A_TEAPOT);
    }

    public static <T> GenericResponse<T> error(T errorMessage, HttpStatus httpStatus) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setPayload(errorMessage);
        response.setResult(httpStatus.name());

        return response;
    }

    private GenericResponse<T> setPayload(T payload) {
        this.payload = payload;
        return this;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    private void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public T getPayload() {
        return payload;
    }

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }
}