package com.example.triviabackend.models.answers;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface ValidatedAnswer<T> extends SubmittedAnswer<T>
        permits BooleanValidatedAnswer, MultipleChoiceValidatedAnswer {
    T correctAnswer();

    @JsonProperty("isCorrect")
    default boolean isCorrect() {
        return answer().equals(correctAnswer());
    }
}
