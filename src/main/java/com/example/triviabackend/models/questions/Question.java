package com.example.triviabackend.models.questions;

import com.example.triviabackend.enums.QuestionType;

import java.util.List;

public record Question(String id, QuestionType type, String difficulty, String category, String question,
                       String correctAnswer, List<String> incorrectAnswers) {
}
