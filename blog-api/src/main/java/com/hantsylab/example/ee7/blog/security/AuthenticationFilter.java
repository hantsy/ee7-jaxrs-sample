package com.hantsylab.example.ee7.blog.security;

import java.io.IOException;
import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    @AuthenticatedUser
    Event<String> userAuthenticatedEvent;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the HTTP Authorization header from the request
        String authorizationHeader
            = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly 
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {

            // Validate the token
            validateToken(token);

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws Exception {
        // Check if it was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        
        ///userAuthenticatedEvent.fire(username);
    }

    //@Context
    // SecurityContext securityContext;
//    Principal principal = securityContext.getUserPrincipal();
//    String username = principal.getName();
//    private void setupRequestConetextPrinciple(ContainerRequestContext requestContext) {
//
//        requestContext.setSecurityContext(new SecurityContext() {
//
//            @Override
//            public Principal getUserPrincipal() {
//
//                return new Principal() {
//
//                    @Override
//                    public String getName() {
//                        return username;
//                    }
//                };
//            }
//
//            @Override
//            public boolean isUserInRole(String role) {
//                return true;
//            }
//
//            @Override
//            public boolean isSecure() {
//                return false;
//            }
//
//            @Override
//            public String getAuthenticationScheme() {
//                return null;
//            }
//        });
//    }
}
