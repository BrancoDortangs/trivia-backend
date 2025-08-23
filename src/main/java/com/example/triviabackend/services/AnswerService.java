package com.example.triviabackend.services;

import com.example.triviabackend.models.answers.*;
import com.example.triviabackend.models.answers.BooleanValidatedAnswer;
import com.example.triviabackend.models.answers.MultipleChoiceValidatedAnswer;
import com.example.triviabackend.models.answers.SubmittedAnswer;
import com.example.triviabackend.models.answers.ValidatedAnswer;
import com.example.triviabackend.models.dto.Question;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AnswerService {
    private final QuestionCache questionCache;

    public AnswerService(QuestionCache questionCache) {
        this.questionCache = questionCache;
    }

    public List<ValidatedAnswer<?>> checkAnswers(List<SubmittedAnswer<?>> answers) {
        return answers.stream()
                .map(submittedAnswer -> {
                    Question question = questionCache.getQuestion(submittedAnswer.id());

                    return switch (submittedAnswer) {
                        case MultipleChoiceSubmittedAnswer multipleChoiceAnswer:
                            yield new MultipleChoiceValidatedAnswer(question.id(), multipleChoiceAnswer.answer(), question.correctAnswer());

                        case BooleanSubmittedAnswer booleanAnswer:
                            boolean correctAnswer = Boolean.parseBoolean(question.correctAnswer());

                            yield (ValidatedAnswer<?>) new BooleanValidatedAnswer(question.id(), booleanAnswer.answer(), correctAnswer);

                        default:
                            yield null;
                    };
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
