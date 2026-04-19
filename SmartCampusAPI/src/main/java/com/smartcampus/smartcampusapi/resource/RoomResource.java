package com.smartcampus.smartcampusapi.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.smartcampus.smartcampusapi.datastore.DataStore;
import com.smartcampus.smartcampusapi.model.Room;

/**
 * JAX-RS Resource class for managing Room entities.
 * Base path is /room, and it handles JSON requests and responses.
 */
@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class RoomResource {

    /**
     * Retrieves all rooms currently stored in the system.
     * 
     * @return A list of all Room objects.
     */
    @GET
    public Response getAllRooms() {

        List<Room> allRooms = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(allRooms).build();
    }

    /**
     * Retrieves a specific room by its unique ID.
     * 
     * @param roomId The unique identifier of the room.
     * @return The Room object if found, otherwise a 404 Not Found response.
     */
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        // Look up the room in the DataStore map using the provided ID
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found with ID: " + roomId + "\"}").build();

        }
        return Response.ok(roomId).build();
    }

    /**
     * Creates a new room and adds it to the system.
     * Validates that the Room ID and Name are provided and unique.
     * 
     * @param room The Room object to be created.
     * @return The created Room object with a 201 Created status and Location
     *         header.
     */
    @POST
    public Response createRoom(Room room) {

        room = new Room();

        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Room ID is required.\"}").build();
        }
        if (room.getName() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Room name is required.\"}")
                    .build();
        }

        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Room ID is already exist\"}")
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);

        URI location = UriBuilder
                .fromUri("http://localhost:8080/SmartCampusAPI/api/v1/rooms/{id}")
                .build(room.getId());
        return Response.created(location).entity(room).build();
    }
}
