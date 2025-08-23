package com.example.triviabackend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionSessionServiceTests {
    @InjectMocks
    private QuestionSessionService questionSessionService;

    private static final String INVALID_SESSION_ID = "invalidSessionId";

    @Test
    void shouldCreateSessionId() {
        String sessionId = questionSessionService.createSessionId();
        assertNotNull(sessionId);
        assertTrue(questionSessionService.sessionExists(sessionId));
        assertEquals(0, questionSessionService.getAskedQuestionIds(sessionId).size());
    }

    @Test
    void sessionShouldNotExistsIfNotInitialized() {
        assertFalse(questionSessionService.sessionExists(INVALID_SESSION_ID));
    }

    @Test
    void shouldGetAskedQuestionIds() {
        String sessionId = questionSessionService.createSessionId();
        questionSessionService.addAskedQuestionIds(sessionId, Arrays.asList("1", "2"));

        List<String> questionIds = questionSessionService.getAskedQuestionIds(sessionId);
        assertEquals(2, questionIds.size());
        assertTrue(questionIds.containsAll(Arrays.asList("1", "2")));
    }

    @Test
    void shouldThrowExceptionWhenGettingQuestionsFromNonExistentSession() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> questionSessionService.getAskedQuestionIds(INVALID_SESSION_ID)
        );
        assertTrue(exception.getMessage().contains(INVALID_SESSION_ID));
    }

    @Test
    void shouldAddAskedQuestionIds() {
        String sessionId = questionSessionService.createSessionId();
        questionSessionService.addAskedQuestionIds(sessionId, Arrays.asList("1"));
        questionSessionService.addAskedQuestionIds(sessionId, Arrays.asList("2"));

        List<String> questionIds = questionSessionService.getAskedQuestionIds(sessionId);
        assertEquals(2, questionIds.size());
        assertTrue(questionIds.containsAll(Arrays.asList("1", "2")));
    }

    @Test
    void shouldThrowExceptionWhenAddingQuestionsToNonExistentSession() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> questionSessionService.addAskedQuestionIds(INVALID_SESSION_ID, Arrays.asList("1"))
        );
        assertTrue(exception.getMessage().contains(INVALID_SESSION_ID));
    }
}