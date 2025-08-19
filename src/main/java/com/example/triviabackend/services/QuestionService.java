package com.example.triviabackend.services;

import com.example.triviabackend.models.questions.UnAnsweredQuestion;

import java.util.List;

public interface QuestionService {
    List<UnAnsweredQuestion> getQuestions(String sessionId, int limit);
}
