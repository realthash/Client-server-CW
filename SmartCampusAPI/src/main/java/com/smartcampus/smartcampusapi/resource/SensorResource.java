package com.smartcampus.smartcampusapi.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.smartcampusapi.datastore.DataStore;
import com.smartcampus.smartcampusapi.model.Sensor;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class SensorResource {
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {

        List<Sensor> allSensors = new ArrayList<>(DataStore.sensors.values());

        if (type == null || type.trim().isEmpty()) {
            return Response.ok(allSensors).build();
        }

        List<Sensor> filtered = allSensors.stream()
                .filter(sensor -> sensor.getType().toLowerCase().contains(type.trim().toLowerCase()))
                .collect(Collectors.toList());

        return Response.ok(filtered).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String status = sensor.getStatus();
        if (sensor.getStatus() == null || (!status.equals("ACTIVE") && !status.equals("OFFLINE")
                && !status.equals("MAINTENANCE"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Status must be ACTIVE, MAINTENANCE, or OFFLINE.\"}").build();
        }

        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"A sensor with ID '" + sensor.getId() + "' already exists.\"}").build();
        }

        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Cannot register sensor: the roomId '" + sensor.getRoomId() +
                    "' does not refer to any existing room in the system.");
        }

        DataStore.sensors.put(sensor.getId(), sensor);
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        DataStore.readings.put(sensor.getId(), new ArrayList<>());

        URI location = UriBuilder
                .fromUri("http://localhost:8080/SmartCampusAPI/api/v1/sensors/{id}")
                .build(sensor.getId());

        return Response
                .created(location)
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Sensor not found with ID: " + sensorId + "\"}")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {

        return new SensorReadingResource(sensorId);
    }

}