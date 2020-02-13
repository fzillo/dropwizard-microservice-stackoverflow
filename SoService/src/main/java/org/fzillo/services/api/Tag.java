package org.fzillo.services.api;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tag")
@NamedQueries({
        @NamedQuery(name = "org.fzillo.services.api.Tag.FindByValue",
                query = "select t from Tag t where t.value like :value")
})
public class Tag {
    //TODO getters/setters
    //TODO public/private

    public Tag (){
        //keep default Constructor for Hibernates sake
    }

    public Tag(Integer tag_id, String value){
        this.tag_id = tag_id;
        this.value = value;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("tag_id")
    @Column(name = "tag_id")
    public Integer tag_id;

    @JsonProperty("value")
    @Column(name = "value")
    public String value;

    @JsonIgnore // because we want no circular dependency
    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions = new HashSet<>();


    @JsonIgnore // not relevant for outside
    @UpdateTimestamp
    @Column(name = "last_modified")
    public LocalDateTime db_last_modified;
}
