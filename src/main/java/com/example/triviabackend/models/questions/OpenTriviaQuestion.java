package com.example.triviabackend.models.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class OpenTriviaQuestion {
    private String id;

    private QuestionType type;
    private String difficulty;
    private String category;
    private String question;

    @JsonProperty("correct_answer")
    private String correctAnswer;

    @JsonProperty("incorrect_answers")
    private List<String> incorrectAnswers;

    public String getId() {
        if (id == null) {
            id = Integer.toHexString(Objects.hash(type, difficulty, category, question, correctAnswer));
        }

        return id;
    }

    public QuestionType getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }


}