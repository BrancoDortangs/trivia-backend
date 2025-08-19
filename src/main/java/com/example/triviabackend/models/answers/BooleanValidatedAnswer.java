package com.example.triviabackend.models.answers;

public record BooleanValidatedAnswer(String id, Boolean answer, Boolean correctAnswer,
                                     boolean isCorrect) implements ValidatedAnswer<Boolean> {
}