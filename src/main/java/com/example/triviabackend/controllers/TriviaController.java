package com.example.triviabackend.controllers;

import com.example.triviabackend.models.answers.BooleanSubmittedAnswer;
import com.example.triviabackend.models.answers.MultipleChoiceSubmittedAnswer;
import com.example.triviabackend.models.dto.Answer;
import com.example.triviabackend.services.QuestionCache;
import com.example.triviabackend.services.interfaces.QuestionService;
import com.example.triviabackend.models.answers.SubmittedAnswer;
import com.example.triviabackend.models.dto.UnAnsweredQuestion;
import com.example.triviabackend.models.answers.ValidatedAnswer;
import com.example.triviabackend.services.AnswerService;
import com.example.triviabackend.services.QuestionSessionService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TriviaController {
    private final QuestionService questionService;
    private final QuestionSessionService questionSessionService;
    private final AnswerService answerService;
    private final QuestionCache questionCache;

    public TriviaController(QuestionService questionService, QuestionSessionService questionSessionService, AnswerService answerService, QuestionCache questionCache) {
        this.questionService = questionService;
        this.questionSessionService = questionSessionService;
        this.answerService = answerService;
        this.questionCache = questionCache;
    }

    @GetMapping("/session")
    public Map<String, String> createSession() {
        return Map.of("sessionId", questionSessionService.createSessionId());
    }

    @GetMapping("/questions")
    public List<UnAnsweredQuestion> getQuestions(@RequestParam String sessionId, @RequestParam(required = false, defaultValue = "10") int limit, HttpServletResponse response) {
        if (!questionSessionService.sessionExists(sessionId)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }

        return questionService.getQuestions(sessionId, limit);
    }

    @PostMapping("/check-answers")
    public List<ValidatedAnswer<?>> checkAnswers(@RequestBody List<Answer> answers) {
        List<SubmittedAnswer<?>> submittedAnswers = answers.stream().<SubmittedAnswer<?>>
                        map(answer -> switch (questionCache.getQuestion(answer.id()).type()) {
                    case BOOLEAN -> new BooleanSubmittedAnswer(answer.id(), Boolean.parseBoolean(answer.answer()));
                    case MULTIPLE -> new MultipleChoiceSubmittedAnswer(answer.id(), answer.answer());
                })
                .toList();

        return answerService.checkAnswers(submittedAnswers);
    }
}
