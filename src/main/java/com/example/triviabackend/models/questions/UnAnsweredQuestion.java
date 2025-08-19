package com.example.triviabackend.models.questions;

import java.util.List;

public record UnAnsweredQuestion(String id, QuestionType type, String difficulty, String category, String question,
                                 List<String> answers) {
}
