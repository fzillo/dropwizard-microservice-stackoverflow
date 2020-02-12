package org.fzillo.services.resources;

import com.codahale.metrics.annotation.Timed;
import org.fzillo.services.api.SoUserResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class SoUsersResource {

    private final Client client;

    private static final Logger log = LoggerFactory.getLogger(SoUsersResource.class);

    public SoUsersResource(Client client) {
        //TODO log?
        this.client = client;
    }


    @GET
    @Timed
    @Path("/byId/{id}")
    //TODO validate query param - NotNull?
    public Response getUserById(@NotNull @PathParam("id") Integer  id) {
        //TODO cofeine cache

        SoUserResponseWrapper userResponseWrapper = client.target("https://api.stackexchange.com").path("/2.2/users/" + id) //
                .queryParam("site","stackoverflow") //
                .request(MediaType.APPLICATION_JSON).get(SoUserResponseWrapper.class);

        if (userResponseWrapper.getUser() != null) {
            return Response.status(Response.Status.OK).entity(userResponseWrapper.getUser()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
