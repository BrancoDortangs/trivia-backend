package com.example.triviabackend.models.answers;

public record MultipleChoiceValidatedAnswer(String id, String answer, String correctAnswer) implements ValidatedAnswer<String> {
}