package org.fzillo.services.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SoUserResponseWrapper {
    //TODO public/private

    @JsonProperty("items")
    public List<SoUser> userList;   //TODO private

    public SoUser getUser(){
        if (userList != null && !userList.isEmpty()){
            return userList.get(0);
        }

        return null;
    }

}
