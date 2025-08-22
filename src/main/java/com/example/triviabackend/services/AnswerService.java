package com.example.triviabackend.services;

import com.example.triviabackend.models.answers.*;
import com.example.triviabackend.models.answers.BooleanValidatedAnswer;
import com.example.triviabackend.models.answers.SingleChoiceValidatedAnswer;
import com.example.triviabackend.models.answers.SubmittedAnswer;
import com.example.triviabackend.models.answers.ValidatedAnswer;
import com.example.triviabackend.models.questions.Question;
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

                    if (submittedAnswer instanceof SingleChoiceSubmittedAnswer singleChoiceAnswer) {
                        boolean isCorrect = singleChoiceAnswer.answer().equals(question.correctAnswer());

                        return (ValidatedAnswer<?>) new SingleChoiceValidatedAnswer(
                                question.id(),
                                singleChoiceAnswer.answer(),
                                question.correctAnswer(),
                                isCorrect
                        );
                    } else if (submittedAnswer instanceof BooleanSubmittedAnswer booleanAnswer) {
                        boolean correctAnswer = Boolean.parseBoolean(question.correctAnswer());
                        boolean isCorrect = booleanAnswer.answer() == correctAnswer;

                        return (ValidatedAnswer<?>) new BooleanValidatedAnswer(
                                question.id(),
                                booleanAnswer.answer(),
                                correctAnswer,
                                isCorrect
                        );
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
