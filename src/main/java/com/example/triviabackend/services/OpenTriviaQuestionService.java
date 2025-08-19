package com.example.triviabackend.services;

import com.example.triviabackend.models.questions.UnAnsweredQuestion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenTriviaQuestionService implements QuestionService {
    private final QuestionCache questionCache;

    public OpenTriviaQuestionService(QuestionCache questionCache) {
        this.questionCache = questionCache;
    }

    @Override
    public List<UnAnsweredQuestion> getQuestions(String sessionId, int limit) {
        return questionCache.getQuestions(sessionId, limit);
    }
}
