package com.example.triviabackend.models.dto;

import com.example.triviabackend.enums.QuestionType;

import java.util.List;

public record UnAnsweredQuestion(String id, QuestionType type, String difficulty, String category, String question,
                                 List<String> answers) {
}
