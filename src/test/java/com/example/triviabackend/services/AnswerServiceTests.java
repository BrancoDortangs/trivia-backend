package com.example.triviabackend.services;

import com.example.triviabackend.enums.QuestionType;
import com.example.triviabackend.models.answers.*;
import com.example.triviabackend.models.dto.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTests {
    @Mock
    private QuestionCache questionCache;

    @InjectMocks
    private AnswerService answerService;

    @Test
    void shouldValidateBooleanAnswers() {
        Question question = new Question("1", QuestionType.BOOLEAN, "easy", "General Knowledge", "Pluto is a planet.", "True", List.of("False"));
        when(questionCache.getQuestion("1")).thenReturn(question);

        BooleanSubmittedAnswer submittedAnswer = new BooleanSubmittedAnswer("1", true);

        List<ValidatedAnswer<?>> validatedAnswers = answerService.checkAnswers(List.of(submittedAnswer));

        assertEquals(1, validatedAnswers.size());
        assertInstanceOf(BooleanValidatedAnswer.class, validatedAnswers.getFirst());

        BooleanValidatedAnswer validatedAnswer = (BooleanValidatedAnswer) validatedAnswers.getFirst();
        assertEquals("1", validatedAnswer.id());
        assertTrue(validatedAnswer.answer());
        assertTrue(validatedAnswer.correctAnswer());
    }

    @Test
    void shouldValidateMultipleChoiceAnswers() {
        Question question = new Question("2", QuestionType.MULTIPLE, "easy", "General Knowledge", "What is 2+2?", "4", List.of("1", "2", "3"));
        when(questionCache.getQuestion("2")).thenReturn(question);

        MultipleChoiceSubmittedAnswer submittedAnswer = new MultipleChoiceSubmittedAnswer("2", "4");

        List<ValidatedAnswer<?>> validatedAnswers = answerService.checkAnswers(List.of(submittedAnswer));

        assertEquals(1, validatedAnswers.size());
        assertInstanceOf(MultipleChoiceValidatedAnswer.class, validatedAnswers.getFirst());

        MultipleChoiceValidatedAnswer validatedAnswer = (MultipleChoiceValidatedAnswer) validatedAnswers.getFirst();
        assertEquals("2", validatedAnswer.id());
        assertEquals("4", validatedAnswer.answer());
        assertEquals("4", validatedAnswer.correctAnswer());
    }
}

