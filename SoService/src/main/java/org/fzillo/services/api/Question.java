package org.fzillo.services.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.fzillo.services.serializer.CustomLocalDateTimeDeserializer;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "question")
@NamedQueries({
        @NamedQuery(name = "org.fzillo.services.api.Question.findAll",
                query = "select q from Question q")
        ,@NamedQuery(name = "org.fzillo.services.api.Question.findAllWithTag",
                query = "select q from Question q join q.tags t where t.value = :value")
})
public class Question {
    //TODO standard getters/setters
    //TODO public/private

    public Question (){
        //keep default Constructor for Hibernates sake
    }

    @Id
    @JsonProperty("question_id")
    @Column(name = "question_id")
    public Integer question_id;

    //it's not in the requirements, but shouldn't the title be included as well?
    @JsonProperty("title")
    @Column(name = "title")
    public String title;

//    @JsonDeserialize(using = CustomTagSerializer.class)
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
    @JoinTable(
            name = "question_tag",
            joinColumns = { @JoinColumn(name = "question_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    public Set<Tag> tags = new HashSet<>(); //TODO return via api as string List

    @JsonProperty("tags")
    public void unpackNestedTags(List<String> nestedTags) {
        //TODO stream
        for (String nTag: nestedTags){
            tags.add(new Tag(null,nTag));
        }
    }


    @JsonProperty("is_answered")
    @Column(name = "is_answered")
    public Boolean is_answered;

    @JsonProperty("view_count")
    @Column(name = "view_count")
    public Integer view_count;

    @JsonProperty("answer_count")
    @Column(name = "answer_count")
    public Integer answer_count;

    @JsonProperty("creation_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "stackoverflow_creation_date")
    public LocalDateTime stackoverflow_creation_date;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    public Integer user_id;

    @JsonProperty("owner")
    public void unpackNestedUserId(Map<String,Object> owner) {
        this.user_id = (Integer) owner.get("user_id");
    }

   //TODO why is this null on the return object from db?
    @JsonIgnore //TODO comment
    @UpdateTimestamp
    @Column(name = "last_modified")
    public LocalDateTime db_last_modified;

}
