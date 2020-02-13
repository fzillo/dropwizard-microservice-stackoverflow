package org.fzillo.services.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO comment
public class QuestionResponseWrapper {

    public static int LIMIT_QUESTIONLIST = 20;

    @JsonProperty("items")
    public List<Question> questionList;

    public List<Question> getLimitedQuestionList(){
        if (questionList != null) {
            return questionList.stream().limit(LIMIT_QUESTIONLIST).collect(Collectors.toList());
        }
        return new ArrayList<Question>();
    }

}
