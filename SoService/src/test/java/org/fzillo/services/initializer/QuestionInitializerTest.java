package org.fzillo.services.initializer;

import org.fzillo.services.api.Question;
import org.fzillo.services.db.IQuestionDAO;
import org.fzillo.services.stackoverflow.client.StackOverflowClient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionInitializerTest {


    @Test
    public void testStart_passCodeWithoutException() throws Exception {

        SessionFactory sessionFactory = mock(SessionFactory.class);
        Session session = mock(Session.class);
        StackOverflowClient client = mock(StackOverflowClient.class);
        IQuestionDAO questionDAO = mock(IQuestionDAO.class);

        List<Question> questionList = new ArrayList<>();

        when(sessionFactory.openSession()).thenReturn(session);
        when(client.getQuestions()).thenReturn(questionList);
        when(questionDAO.createMultiple(client.getQuestions())).thenReturn(questionList);

        new QuestionInitializer(sessionFactory, client, questionDAO).start();
    }

    @Test
    public void testStart_throwAndCatchHibernateException() throws Exception {

        SessionFactory sessionFactory = mock(SessionFactory.class);
        StackOverflowClient client = mock(StackOverflowClient.class);
        IQuestionDAO questionDAO = mock(IQuestionDAO.class);

        when(sessionFactory.openSession()).thenThrow(new HibernateException("expected_exception"));

        new QuestionInitializer(sessionFactory, client, questionDAO).start();
    }
}
