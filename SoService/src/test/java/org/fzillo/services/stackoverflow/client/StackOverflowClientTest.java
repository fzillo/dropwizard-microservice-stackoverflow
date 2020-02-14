package org.fzillo.services.stackoverflow.client;

import org.fzillo.services.api.Question;
import org.fzillo.services.api.QuestionResponseWrapper;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StackOverflowClientTest {

    @Test
    public void getQuestions_returnQuestionList() {
        Client client = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);

        Question question1 = new Question (1,"Title1",null,true,5,4, LocalDateTime.now(),99);
        Question question2 = new Question (2,"Title1",null,true,5,4, LocalDateTime.now(),99);
        List<Question> questionList = new ArrayList<>();
        questionList.add(question1);
        questionList.add(question2);
        QuestionResponseWrapper questionResponseWrapper = new QuestionResponseWrapper();
        questionResponseWrapper.questionList = questionList;

        when(client.target(StackOverflowClient.STACKOVERFLOW_BASE_URL)).thenReturn(webTarget);
        when(webTarget.path(StackOverflowClient.QUESTION_PATH)).thenReturn(webTarget);
        when(webTarget.queryParam("order", "desc")).thenReturn(webTarget);
        when(webTarget.queryParam("sort", "creation")).thenReturn(webTarget);
        when(webTarget.queryParam("site", "stackoverflow")).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.get(QuestionResponseWrapper.class)).thenReturn(questionResponseWrapper);


        StackOverflowClient stackOverflowClient = new StackOverflowClient(client);

        assertEquals(questionList, stackOverflowClient.getQuestions());
    }
}