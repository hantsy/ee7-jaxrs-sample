package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.service.PasswordMismatchedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class PasswordMismatchedExceptionMapper implements ExceptionMapper<PasswordMismatchedException> {

    @Override
    public Response toResponse(PasswordMismatchedException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(exception.getMessage())
            .build();
    }

}
