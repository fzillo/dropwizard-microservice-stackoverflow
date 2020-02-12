package org.fzillo.services.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.fzillo.services.api.Question;
import org.fzillo.services.api.Tag;
import org.fzillo.services.resources.QuestionsResource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO Exception Handling!
public class QuestionDAO extends AbstractDAO<Question> {

    private static final Logger log = LoggerFactory.getLogger(QuestionDAO.class);

    public QuestionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Question findById(Integer id) {
        return get(id);
    }


    public List<Question> createMultiple(List<Question> questionList){
        List<Question> retSavedQuestionList = new ArrayList<>();

        //TODO stream
        for (Question q :questionList){
            retSavedQuestionList.add(create(q));
        }

        return retSavedQuestionList;
    }

    //TODO comment
    public Question create(Question question) {
        Set<Tag> tags = question.tags;

        //TODO stream
        Set<Tag> replacedTags = new HashSet<>();
        for (Tag t : tags) {
            List<Tag> foundTags = (List<Tag>) namedQuery("org.fzillo.services.api.Tag.FindByValue").setParameter("value", t.value).list();
            if (!foundTags.isEmpty()){
                replacedTags.addAll(foundTags);
            } else {
                replacedTags.add(t);
            }
        }

        currentSession().evict(question); //otherwise Hibernate throws NonUniqueException when you modify the object
        question.tags = replacedTags;

        return persist(question);
    }

    public void delete(Question question) {
        currentSession().delete(question);
    }

    public void deleteAll(){ //TODO this does delete all fks but not Tags
        currentSession().createQuery("delete from Question").executeUpdate();
    }

    public void deleteById(Integer id) {
        Question question = currentSession().load(Question.class, id);

        if (question!= null) {
            currentSession().delete(question);
        } else {
            //TODO log error + response in api
        }
    }

    public List<Question> findAll() {
        return (List<Question>) namedQuery("org.fzillo.services.api.Question.findAll").list();
    }


    public List<Question> findAllWithTag(String tag) {
        return (List<Question>) namedQuery("org.fzillo.services.api.Question.findAllWithTag").setParameter("value", tag).list();
    }
}
