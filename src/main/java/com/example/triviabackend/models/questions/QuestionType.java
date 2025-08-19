package com.example.triviabackend.models.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum QuestionType {
    @JsonProperty("boolean")
    BOOLEAN,
    @JsonProperty("multiple")
    MULTIPLE
}