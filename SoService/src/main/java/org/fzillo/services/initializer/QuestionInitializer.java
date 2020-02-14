package org.fzillo.services.initializer;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import org.fzillo.services.db.IQuestionDAO;
import org.fzillo.services.resources.SoUsersResource;
import org.fzillo.services.stackoverflow.client.StackOverflowClient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class QuestionInitializer implements Managed {

    private static final Logger log = LoggerFactory.getLogger(SoUsersResource.class);

    private final SessionFactory sessionFactory;
    private final StackOverflowClient client;
    private final IQuestionDAO questionDAO;

    public QuestionInitializer(SessionFactory sessionFactory, StackOverflowClient client, IQuestionDAO questionDAO) {
        this.sessionFactory = sessionFactory;
        this.client = client;
        this.questionDAO = questionDAO;
    }

    @Override
    public void start() throws Exception {
        try (Session session = sessionFactory.openSession()) {
            ManagedSessionContext.bind(session);
            questionDAO.createMultiple(client.getQuestions());
            log.info("Initialization of database with data from StackoverFlow was successfull");
        } catch (HibernateException e) {
            log.error("Initialization of database with data from StackoverFlow failed", e);
        }
    }

    @Override
    public void stop() throws Exception {
        //do nothing
    }
}
