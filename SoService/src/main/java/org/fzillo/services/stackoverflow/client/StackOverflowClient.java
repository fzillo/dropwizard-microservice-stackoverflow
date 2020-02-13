package org.fzillo.services.stackoverflow.client;

import org.fzillo.services.api.Question;
import org.fzillo.services.api.QuestionResponseWrapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class StackOverflowClient implements IStackOverflowClient {

    public static final String STACKOVERFLOW_BASE_URL = "https://api.stackexchange.com";
    public static final String QUESTION_PATH = "/2.2/questions/featured";

    private final Client client;

    public StackOverflowClient(Client client) {
        this.client = client;
    }

    @Override
    public List<Question> getQuestions() {
        return client.target(STACKOVERFLOW_BASE_URL).path(QUESTION_PATH) //
                .queryParam("order", "desc") //
                .queryParam("sort", "creation") //
                .queryParam("site", "stackoverflow") //
                .request(MediaType.APPLICATION_JSON).get(QuestionResponseWrapper.class).getLimitedQuestionList();
    }
}
