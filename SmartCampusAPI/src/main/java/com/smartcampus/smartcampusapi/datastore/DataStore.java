package com.smartcampus.smartcampusapi.datastore;

import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    /*
     * Static initialiser block — this runs ONCE when the class is first
     * loaded by the JVM. It pre-populates the store with sample data so
     * your API is not completely empty when the marker tests it for the
     * first time. This is important for your video demo too.
     */
    static {
        // Create two sample rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Science Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        // Create one sample sensor linked to r1
        Sensor s1 = new Sensor("TEMP-001", "temperature", "ACTIVE", 22.5, "LIB-301");
        sensors.put(s1.getId(), s1);

        // Tell room r1 it owns this sensor
        r1.getSensorIds().add(s1.getId());

        // Create an empty readings list for this sensor so it's ready
        readings.put("TEMP-001", new ArrayList<>());
    }
}
