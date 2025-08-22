package com.example.triviabackend.models.answers;

public sealed interface ValidatedAnswer<AnswerType> extends SubmittedAnswer<AnswerType>
        permits BooleanValidatedAnswer, SingleChoiceValidatedAnswer {
    AnswerType correctAnswer();

    boolean isCorrect();
}
