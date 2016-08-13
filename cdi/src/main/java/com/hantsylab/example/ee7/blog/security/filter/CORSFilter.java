/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hantsylab.example.ee7.blog.security.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author hantsy
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    private final static Logger LOG = Logger.getLogger(CORSFilter.class.getName());

    final static String DEFAULT_ALLOW_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    final static String DEFAULT_ALLOW_HEADERS = "origin,content-type,accept,authorization";
    final static int MAX_AGE = 24 * 60 * 60;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOG.log(Level.FINEST, "...entering CORSFilter.");

        if (isPreflightRequest(requestContext)) {
            LOG.log(Level.FINEST, "...handling preflight request.");
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");

            responseContext.getHeaders().add("Access-Control-Allow-Headers", createRequestedHeaders(requestContext));
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

            responseContext.getHeaders().add("Access-Control-Max-Age", MAX_AGE);
            responseContext.getHeaders().add("Access-Control-Allow-Methods", DEFAULT_ALLOW_METHODS);
        }

    }

    private boolean isPreflightRequest(ContainerRequestContext requestContext) {
        return requestContext.getHeaderString("Origin") != null && HttpMethod.OPTIONS.equals(requestContext.getMethod());
    }

    private String createRequestedHeaders(ContainerRequestContext requestContext) {
        String headers = requestContext.getHeaderString("Access-Control-Request-Headers");
        return headers != null ? headers : DEFAULT_ALLOW_HEADERS;
    }

//    private String createRequestedMethods(ContainerRequestContext requestContext) {
//         String method = requestContext.getHeaderString("Access-Control-Request-Method");
//         return DEFAULT_ALLOW_METHODS;
//    }
}
