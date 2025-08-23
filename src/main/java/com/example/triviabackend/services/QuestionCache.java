package com.example.triviabackend.services;

import com.example.triviabackend.models.opentrivia.OpenTriviaQuestionResponse;
import com.example.triviabackend.models.dto.Question;
import com.example.triviabackend.models.dto.UnAnsweredQuestion;
import com.example.triviabackend.services.interfaces.SessionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class QuestionCache {
    private static final int AMOUNT = 50;

    private final String apiUrl;
    private final QuestionSessionService questionSessionService;
    private final SessionService sessionService;
    private final RestTemplate restTemplate;
    private final Map<String, Question> questionPerId;

    public QuestionCache(@Value("${trivia.open-trivia-api-url}") String apiUrl, QuestionSessionService questionSessionService, SessionService sessionService) {
        this.apiUrl = apiUrl;
        this.questionSessionService = questionSessionService;
        this.sessionService = sessionService;
        this.restTemplate = new RestTemplate();
        this.questionPerId = new ConcurrentHashMap<>();

        addNewQuestions();
    }

    private void addNewQuestions() {
        String sessionId = sessionService.getSessionId();

        if (sessionId == null) {
            return;
        }

        String url = apiUrl + "?amount=" + AMOUNT + "&token=" + sessionId;

        OpenTriviaQuestionResponse response = restTemplate.getForObject(url, OpenTriviaQuestionResponse.class);

        if (response == null || response.getResults() == null) {
            return;
        }

        response.getResults().forEach(result -> {
            List<String> decodedIncorrectAnswers = result.getIncorrectAnswers().stream()
                    .map(HtmlUtils::htmlUnescape)
                    .toList();

            Question question = new Question(
                    result.getId(),
                    result.getType(),
                    result.getDifficulty(),
                    result.getCategory(),
                    HtmlUtils.htmlUnescape(result.getQuestion()),
                    HtmlUtils.htmlUnescape(result.getCorrectAnswer()),
                    decodedIncorrectAnswers
            );
            questionPerId.put(question.id(), question);
        });
    }


    public List<UnAnsweredQuestion> getQuestions(String sessionId, int limit) {
        return getNotAskedQuestions(sessionId, limit);
    }

    private List<String> getAvailableQuestionIds(List<String> askedQuestionIds) {
        return questionPerId.values().stream()
                .map(Question::id)
                .filter(id -> !askedQuestionIds.contains(id))
                .toList();
    }

    private List<Question> getAvailableQuestions(List<String> availableQuestionIds) {
        List<Question> questions = availableQuestionIds.stream()
                .map(questionPerId::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Collections.shuffle(questions);

        return questions;
    }

    private List<UnAnsweredQuestion> getNotAskedQuestions(String sessionId, int limit) {
        List<String> askedQuestionIds = questionSessionService.getAskedQuestionIds(sessionId);

        List<String> availableQuestionIds = getAvailableQuestionIds(askedQuestionIds);

        while (availableQuestionIds.size() < limit) {
            addNewQuestions();
            availableQuestionIds = getAvailableQuestionIds(askedQuestionIds);
        }

        List<Question> availableQuestions = getAvailableQuestions(availableQuestionIds);
        List<Question> selectedQuestions = availableQuestions.subList(0, Math.min(availableQuestions.size(), limit));

        questionSessionService.addAskedQuestionIds(sessionId, selectedQuestions.stream()
                .map(Question::id)
                .toList());

        return selectedQuestions.stream().map(question -> {
                    List<String> allAnswers = new ArrayList<>(question.incorrectAnswers());
                    allAnswers.add(question.correctAnswer());

                    Collections.shuffle(allAnswers);

                    return new UnAnsweredQuestion(
                            question.id(),
                            question.type(),
                            question.difficulty(),
                            question.category(),
                            question.question(),
                            allAnswers
                    );
                })
                .toList();
    }

    public Question getQuestion(String id) {
        return questionPerId.get(id);
    }
}

