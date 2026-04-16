package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS (Java EE 8) for the application.
 * Versioned entry point established at /api/v1.
 */

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

}
