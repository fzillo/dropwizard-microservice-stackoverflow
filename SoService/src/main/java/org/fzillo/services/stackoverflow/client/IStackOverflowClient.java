package org.fzillo.services.stackoverflow.client;

import org.fzillo.services.api.Question;

import java.util.List;

public interface IStackOverflowClient {
    List<Question> getQuestions();
}
