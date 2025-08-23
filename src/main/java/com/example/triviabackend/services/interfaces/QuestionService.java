package com.example.triviabackend.services.interfaces;

import com.example.triviabackend.models.dto.UnAnsweredQuestion;

import java.util.List;

public interface QuestionService {
    List<UnAnsweredQuestion> getQuestions(String sessionId, int limit);
}
