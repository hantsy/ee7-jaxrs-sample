package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.CommentDetail;
import com.hantsylab.example.ee7.blog.service.CommentForm;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("comments")
@Secured({Role.USER, Role.ADMIN})
public class CommentResource {

    private static final Logger LOG = Logger.getLogger(CommentResource.class.getName());

    @Inject
    private BlogService service;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        return Response.ok(service.findCommentById(id)).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id,
        @Valid CommentForm form) {
        CommentDetail saved = service.updateComment(id, form);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        service.deleteCommentById(id);
        return Response.noContent().build();
    }

}
