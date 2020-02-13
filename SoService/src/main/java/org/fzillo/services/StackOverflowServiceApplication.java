package org.fzillo.services;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.fzillo.services.api.Question;
import org.fzillo.services.api.Tag;
import org.fzillo.services.db.QuestionDAO;
import org.fzillo.services.db.TagDAO;
import org.fzillo.services.initializer.QuestionInitializer;
import org.fzillo.services.resources.QuestionsResource;
import org.fzillo.services.resources.SoUsersResource;
import org.fzillo.services.stackoverflow.client.StackOverflowClient;
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
        //TODO add health checks!
        //TODO dependency injection? e.g. Juice

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());

        final QuestionDAO questionDAO = new QuestionDAO(hibernate.getSessionFactory());

        StackOverflowClient stackOverflowClient = new StackOverflowClient(client);

        //TODO QuestionInitializer needs stackOverflowClient
        QuestionInitializer questionInitializer = new QuestionInitializer(stackOverflowClient, questionDAO);
        //QuestionInitializer questionInitializer = new UnitOfWorkAwareProxyFactory(hibernate).create(QuestionInitializer.class, QuestionDAO.class, questionDAO);


        environment.lifecycle().manage(questionInitializer);
        environment.jersey().register(new QuestionsResource(questionDAO));
        environment.jersey().register(new SoUsersResource(client));
    }

    private final HibernateBundle<StackOverflowServiceConfiguration> hibernate = new HibernateBundle<StackOverflowServiceConfiguration>(Tag.class,Question.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(StackOverflowServiceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

}
