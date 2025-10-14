package com.example.linebook.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    OPERATION_BUSY("OPERATION_BUSY", "Operation is busy, please try again later"),
    DUPLICATE_UNIQUE("DUPLICATE_UNIQUE", "Duplicate unique constraint"),
    BAD_REQUEST("BAD_REQUEST", "Bad request"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized"),
    FORBIDDEN("FORBIDDEN", "Forbidden"),
    NOT_FOUND("NOT_FOUND", "Resource not found"),
    LIMIT_MAX("LIMIT_MAX", "limit max"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal server error");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
