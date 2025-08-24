# Trivia backend

## Introduction

This Java Spring Boot BE can be used to retrieve some trivia questions. You can also validate the user's answers of these questions with this API. The
questions are retrieved from the Open Trivia API (https://opentdb.com/).

This project handles basic question retrieval and retrieves new questions when needed. An internal cache with unique
questions makes sure that the user doesn't retrieve the same questions multiple time. This is done with a session.

## How to use

- The FE retrieves a session id first. `/session`.
- This session id should be used as a request parameter `sessionId` for every call to `/questions` to ensure unique questions for this user are returned.
- After the user has answered the questions the `/check-answers` endpoint can be used to validate these questions. The session id is not needed when making this call.

## Development

Start the project using a tool like IntelliJ. Important services have been tested, these files are located in
`src/test`.

## Future improvements

Questions are retrieved from the Open Trivia API. That API has some errors which might occur. For instance retrieving questions too often or reaching the question limit.
The current implementation works for many questions and does not reach these limits. More error handling for edge cases should be added in the future.

Scenarios like the FE using the old session id after the BE has restarted could be solved by using a database instead of an in memory cache. In this version that is not supported.