package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.service.BlogService;
import com.hantsylab.example.ee7.blog.service.PostDetail;
import com.hantsylab.example.ee7.blog.service.PostForm;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.Validator;
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
@Path("posts")
public class PostResource {

    private static final Logger LOG = Logger.getLogger(PostResource.class.getName());

    @Inject
    private BlogService service;

    @Context
    UriInfo uriInfo;

    @Inject
    Validator validator;

    @GET()
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("q") String keyworkd) {
        return Response.ok(service.findByKeyword(keyworkd)).build();
    }

    @GET
    @Path("{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        return Response.ok(service.findPostById(id)).build();
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response save(@Valid PostForm post) {
        //validatePost(post);
        PostDetail saved = service.createPost(post);
        return Response.created(uriInfo.getBaseUriBuilder().path("posts/{id}").build(saved.getId())).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id,
        @Valid PostForm post) {
        //validatePost(post);
        PostDetail saved = service.updatePost(id, post);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        service.deletePostById(id);
        return Response.noContent().build();
    }

//    private void validatePost(PostForm post) {
//        try {
//            validator.validate(post);
//        } catch (ConstraintViolationException e) {
//            LOG.log(Level.SEVERE, "exception @{0}", e.getMessage());
//            throw new InvalidFormDataException();
//        }
//    }

}
