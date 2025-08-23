package com.example.triviabackend.models.opentrivia;

import java.util.List;

public class OpenTriviaQuestionResponse extends OpenTriviaResponse {
    private List<OpenTriviaQuestion> results;

    public List<OpenTriviaQuestion> getResults() {
        return results;
    }
}