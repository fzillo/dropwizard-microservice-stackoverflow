package org.fzillo.services.api;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class QuestionTest {

    @Test
    public void unpackNestedTags_CONTAINS_VALUES() {

        Question question = new Question();

        String tag1value = "Tag1";
        String tag2value = "Tag2";

        List<String> stringList = Arrays.asList(new String[]{tag1value, tag2value});

        question.unpackNestedTags(stringList);

        assertEquals(false,question.tags.isEmpty());
        assertNotNull(question.tags.stream().filter((tag) -> tag.value.equals(tag1value)).findAny().get());
        assertNotNull(question.tags.stream().filter((tag) -> tag.value.equals(tag2value)).findAny().get());
    }

    @Test
    public void unpackNestedUserId_CONTAINS_VALUE() {

        Question question = new Question();
        Integer userId = 1;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("user_id", userId);

        question.unpackNestedUserId(linkedHashMap);

        assertEquals(userId, question.user_id);
    }
}