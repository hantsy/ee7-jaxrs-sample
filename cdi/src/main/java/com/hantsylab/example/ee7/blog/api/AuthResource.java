package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.service.Credentials;
import com.hantsylab.example.ee7.blog.service.SignupForm;
import com.hantsylab.example.ee7.blog.service.UserService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author hantsy
 */
@RequestScoped
@Path("auth")
public class AuthResource {

    private static final Logger LOG = Logger.getLogger(AuthResource.class.getName());

    @Inject
    private UserService service;

    @Context
    UriInfo uriInfo;

    @POST
    @Path("login")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response login(@Valid Credentials form) {
        LOG.log(Level.INFO, "login as@{0}", form);
        return Response.ok(service.authenticate(form)).build();
    }

    @POST
    @Path("signup")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response signup(@Valid SignupForm form) {
        LOG.log(Level.INFO, "signup with data@{0}", form);
        return Response.ok(service.registerUser(form)).build();
    }

    @POST
    @Path("logout")
    public Response logout() {
        LOG.info("logging out...");
        return Response.ok().build();
    }

}
