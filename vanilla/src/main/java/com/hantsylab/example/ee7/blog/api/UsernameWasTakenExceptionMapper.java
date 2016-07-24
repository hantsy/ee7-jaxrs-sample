package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.service.UsernameWasTakenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class UsernameWasTakenExceptionMapper implements ExceptionMapper<UsernameWasTakenException> {

    @Override
    public Response toResponse(UsernameWasTakenException exception) {
        return Response.status(Response.Status.CONFLICT)
            .entity(exception.getMessage())
            .build();
    }

}
