package com.example.triviabackend.models.answers;

public sealed interface SubmittedAnswer<AnswerType> permits BooleanSubmittedAnswer, SingleChoiceSubmittedAnswer, ValidatedAnswer {
    String id();

    AnswerType answer();
}
