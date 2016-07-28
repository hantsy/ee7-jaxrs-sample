package com.hantsylab.example.ee7.blog.security.filter;

import com.hantsylab.example.ee7.blog.domain.model.Role;
import com.hantsylab.example.ee7.blog.security.Secured;
import com.hantsylab.example.ee7.blog.security.UserPrincipal;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles);
            } else {
                checkPermissions(methodRoles);
            }

        } catch (Exception e) {
            requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    // Extract the roles from the annotated element
    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                Role[] allowedRoles = secured.value();
                return Arrays.asList(allowedRoles);
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles) throws Exception {
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
        // get user info from SecurityContext directly.

        Principal principal = securityContext.getUserPrincipal();

        if (principal != null && principal instanceof UserPrincipal) {
            UserPrincipal up = (UserPrincipal) principal;

            List<Role> userRoles = up.getRoles();

            boolean allowed = false;
            for (Role r : userRoles) {
                for (Role checkRole : allowedRoles) {
                    if (r.equals(checkRole)) {
                        allowed = true;
                    }
                }
            }

            if (!allowed) {
                throw new ForbiddenException("roles:"+userRoles.toString()+" is not allowed");
            }
        }

    }
}
