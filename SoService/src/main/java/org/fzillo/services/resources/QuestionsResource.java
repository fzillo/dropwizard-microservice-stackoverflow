package org.fzillo.services.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import org.fzillo.services.api.Question;
import org.fzillo.services.db.IQuestionDAO;
import org.fzillo.services.db.QuestionDAO;
import org.fzillo.services.db.TagDAO;
import org.fzillo.services.stackoverflow.client.StackOverflowClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/featured-questions")
@Produces(MediaType.APPLICATION_JSON)
public class QuestionsResource {

    private final IQuestionDAO questionDAO;

    public QuestionsResource(IQuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/all")
    //TODO comment why this responsetype?
    //TODO logging
    public Response getAllQuestions() {
        List<Question> questionList = questionDAO.findAll();

        if (!questionList.isEmpty()) {
            return Response.status(Response.Status.OK).entity(questionList).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/byId/{id}")
    public Response getQuestionWithId(@NotNull @PathParam("id") Integer id) {
        Question question = questionDAO.findById(id);

        if (question != null) {
            return Response.status(Response.Status.OK).entity(question).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/byId/delete/{id}")
    public Response deleteQuestion(@NotNull @PathParam("id") Integer id) {
        Question question = questionDAO.findById(id);

        if (question == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        questionDAO.delete(question);

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/byTag/{tag}")
    //TODO multiple tags as queryparam
    public Response getAllQuestionsWithTag(@NotNull @PathParam("tag") String tag) {
        List<Question> question = questionDAO.findAllWithTag(tag);

        if (question != null) {
            return Response.status(Response.Status.OK).entity(question).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

}