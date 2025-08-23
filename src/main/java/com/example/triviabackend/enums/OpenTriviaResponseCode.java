package com.example.triviabackend.enums;

public enum OpenTriviaResponseCode {
    SUCCESS(0),
    NO_RESULTS(1),
    INVALID_PARAMETER(2),
    TOKEN_NOT_FOUND(3),
    TOKEN_EMPTY(4),
    RATE_LIMIT(5);

    private final int code;

    OpenTriviaResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
