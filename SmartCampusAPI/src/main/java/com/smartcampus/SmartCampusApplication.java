package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This activates JAX-RS and sets the base URL for all endpoints.
 *
 * @ApplicationPath("/api/v1") means every endpoint you write
 * will be reachable at: http://localhost:8080/SmartCampusAPI/api/v1/...
 *
 * The full breakdown:
 * http://localhost:8080 → Tomcat's address
 * /SmartCampusAPI → the name of your WAR file (Tomcat sets this)
 * /api/v1 → this annotation
 * /rooms → your @Path on RoomResource
 */

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

}
