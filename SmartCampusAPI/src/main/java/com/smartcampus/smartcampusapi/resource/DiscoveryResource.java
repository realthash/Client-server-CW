package com.smartcampus.smartcampusapi.resource;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Response discover() {

        /*
         * Map<String, Object> (not Map<String, String>)
         * because one of our values (the "resources" field) is
         * itself a nested map, not a simple string.
         */
        Map<String, Object> response = new HashMap<>();

        // API versioning info
        response.put("name", "Smart Campus Sensor & Room Management API");
        response.put("version", "1.0");
        response.put("status", "running");

        // Administrative contact — use your own details here
        Map<String, String> contact = new HashMap<>();
        contact.put("developer", "Thashmika Sajan");
        contact.put("email", "w2152918@westminster.ac.uk");
        contact.put("university", "University of Westminster");
        response.put("contact", contact);

        /*
         * A client can call GET /api/v1, read this map, and
         * immediately know where to send requests
         */
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        return Response.ok(response).build();
    }
}
