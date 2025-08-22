package com.example.triviabackend.controllers;

import com.example.triviabackend.models.answers.BooleanSubmittedAnswer;
import com.example.triviabackend.models.answers.SingleChoiceSubmittedAnswer;
import com.example.triviabackend.models.dto.AnswerDTO;
import com.example.triviabackend.models.questions.Question;
import com.example.triviabackend.enums.QuestionType;
import com.example.triviabackend.services.QuestionCache;
import com.example.triviabackend.services.interfaces.QuestionService;
import com.example.triviabackend.models.answers.SubmittedAnswer;
import com.example.triviabackend.models.questions.UnAnsweredQuestion;
import com.example.triviabackend.models.answers.ValidatedAnswer;
import com.example.triviabackend.services.AnswerService;
import com.example.triviabackend.services.QuestionSessionService;
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
        return questionSessionService.createSessionId();
    }

    @GetMapping("/questions")
    public List<UnAnsweredQuestion> getQuestions(@RequestParam String sessionId, @RequestParam(required = false, defaultValue = "10") int limit) {
        return questionService.getQuestions(sessionId, limit);
    }

    @PostMapping("/check-answers")
    public List<ValidatedAnswer<?>> checkAnswers(@RequestParam String sessionId, @RequestBody List<AnswerDTO> answers) {
        List<SubmittedAnswer<?>> submittedAnswers = answers.stream().<SubmittedAnswer<?>>
                        map(answer -> {
                    Question question = questionCache.getQuestion(answer.id());
                    if (question.type() == QuestionType.BOOLEAN) {
                        return new BooleanSubmittedAnswer(answer.id(), Boolean.parseBoolean(answer.answer()));
                    }

                    return new SingleChoiceSubmittedAnswer(answer.id(), answer.answer());
                })
                .toList();

        return answerService.checkAnswers(submittedAnswers);
    }
}
