package com.smartcampus.smartcampusapi.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.smartcampusapi.datastore.DataStore;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Sensor not found with ID: " + sensorId + "\"}").build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());

        return Response.ok(sensorReadings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Sensor not found with ID: " + sensorId + "\"}").build();
        }

        if ("MAINTENANCE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is currently under MAINTENANCE " +
                            "and cannot accept new readings. " +
                            "Please wait until it is restored to ACTIVE status.");
        }

        if ("OFFLINE".equals(sensor.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is OFFLINE " + "and cannot record new readings.");
        }

        if (reading == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Request body is required.\"}")
                    .build();
        }

        SensorReading newReading = new SensorReading(reading.getValue());
        DataStore.readings.get(sensorId).add(newReading);
        sensor.setCurrentValue(newReading.getValue());

        URI location = UriBuilder.fromUri("http://localhost:8080/SmartCampusAPI/api/v1/" +
                "sensors/{sensorId}/readings/{readingId}").build(sensorId, newReading.getId());

        return Response.created(location).entity(newReading).build();
    }

}
