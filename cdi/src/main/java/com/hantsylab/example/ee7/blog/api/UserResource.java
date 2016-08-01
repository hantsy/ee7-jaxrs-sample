package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.service.UserDetail;
import com.hantsylab.example.ee7.blog.service.UserForm;
import com.hantsylab.example.ee7.blog.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author hantsy
 */
@RequestScoped
@Path("users")
@Secured({Role.ADMIN})
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UserService service;

    @Context
    UriInfo uriInfo;

    @GET()
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("q") String keyworkd) {
        return Response.ok(service.findByKeyword(keyworkd)).build();
    }

    @GET
    @Path("exists")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("username") String username) {
        boolean exists = service.usernameExists(username);
        Map<String, Boolean> result = new HashMap<>();
        result.put("result", exists);
        return Response.ok(result).build();
    }

    @GET
    @Path("{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        return Response.ok(service.findUserById(id)).build();
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response save(@Valid UserForm user) {
        UserDetail saved = service.createUser(user);
        return Response.created(uriInfo.getBaseUriBuilder().path("users/{id}").build(saved.getId())).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id,
        @Valid UserForm user) {
        UserDetail saved = service.updateUser(id, user);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        service.deleteUserById(id);
        return Response.noContent().build();
    }

}
