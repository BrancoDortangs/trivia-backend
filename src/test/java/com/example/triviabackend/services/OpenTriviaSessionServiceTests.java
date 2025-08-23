package com.example.triviabackend.services;

import com.example.triviabackend.models.opentrivia.OpenTriviaSessionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenTriviaSessionServiceTests {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenTriviaSessionService openTriviaSessionService;

    private static final String BASE_URL = "https://api.com/api_token";
    private static final String SESSION_URL = BASE_URL + "?command=request";
    private static final String SESSION_ID = "session-id";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(openTriviaSessionService, "apiTokenUrl", BASE_URL);
    }

    @Test
    void shouldInitSessionId() {
        OpenTriviaSessionResponse response = new OpenTriviaSessionResponse();
        ReflectionTestUtils.setField(response, "token", SESSION_ID);

        when(restTemplate.getForObject(SESSION_URL, OpenTriviaSessionResponse.class))
                .thenReturn(response);

        ReflectionTestUtils.invokeMethod(openTriviaSessionService, "initSessionId");

        String sessionId = openTriviaSessionService.getSessionId();
        assertEquals(SESSION_ID, sessionId);
    }

    @Test
    void shouldHandleNullResponse() {
        when(restTemplate.getForObject(SESSION_URL, OpenTriviaSessionResponse.class))
                .thenReturn(null);

        ReflectionTestUtils.invokeMethod(openTriviaSessionService, "initSessionId");

        assertNull(openTriviaSessionService.getSessionId());
    }

    @Test
    void shouldHandleNullToken() {
        OpenTriviaSessionResponse response = new OpenTriviaSessionResponse();
        ReflectionTestUtils.setField(response, "token", null);

        when(restTemplate.getForObject(SESSION_URL, OpenTriviaSessionResponse.class))
                .thenReturn(response);

        ReflectionTestUtils.invokeMethod(openTriviaSessionService, "initSessionId");

        assertNull(openTriviaSessionService.getSessionId());
    }
}