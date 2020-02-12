package org.fzillo.services.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.fzillo.services.serializer.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;


public class SoUser {
    //TODO getters/setters?
    //TODO public/private

    @JsonProperty("user_id")
    public Integer user_id;

    @JsonProperty("display_name")
    public String display_name;

    @JsonProperty("creation_date")
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    public LocalDateTime creation_date_stackoverflow; //TODO

}
