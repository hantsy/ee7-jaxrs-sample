package com.hantsylab.example.ee7.blog.api;

import com.hantsylab.example.ee7.blog.service.AuthenticationException;
import com.hantsylab.example.ee7.blog.service.UsernameWasTakenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {

    @Override
    public Response toResponse(AuthenticationException exception) {
        return Response.status(Response.Status.UNAUTHORIZED)
            .entity(exception.getMessage())
            .build();
    }

}
