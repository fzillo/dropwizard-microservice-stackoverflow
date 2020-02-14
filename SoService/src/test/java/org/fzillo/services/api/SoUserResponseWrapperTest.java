package org.fzillo.services.api;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SoUserResponseWrapperTest {

    @Test
    public void getUser_returnUser() {
        SoUserResponseWrapper soUserResponseWrapper = new SoUserResponseWrapper();

        List<SoUser> userList = new ArrayList<>();
        SoUser soUser = new SoUser(1, "TestUser", LocalDateTime.now());
        userList.add(soUser);

        soUserResponseWrapper.userList = userList;

        assertNotNull(soUserResponseWrapper.getUser());
    }

    @Test
    public void getUser_returnNull() {
        SoUserResponseWrapper soUserResponseWrapper = new SoUserResponseWrapper();

        assertNull(soUserResponseWrapper.getUser());
    }
}