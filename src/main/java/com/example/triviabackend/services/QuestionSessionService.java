package com.example.triviabackend.services;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuestionSessionService {
    private final Map<String, List<String>> askedQuestionIdsPerSessionId = new ConcurrentHashMap<>();

    public Map<String, String> createSessionId() {
        String sessionId = UUID.randomUUID().toString();

        askedQuestionIdsPerSessionId.put(sessionId, new ArrayList<>());

        return Collections.singletonMap("sessionId", sessionId);
    }

    public List<String> getAskedQuestionIds(String sessionId) {
        return askedQuestionIdsPerSessionId.getOrDefault(sessionId, new ArrayList<>());
    }

    public void addAskedQuestionIds(String sessionId, List<String> questionIds) {
        askedQuestionIdsPerSessionId.computeIfAbsent(sessionId, k -> new ArrayList<>()).addAll(questionIds);
    }
}
