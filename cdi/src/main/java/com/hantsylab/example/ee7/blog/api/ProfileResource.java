package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.security.AuthenticatedUser;
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.PasswordForm;
import com.hantsylab.example.ee7.blog.service.ProfileForm;
import com.hantsylab.example.ee7.blog.service.UserService;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author hantsy
 */
@RequestScoped
@Path("me")
@Secured({Role.USER, Role.ADMIN})
public class ProfileResource {

    private static final Logger LOG = Logger.getLogger(ProfileResource.class.getName());

    @Inject
    @AuthenticatedUser
    private User me;

    @Inject
    UserService userService;

    @Inject
    BlogService blogService;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("profile")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response profileInfo() {
        LOG.info("get profile...");
        return Response.ok(this.userService.findUserById(this.me.getId())).build();
    }

    @GET
    @Path("posts")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response allOfMyPosts() {
        LOG.info("get my posts...");
        return Response.ok(this.blogService.findByUsername(this.me.getUsername())).build();
    }

    @PUT
    @Path("profile")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response updateProfile(@Valid ProfileForm form) {
        LOG.info("updating profile...");
        this.userService.updateProfile(this.me.getId(), form);
        return Response.noContent().build();
    }

    @PUT
    @Path("password")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response updatePassword(@Valid PasswordForm form) {
        LOG.info("updating password...");
        this.userService.updatePassword(this.me.getId(), form);
        return Response.noContent().build();
    }

}
