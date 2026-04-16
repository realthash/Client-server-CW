package com.smartcampus.smartcampusapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * A sample resource using JAX-RS (Java EE 8) annotations.
 */
@Path("/start")
public class SmartCampusResource {

    @GET
    @Path("ping")
    public Response ping() {
        return Response
                .ok("ping Smart Campus (Java EE 8)")
                .build();
    }
}
