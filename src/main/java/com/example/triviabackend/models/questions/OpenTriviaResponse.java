package com.example.triviabackend.models.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OpenTriviaResponse {
    @JsonProperty("response_code")
    private int responseCode;

    private List<OpenTriviaQuestion> results;

    public int getResponseCode() {
        return responseCode;
    }

    public List<OpenTriviaQuestion> getResults() {
        return results;
    }
}