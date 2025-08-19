package com.example.triviabackend.models.answers;

public record SingleChoiceValidatedAnswer(String id, String answer, String correctAnswer,
                                          boolean isCorrect) implements ValidatedAnswer<String> {
}