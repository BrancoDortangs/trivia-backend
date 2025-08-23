package com.example.triviabackend.services;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuestionSessionService {
    private final Map<String, List<String>> askedQuestionIdsPerSessionId = new ConcurrentHashMap<>();

    public String createSessionId() {
        String sessionId = UUID.randomUUID().toString();

        askedQuestionIdsPerSessionId.put(sessionId, new ArrayList<>());

        return sessionId;
    }

    public boolean sessionExists(String sessionId) {
        return askedQuestionIdsPerSessionId.containsKey(sessionId);
    }

    public List<String> getAskedQuestionIds(String sessionId) {
        if (!sessionExists(sessionId)) {
            throw new IllegalArgumentException("Session does not exist " + sessionId);
        }

        return askedQuestionIdsPerSessionId.get(sessionId);
    }

    public void addAskedQuestionIds(String sessionId, List<String> questionIds) {
        if (!sessionExists(sessionId)) {
            throw new IllegalArgumentException("Session does not exist " + sessionId);
        }

        askedQuestionIdsPerSessionId.get(sessionId).addAll(questionIds);
    }
}
