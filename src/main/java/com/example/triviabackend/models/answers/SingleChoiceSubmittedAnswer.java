package com.example.triviabackend.models.answers;

public record SingleChoiceSubmittedAnswer(String id,
                                          String answer) implements SubmittedAnswer<String> {
}