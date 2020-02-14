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
public class QuestionDAO extends AbstractDAO<Question> implements IQuestionDAO {

    private static final Logger log = LoggerFactory.getLogger(QuestionDAO.class);

    public QuestionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Question findById(Integer id) {
        return get(id);
    }


    @Override
    public List<Question> createMultiple(List<Question> questionList){
        List<Question> retSavedQuestionList = new ArrayList<>();

        //TODO stream
        for (Question question :questionList){
            retSavedQuestionList.add(create(question));
        }

        return retSavedQuestionList;
    }

    //TODO comment
    @Override
    public Question create(Question question) {
        Set<Tag> tags = question.tags;

        Set<Tag> replacedTags = replaceExistingTags(tags);

        currentSession().evict(question); //otherwise Hibernate throws NonUniqueException when you modify the object
        question.tags = replacedTags;

        return persist(question);
    }

    //TODO write test
    private Set<Tag> replaceExistingTags(Set<Tag> tags) {
        Set<Tag> replacedTags = new HashSet<>();

        //TODO stream
        for (Tag t : tags) {
            List<Tag> foundTags = (List<Tag>) namedQuery("org.fzillo.services.api.Tag.FindByValue").setParameter("value", t.value).list();
            if (!foundTags.isEmpty()){
                replacedTags.addAll(foundTags);
            } else {
                replacedTags.add(t);
            }
        }
        return replacedTags;
    }

    @Override
    public void delete(Question question) {
        currentSession().delete(question);
    }

    @Override
    public void deleteAll(){ //TODO this does delete all fks but not Tags
        currentSession().createQuery("delete from Question").executeUpdate();
    }

    @Override
    public void deleteById(Integer id) {
        Question question = currentSession().load(Question.class, id);

        if (question!= null) {
            currentSession().delete(question);
        } else {
            //TODO log error + response in api
        }
    }

    @Override
    public List<Question> findAll() {
        return (List<Question>) namedQuery("org.fzillo.services.api.Question.findAll").list();
    }


    @Override
    public List<Question> findAllWithTag(String tag) {
        return (List<Question>) namedQuery("org.fzillo.services.api.Question.findAllWithTag").setParameter("value", tag).list();
    }
}
