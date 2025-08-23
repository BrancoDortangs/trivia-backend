package com.example.triviabackend.models.opentrivia;

import com.example.triviabackend.enums.OpenTriviaResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class OpenTriviaResponse {
    @JsonProperty("response_code")
    private OpenTriviaResponseCode responseCode;

    public OpenTriviaResponseCode getResponseCode() {
        return responseCode;
    }
}
