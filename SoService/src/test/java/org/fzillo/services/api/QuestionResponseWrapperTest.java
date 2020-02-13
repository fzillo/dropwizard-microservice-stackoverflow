package org.fzillo.services.api;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class QuestionResponseWrapperTest {

    public static int DEFAULT_SIZE_QUESTIONLIST_STACKOVERFLOW = 30;

    @Test
    public void getLimitedQuestionList_RETURNS_LIMITED_LIST() {
        QuestionResponseWrapper wrapper = createQuestionResponseWrapperForTesting();

        assertNotNull(wrapper.getLimitedQuestionList());
        assertEquals(QuestionResponseWrapper.LIMIT_QUESTIONLIST, wrapper.getLimitedQuestionList().size());
    }

    @Test
    public void getLimitedQuestionList_RETURNS_EMPTY_LIST() {
        QuestionResponseWrapper wrapper = new QuestionResponseWrapper();

        assertNotNull(wrapper.getLimitedQuestionList());
        assertEquals(true, wrapper.getLimitedQuestionList().isEmpty());
    }

    //TODO TEST IS_ORDERED?

    private QuestionResponseWrapper createQuestionResponseWrapperForTesting (){

        QuestionResponseWrapper wrapper = new QuestionResponseWrapper();
        List<Question> questionList = new ArrayList<>();

        for (int i= 0; i < DEFAULT_SIZE_QUESTIONLIST_STACKOVERFLOW; i++){
            Integer question_id = new Integer(i+1);
            String title = "TestTitle";
            Set<Tag> tags = new HashSet<>();
            Boolean is_answered=true;
            Integer view_count=5;
            Integer answer_count=3;
            LocalDateTime stackoverflow_creation_date = LocalDateTime.now();
            Integer user_id = 99;

            Question question = new Question(question_id, title, tags, is_answered, view_count,
                    answer_count, stackoverflow_creation_date, user_id);

            questionList.add(question);
        }

        wrapper.questionList = questionList;

        return wrapper;
    }
}