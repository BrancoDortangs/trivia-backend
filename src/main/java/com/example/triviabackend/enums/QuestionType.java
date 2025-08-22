package com.example.triviabackend.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QuestionType {
    @JsonProperty("boolean")
    BOOLEAN,
    @JsonProperty("multiple")
    MULTIPLE
}