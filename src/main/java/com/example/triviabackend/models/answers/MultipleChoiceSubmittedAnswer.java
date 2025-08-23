package com.example.triviabackend.models.answers;

public record MultipleChoiceSubmittedAnswer(String id,
                                            String answer) implements SubmittedAnswer<String> {
}