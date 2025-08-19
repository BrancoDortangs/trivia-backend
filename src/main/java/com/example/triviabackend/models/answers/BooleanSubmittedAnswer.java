package com.example.triviabackend.models.answers;

public record BooleanSubmittedAnswer(String id, Boolean answer) implements SubmittedAnswer<Boolean> {
}