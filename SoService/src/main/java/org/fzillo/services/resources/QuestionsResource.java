package org.fzillo.services.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import org.fzillo.services.api.Question;
import org.fzillo.services.api.QuestionResponseWrapper;
import org.fzillo.services.db.QuestionDAO;
import org.fzillo.services.db.TagDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/featured-questions")
@Produces(MediaType.APPLICATION_JSON)
public class QuestionsResource {

    private final Client client;

    private static final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    private TagDAO tagDao;
    private QuestionDAO questionDAO;

    public QuestionsResource(Client client, TagDAO tagDAO, QuestionDAO questionDAO) {
        //TODO log?
        this.client = client;
        this.tagDao = tagDAO;
        this.questionDAO = questionDAO;
    }

    public void initialize() { //TODO rename
        System.out.println("INIT QuestionsResource");   //TODO log
        //truncateTablesInDB();
    }


    @GET
    @Timed
    @UnitOfWork
    @Path("/startup")
    //TODO implement at application start
    //TODO logging
    public Response startUp() {
        log.info("StartUp has been called!");

        truncateTablesInDB();
        loadTwentyMostFeaturedQuestionsFromStackOverflowIntoDB();

        return Response.status(Response.Status.OK).build();
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
    public Response getQuestionWithId(@NotNull @PathParam("id") Integer  id) {
        Question question = questionDAO.findById(id);

        if (question != null){
            return Response.status(Response.Status.OK).entity(question).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/byId/delete/{id}")
    public Response deleteQuestion(@NotNull @PathParam("id") Integer  id) {
        questionDAO.deleteById(id);
        //FIXME problem with foreign key constraint
        //TODO found/not found
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/byTag/{tag}")
    //TODO multiple tags
    public Response getAllQuestionsWithTag(@NotNull @PathParam("tag") String tag) {
        List<Question> question = questionDAO.findAllWithTag(tag);

        if (question != null){
            return Response.status(Response.Status.OK).entity(question).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    //TODO description
    private void loadTwentyMostFeaturedQuestionsFromStackOverflowIntoDB() {
        QuestionResponseWrapper questionResponseWrapper =
                client.target("https://api.stackexchange.com").path("/2.2/questions/featured") //
                        .queryParam("order","desc") //
                        .queryParam("sort","creation") //
                        .queryParam("site","stackoverflow") //
                        .request(MediaType.APPLICATION_JSON).get(QuestionResponseWrapper.class);

        if (!questionResponseWrapper.questionList.isEmpty()) {
            List<Question> questionList = questionResponseWrapper.getLimitedQuestionList();
            questionDAO.createMultiple(questionList);
        } else {
            //TODO log warning
        }
    }

    //TODO description
    private void truncateTablesInDB(){
        questionDAO.deleteAll();
        tagDao.deleteAll();
        //TODO check success?
    }


    //--------------------------------------------------------
    //    @GET
//    @Path("/find-tag-by-id-test")
//    @Timed
//    @UnitOfWork
//    public Tag findTagByIdTest() {
//        Integer test_id = 1;
//        Tag found = tagDao.findById(test_id);
//
//        return found;
//    }
//
//    @GET
//    @Path("/find-all-questions-test")
//    @Timed
//    @UnitOfWork
//    public List<Question> findAllQuestionsTest() {
//        return questionDAO.findAll();
//    }
//
//    @GET
//    @Path("/find-all-questions-with-tag-test")
//    @Timed
//    @UnitOfWork
//    public List<Question> findAllQuestionsWithTagTest() {
//        return questionDAO.findAllWithTag("Fabian");
//    }
//
//    @GET
//    @Path("/create-question-test")
//    @Timed
//    @UnitOfWork
//    public Question createQuestionTest() {
//
//        Question newQuestion = new Question();
//        newQuestion.question_id = 5;
//        newQuestion.title = "Test5";
//        newQuestion.user_id = 55;
//        newQuestion.answer_count = 44;
//        newQuestion.view_count = 12;
//        newQuestion.is_answered = true;
//        newQuestion.stackoverflow_creation_date = LocalDateTime.now();
//
//        Question questionInDB = questionDAO.create(newQuestion);
//
//        return questionInDB;
//    }
//
//    @DELETE
//    @Path("/delete-question-test/{id}")
//    @Timed
//    @UnitOfWork
//    public Response deleteQuestionTest(@PathParam("id") Integer  id) {
//        questionDAO.deleteById(id);
//        //TODO found/not found
//        return Response.status(Response.Status.OK).build();
//    }
//
//    @DELETE
//    @Path("/delete-all-question-test")
//    @Timed
//    @UnitOfWork
//    public Response deleteQuestionTest() {
//        //TODO should this be done this way?
//        questionDAO.deleteAll();
//        tagDao.deleteAll();
//        //TODO successfull?
//        return Response.status(Response.Status.OK).build();
//    }
//
//    @GET
//    @Path("/get-question-test")
//    @Timed
//    @UnitOfWork
//    public Question getQuestionTest() {
//        Integer test_id = 1;
//        Question found = questionDAO.findById(test_id);
//
//        return found;
//    }
//
//    @GET
//    @Timed
//    @Path("/all-directly-from-SO")
//    //TODO comment why this responsetype?
//    public Response getAllQuestionsFromSO() {
//
//
//        QuestionResponseWrapper questionResponseWrapper =
//                client.target("https://api.stackexchange.com").path("/2.2/questions/featured") //
//                .queryParam("order","desc") //
//                .queryParam("sort","creation") //
//                .queryParam("site","stackoverflow") //
//                .request(MediaType.APPLICATION_JSON).get(QuestionResponseWrapper.class);
//
//        if (!questionResponseWrapper.questionList.isEmpty()) { //TODO
//            return Response.status(Response.Status.OK).entity(questionResponseWrapper.getFirstTwentyItems()).build();
//        }
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }
//
//
//
//    @GET
//    @Timed
//    @UnitOfWork
//    @Path("/all-save-position/{id}")
//    //TODO comment why this responsetype?
//    public Response getAllQuestionsSaveFirst(@PathParam("id") Integer  id) {
//
//
//        QuestionResponseWrapper questionResponseWrapper =
//                client.target("https://api.stackexchange.com").path("/2.2/questions/featured") //
//                        .queryParam("order","desc") //
//                        .queryParam("sort","creation") //
//                        .queryParam("site","stackoverflow") //
//                        .request(MediaType.APPLICATION_JSON).get(QuestionResponseWrapper.class);
//
//        if (!questionResponseWrapper.questionList.isEmpty()) { //TODO
//
//            Question firstQuestion = questionResponseWrapper.getFirstTwentyItems().get(id);
//
//            questionDAO.create(firstQuestion);
//        }
//        return null;
//    }


}