package com.example.triviabackend.models.answers;

public sealed interface ValidatedAnswer<AnswerType> extends SubmittedAnswer<AnswerType>
        permits BooleanValidatedAnswer, SingleChoiceValidatedAnswer {
    String id();

    AnswerType answer();

    AnswerType correctAnswer();

    boolean isCorrect();
}
