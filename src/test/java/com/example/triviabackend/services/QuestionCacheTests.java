package com.example.triviabackend.services;

import com.example.triviabackend.enums.QuestionType;
import com.example.triviabackend.models.dto.Question;
import com.example.triviabackend.models.dto.UnAnsweredQuestion;
import com.example.triviabackend.models.opentrivia.OpenTriviaQuestion;
import com.example.triviabackend.models.opentrivia.OpenTriviaQuestionResponse;
import com.example.triviabackend.services.interfaces.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionCacheTests {
    private static final String BASE_URL = "https://api.com/api.php";
    private static final String SESSION_ID = "session-id";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private QuestionSessionService questionSessionService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private QuestionCache questionCache;

    @Test
    void shouldAddQuestionsToCache() {
        when(sessionService.getSessionId()).thenReturn(SESSION_ID);
        when(questionSessionService.getAskedQuestionIds(SESSION_ID)).thenReturn(List.of());

        OpenTriviaQuestion question = new OpenTriviaQuestion();
        ReflectionTestUtils.setField(question, "id", "1");
        ReflectionTestUtils.setField(question, "type", QuestionType.BOOLEAN);
        ReflectionTestUtils.setField(question, "difficulty", "easy");
        ReflectionTestUtils.setField(question, "category", "General Knowledge");
        ReflectionTestUtils.setField(question, "question", "Pluto is a planet.");
        ReflectionTestUtils.setField(question, "correctAnswer", "True");
        ReflectionTestUtils.setField(question, "incorrectAnswers", List.of("False"));

        OpenTriviaQuestionResponse response = new OpenTriviaQuestionResponse();
        ReflectionTestUtils.setField(response, "results", List.of(question));

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaQuestionResponse.class)))
                .thenReturn(response);

        questionCache.getNotAskedQuestions(SESSION_ID, 1);

        Question cachedQuestion = questionCache.getQuestion("1");

        assertNotNull(cachedQuestion);
        assertEquals("Pluto is a planet.", cachedQuestion.question());
        assertEquals("True", cachedQuestion.correctAnswer());

        verify(questionSessionService).addAskedQuestionIds(eq(SESSION_ID), eq(List.of("1")));
    }

    @Test
    void getQuestions_shouldReturnUnAnsweredQuestions() {
        when(sessionService.getSessionId()).thenReturn(SESSION_ID);
        when(questionSessionService.getAskedQuestionIds(SESSION_ID)).thenReturn(List.of());

        OpenTriviaQuestion question = new OpenTriviaQuestion();
        ReflectionTestUtils.setField(question, "id", "1");
        ReflectionTestUtils.setField(question, "type", QuestionType.BOOLEAN);
        ReflectionTestUtils.setField(question, "difficulty", "easy");
        ReflectionTestUtils.setField(question, "category", "General Knowledge");
        ReflectionTestUtils.setField(question, "question", "Pluto is a planet.");
        ReflectionTestUtils.setField(question, "correctAnswer", "True");
        ReflectionTestUtils.setField(question, "incorrectAnswers", List.of("False"));

        OpenTriviaQuestionResponse response = new OpenTriviaQuestionResponse();
        ReflectionTestUtils.setField(response, "results", List.of(question));

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaQuestionResponse.class)))
                .thenReturn(response);

        List<UnAnsweredQuestion> questions = questionCache.getNotAskedQuestions(SESSION_ID, 1);

        assertEquals(1, questions.size());
        UnAnsweredQuestion unAnsweredQuestion = questions.getFirst();
        assertEquals("1", unAnsweredQuestion.id());
        assertTrue(unAnsweredQuestion.answers().contains("True"));
        assertTrue(unAnsweredQuestion.answers().contains("False"));
    }

    @Test
    void getQuestion_shouldReturnNullIfNotPresent() {
        assertNull(questionCache.getQuestion("0"));
    }
}