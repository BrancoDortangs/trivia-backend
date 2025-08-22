package com.example.triviabackend.services;

import com.example.triviabackend.models.questions.OpenTriviaResponse;
import com.example.triviabackend.models.questions.Question;
import com.example.triviabackend.models.questions.UnAnsweredQuestion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QuestionCache {
    private final QuestionSessionService questionSessionService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Question> questionPerId = new ConcurrentHashMap<>();

    @Value("${trivia.open-trivia-api-url}")
    private String API_URL;

    @Value("${trivia.default-amount}")
    private int DEFAULT_AMOUNT;

    public QuestionCache(QuestionSessionService questionSessionService) {
        this.questionSessionService = questionSessionService;
    }

    private void addNewQuestions() {
        addNewQuestions(DEFAULT_AMOUNT);
    }

    private void addNewQuestions(int amount) {
        String url = API_URL + "?amount=" + amount;

        OpenTriviaResponse response = restTemplate.getForObject(url, OpenTriviaResponse.class);

        if (response != null && response.getResults() != null) {
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
    }

    public List<UnAnsweredQuestion> getQuestions(String sessionId, int limit) {
        if (questionPerId.isEmpty()) {
            addNewQuestions();
        }

        return this.getNotAskedQuestions(sessionId, limit);
    }

    private List<UnAnsweredQuestion> getNotAskedQuestions(String sessionId, int limit) {
        List<String> askedQuestionIds = questionSessionService.getAskedQuestionIds(sessionId);

        List<Question> availableQuestions = new ArrayList<>(questionPerId.values());
        availableQuestions.removeIf(availableQuestion -> askedQuestionIds.contains(availableQuestion.id()));

        if (availableQuestions.size() < limit) {
            addNewQuestions(limit - availableQuestions.size());
            availableQuestions = new ArrayList<>(questionPerId.values());
            availableQuestions.removeIf(availableQuestion -> askedQuestionIds.contains(availableQuestion.id()));
        }

        Collections.shuffle(availableQuestions);

        List<Question> selectedQuestions = availableQuestions.stream()
                .limit(limit)
                .toList();

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

