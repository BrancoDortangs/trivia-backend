package com.example.triviabackend.services;

import com.example.triviabackend.models.opentrivia.OpenTriviaSessionResponse;
import com.example.triviabackend.services.interfaces.SessionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenTriviaSessionService implements SessionService {
    private final RestTemplate restTemplate;

    @Value("${trivia.open-trivia-api-token-url}")
    private String apiTokenUrl;

    private String sessionId;

    public OpenTriviaSessionService() {
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    private void initSessionId() {
        String url = apiTokenUrl + "?command=request";

        OpenTriviaSessionResponse response = restTemplate.getForObject(url, OpenTriviaSessionResponse.class);

        if (response == null || response.getToken() == null) {
            return;
        }

        sessionId = response.getToken();
    }

    public String getSessionId() {
        return sessionId;
    }
}
