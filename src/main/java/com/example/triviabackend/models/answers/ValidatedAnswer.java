package com.example.triviabackend.models.answers;

public sealed interface ValidatedAnswer<T> extends SubmittedAnswer<T>
        permits BooleanValidatedAnswer, MultipleChoiceValidatedAnswer {
    T correctAnswer();

    default boolean isCorrect() {
        return answer().equals(correctAnswer());
    }
}
