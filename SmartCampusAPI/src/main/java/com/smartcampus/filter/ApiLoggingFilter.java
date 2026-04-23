package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class ApiLoggingFilter
        implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(ApiLoggingFilter.class.getName());

    /*
     * Called BEFORE the request reaches your resource method.
     * Logs the HTTP method (GET, POST, DELETE...) and the
     * full URI that was requested.
     */
    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        LOGGER.info(String.format(
                "--> Incoming Request: [%s] %s",
                requestContext.getMethod(), // e.g. GET, POST
                requestContext.getUriInfo()
                        .getRequestUri() // e.g. http://localhost:8080/.../rooms
        ));
    }

    /*
     * Called AFTER your resource method has returned a Response.
     * Logs the HTTP status code that is about to be sent back.
     *
     * Notice the method signature is different — it receives
     * BOTH contexts: the original request and the response.
     * This lets you log both in the same message if needed.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
            throws IOException {

        LOGGER.info(String.format(
                "<-- Outgoing Response: [%s] %s → Status: %d",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri(),
                responseContext.getStatus() // e.g. 200, 201, 404, 409
        ));
    }
}
