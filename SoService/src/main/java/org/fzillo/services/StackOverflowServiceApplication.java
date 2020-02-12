package org.fzillo.services;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.fzillo.services.api.Question;
import org.fzillo.services.api.Tag;
import org.fzillo.services.db.QuestionDAO;
import org.fzillo.services.db.TagDAO;
import org.fzillo.services.resources.QuestionsResource;
import org.fzillo.services.resources.SoUsersResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;

public class StackOverflowServiceApplication extends Application<StackOverflowServiceConfiguration> {

    private static final Logger log = LoggerFactory.getLogger(StackOverflowServiceApplication.class);

    public static void main(final String[] args) throws Exception {
        new StackOverflowServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "StackOverflowService";
    }

    @Override
    public void initialize(final Bootstrap<StackOverflowServiceConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final StackOverflowServiceConfiguration configuration,
                    final Environment environment) {

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());

        final TagDAO tagDAO = new TagDAO(hibernate.getSessionFactory());
        final QuestionDAO questionDAO = new QuestionDAO(hibernate.getSessionFactory());

        QuestionsResource questionsResource = new QuestionsResource(client, tagDAO, questionDAO);
        SoUsersResource soUsersResource = new SoUsersResource(client);

        //TODO add health checks!

        environment.jersey().register(questionsResource);
        environment.jersey().register(soUsersResource);

        questionsResource.initialize(); //TODO
    }

    private final HibernateBundle<StackOverflowServiceConfiguration> hibernate = new HibernateBundle<StackOverflowServiceConfiguration>(Tag.class,Question.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(StackOverflowServiceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

}
