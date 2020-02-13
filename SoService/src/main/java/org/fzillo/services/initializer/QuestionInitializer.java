package org.fzillo.services.initializer;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.lifecycle.Managed;
import org.fzillo.services.db.IQuestionDAO;
import org.fzillo.services.resources.SoUsersResource;
import org.fzillo.services.stackoverflow.client.StackOverflowClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionInitializer implements Managed {

    private static final Logger log = LoggerFactory.getLogger(SoUsersResource.class);

    private final StackOverflowClient client;

    private final IQuestionDAO questionDAO;

    public QuestionInitializer(StackOverflowClient client, IQuestionDAO questionDAO) {
        this.client = client;
        this.questionDAO = questionDAO;
    }

    @Override
    public void start() throws Exception {
        log.info("Initializing Database from StackoverFlow");
        //TODO test this
        //questionDAO.createMultiple(client.getQuestions());
    }

    @Override
    public void stop() throws Exception {
        //do nothing
    }
}
