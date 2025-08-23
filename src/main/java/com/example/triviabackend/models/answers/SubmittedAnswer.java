package com.example.triviabackend.models.answers;

public sealed interface SubmittedAnswer<T> permits BooleanSubmittedAnswer, MultipleChoiceSubmittedAnswer, ValidatedAnswer {
    String id();

    T answer();
}
