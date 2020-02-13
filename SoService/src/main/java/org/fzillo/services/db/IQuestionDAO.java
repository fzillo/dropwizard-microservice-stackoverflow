package org.fzillo.services.db;

import org.fzillo.services.api.Question;

import java.util.List;

public interface IQuestionDAO {
    Question findById(Integer id);

    List<Question> createMultiple(List<Question> questionList);

    //TODO comment
    Question create(Question question);

    void delete(Question question);

    void deleteAll();

    void deleteById(Integer id);

    List<Question> findAll();

    List<Question> findAllWithTag(String tag);
}
