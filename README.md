# Smart Campus Sensor & Room Management API

**Module:** 5COSC022W  - Client-Server Architectures  
**University:** University of Westminster  
**Student:** Thashmika Rathnayake 
**Student ID:** w2152918

---

## Table of Contents

1. [API Overview](#api-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [How to Build and Run](#how-to-build-and-run)
   - [Option A  -  NetBeans (Recommended)](#option-a--netbeans-recommended)
   - [Option B  -  Command Line Terminal](#option-b--command-line-terminal)
5. [API Endpoints Reference](#api-endpoints-reference)
6. [Sample curl Commands](#sample-curl-commands)
7. [Conceptual Report - Question Answers](#conceptual-report---question-answers)
---

## API Overview

This project implements a RESTful web service for the University of Westminster's **Smart Campus** initiative. The API provides a backend system for managing campus **Rooms** and the **Sensors** deployed within them (e.g. temperature monitors, CO2 detectors, occupancy trackers).

The system is designed as a high-performance web service using **JAX-RS (Jersey 2.41)** deployed on **Apache Tomcat 9**. It follows REST architectural principles including proper HTTP status codes, resource-based URL design, and HATEOAS-driven discovery.

### Key design decisions

- All data is stored **in-memory** using `ConcurrentHashMap` and `ArrayList`  - no database is used.
- A `ConcurrentHashMap` is used instead of `HashMap` to ensure **thread-safety** under concurrent HTTP requests.
- Resources follow a **logical hierarchy**: Rooms contain Sensors, and Sensors contain Readings.
- The API is **leak-proof**  - no raw Java stack traces are ever exposed to consumers.
- Every request and response is **logged** via a JAX-RS filter for full observability.

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 11 |
| Framework | JAX-RS (Jersey 2.41) |
| Server | Apache Tomcat 9 |
| Build tool | Apache Maven |
| JSON serialisation | Jackson (via jersey-media-json-jackson) |
| Packaging | WAR (Web Application Archive) |

---

## Project Structure

```
SmartCampusAPI/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/smartcampus/
        │       ├── SmartCampusApplication.java       ← JAX-RS entry point
        │       ├── datastore/
        │       │   └── DataStore.java                ← In-memory storage
        │       ├── model/
        │       │   ├── Room.java
        │       │   ├── Sensor.java
        │       │   └── SensorReading.java
        │       ├── resource/
        │       │   ├── DiscoveryResource.java         ← GET /api/v1
        │       │   ├── RoomResource.java              ← /api/v1/rooms
        │       │   ├── SensorResource.java            ← /api/v1/sensors
        │       │   └── SensorReadingResource.java     ← /api/v1/sensors/{id}/readings
        │       ├── exception/
        │       │   ├── ErrorResponse.java
        │       │   ├── RoomNotEmptyException.java
        │       │   ├── LinkedResourceNotFoundException.java
        │       │   ├── SensorUnavailableException.java
        │       │   └── mapper/
        │       │       ├── RoomNotEmptyExceptionMapper.java      ← 409
        │       │       ├── LinkedResourceNotFoundExceptionMapper ← 422
        │       │       ├── SensorUnavailableExceptionMapper.java ← 403
        │       │       └── GlobalExceptionMapper.java            ← 500
        │       └── filter/
        │           └── ApiLoggingFilter.java
        └── webapp/
            └── WEB-INF/
                └── web.xml
```

---
 
## How to Build and Run
 
### Prerequisites  -  install these before starting
 
| Tool | Version | How to verify |
|------|---------|---------------|
| Java JDK | 11 or 17 | Open a terminal and run `java -version` |
| Apache Maven | 3.6 or above | Run `mvn -version` |
| Apache Tomcat | **9.x only** | Download from https://tomcat.apache.org/download-90.cgi |
| NetBeans IDE | 19 or above | Download from https://netbeans.apache.org |
 
> **Critical:** Use Tomcat 9 specifically  -  not Tomcat 10 or above. Tomcat 10+ uses the `jakarta.*` namespace which is incompatible with this project's `javax.*` imports and will cause a deployment failure.
 
---
 
### Option A  -  NetBeans (Recommended)
 
This is the simplest method. NetBeans handles the build and deployment automatically  -  no terminal commands needed.
 
#### Step 1  -  Download the project from GitHub
 
1. Go to the GitHub repository: `https://github.com/realthash/Client-server-CW.git`
2. Click the green **"Code"** button near the top right
3. Click **"Download ZIP"**
4. Once downloaded, unzip the file to a location you can find easily
   - Example: `C:\Projects\SmartCampusAPI`
#### Step 2  -  Open the project in NetBeans
 
1. Launch NetBeans
2. Go to **File → Open Project** from the top menu
3. In the file browser, navigate to the folder where you unzipped the project
4. Click on the `SmartCampusAPI` folder  -  NetBeans will recognise it as a Maven project automatically and show the Maven project icon next to it
5. Click **Open Project**
6. Wait for NetBeans to finish scanning and indexing  -  you will see a progress bar at the bottom
#### Step 3  -  Add Tomcat 9 to NetBeans (first time only)
 
> Skip this step if you have already added Tomcat 9 to NetBeans.
 
1. Go to **Tools → Servers** from the top menu
2. Click **"Add Server..."**
3. Select **"Apache Tomcat or TomEE"** from the list → click **Next**
4. Click **Browse** and navigate to your Tomcat 9 installation folder
   - Example: `C:\tomcat9`
5. Click **Next** → **Finish**
6. Click **Close**
#### Step 4  -  Build the project
 
1. In the Projects panel on the left side, right-click the `SmartCampusAPI` project
2. Click **"Clean and Build"**
3. Watch the Output window at the bottom of the screen
4. Wait until you see this line:
   ```
   BUILD SUCCESS
   ```
5. The WAR file has been created at `target/SmartCampusAPI.war` inside your project folder


#### Step 5  -  Run on Tomcat
 
1. Right-click the `SmartCampusAPI` project in the Projects panel
2. Click **"Run"**
3. If a dialog appears asking you to choose a server, select **Apache Tomcat 9**
4. NetBeans will deploy the WAR to Tomcat and start the server automatically
5. Watch the Output window  -  when you see this line the server is ready:
   ```
   INFO: Server startup in [X] ms
   ```
 
#### Step 6  -  Verify the API is running
 
Open Postman and send:
```
GET http://localhost:8080/SmartCampusAPI/api/v1
```
 
You will receive a JSON discovery response. The API is live.
 
#### To stop the server
 
Go to **Window → Services** in the NetBeans menu bar. Expand **Servers**, right-click **Apache Tomcat 9**, and click **Stop**.
 
---
 
### Option B  -  Command Line Terminal
 
Use this method if you prefer working in a terminal without NetBeans.
 
#### Step 1  -  Clone the repository
 
```bash
git clone https://github.com/YOUR_USERNAME/SmartCampusAPI.git
cd SmartCampusAPI
```
 
#### Step 2  -  Build the WAR file
 
```bash
mvn clean package
```
 
Wait for `BUILD SUCCESS`. The WAR file is produced at `target/SmartCampusAPI.war`.
 
#### Step 3  -  Deploy to Tomcat
 
```bash
copy target\SmartCampusAPI.war C:\tomcat9\webapps\
```
 
#### Step 4  -  Start Tomcat
 
```bash
C:\tomcat9\bin\startup.bat
```
 
Tomcat detects the new WAR and deploys it within a few seconds. A folder named `SmartCampusAPI` will appear inside `webapps/`  -  this confirms successful deployment.
 
#### Step 5  -  Verify the API is running
 
```
GET http://localhost:8080/SmartCampusAPI/api/v1
```
 
#### To stop the server
 
`C:\tomcat9\bin\shutdown.bat`
 
---
 
### Checking request and response logs
 
`ApiLoggingFilter` automatically logs every request and response. No configuration is needed  -  it registers itself via `@Provider`. View the logs at:
 
```
C:\tomcat9\logs\catalina.out
```
 
Every API call produces a pair of lines:
```
INFO: --> Incoming Request : [GET] http://localhost:8080/SmartCampusAPI/api/v1/rooms
INFO: <-- Outgoing Response: [GET] http://localhost:8080/SmartCampusAPI/api/v1/rooms → Status: 200
```
 
---

## API Endpoints Reference

**Base URL:** `http://localhost:8080/SmartCampusAPI/api/v1`

### Discovery

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/` | API discovery  - returns metadata and resource links | 200 OK |

### Rooms

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/rooms` | Get all rooms | 200 OK |
| POST | `/rooms` | Create a new room | 201 Created + Location header |
| GET | `/rooms/{roomId}` | Get a specific room by ID | 200 OK / 404 |
| DELETE | `/rooms/{roomId}` | Delete a room (blocked if sensors exist) | 204 No Content / 409 |

### Sensors

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/sensors` | Get all sensors (optional `?type=` filter) | 200 OK |
| POST | `/sensors` | Register a new sensor (validates roomId) | 201 Created / 422 |
| GET | `/sensors/{sensorId}` | Get a specific sensor by ID | 200 OK / 404 |

### Sensor Readings

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/sensors/{sensorId}/readings` | Get full reading history | 200 OK |
| POST | `/sensors/{sensorId}/readings` | Post a new reading (updates currentValue) | 201 Created / 403 |

### Error responses

| Status | Scenario |
|--------|----------|
| 400 Bad Request | Missing or invalid required fields |
| 403 Forbidden | Posting a reading to a MAINTENANCE or OFFLINE sensor |
| 404 Not Found | Resource ID does not exist |
| 409 Conflict | Deleting a room that still has sensors |
| 422 Unprocessable Entity | Sensor registration with a non-existent roomId |
| 500 Internal Server Error | Any unexpected runtime error (clean JSON, no stack trace) |

---

## Sample curl Commands

> All commands assume the server is running at `http://localhost:8080/SmartCampusAPI/api/v1`

### 1  - Discover the API entry point

``` 
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1 \
  -H "Accept: application/json"
```

Expected response:
```json
{
    "name": "Smart Campus Sensor & Room Management API",
    "version": "1.0",
    "status": "running",
    "contact": {
        "developer": "Thashmika Rathnayake",
        "email": "w2152918@westminster.ac.uk",
        "university": "University of Westminster"
    },
    "resources": {
        "rooms": "/api/v1/rooms",
        "sensors": "/api/v1/sensors"
    }
}
```

---

### 2  - Create a new room (POST)

``` 
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{
    "id": "HALL-001",
    "name": "Main Hall",
    "capacity": 200
  }'
```

Expected response: `201 Created` with a `Location` header pointing to `/api/v1/rooms/HALL-001` and the created room object in the body.

---

### 3  - Get all sensors filtered by type

``` 
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=temperature" \
  -H "Accept: application/json"
```

Expected response: a JSON array containing only sensors whose type contains "temperature" (case-insensitive).

---

### 4  - Post a sensor reading and verify currentValue updates

``` 
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{ "value": 26.3 }'
```

Expected response: `201 Created` with the new reading including auto-generated `id` and `timestamp`.

Then verify the parent sensor's `currentValue` updated:

``` 
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001 \
  -H "Accept: application/json"
```

The `currentValue` field should now show `26.3`.

---

### 5  - Attempt to delete a room with sensors (trigger 409)

``` 
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301 \
  -H "Accept: application/json"
```

Expected response:
```json
{
    "status": 409,
    "error": "Conflict",
    "message": "Room 'LIB-301' cannot be deleted because it still has 1 sensor(s) assigned to it. Please remove all sensors before decommissioning this room."
}
```

---

### 6  - Register a sensor with a non-existent roomId (trigger 422)

``` 
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEST-999",
    "type": "CO2",
    "status": "ACTIVE",
    "currentValue": 400.0,
    "roomId": "FAKE-999"
  }'
```

Expected response:
```json
{
    "status": 422,
    "error": "Unprocessable Entity",
    "message": "Cannot register sensor: the roomId 'FAKE-999' does not refer to any existing room in the system."
}
```

---

### 7  - Post a reading to a MAINTENANCE sensor (trigger 403)

First, register a sensor in MAINTENANCE status:

``` 
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{
    "id": "TEMP-002",
    "type": "temperature",
    "status": "MAINTENANCE",
    "currentValue": 0.0,
    "roomId": "LAB-101"
  }'
```

Then attempt to post a reading to it:

``` 
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-002/readings \
  -H "Content-Type: application/json" \
  -d '{ "value": 19.5 }'
```

Expected response:
```json
{
    "status": 403,
    "error": "Forbidden",
    "message": "Sensor 'TEMP-002' is currently under MAINTENANCE and cannot accept new readings. Please wait until it is restored to ACTIVE status."
}
```

---

## Conceptual Report - Question Answers

---

### Part 1.1 - JAX-RS Resource Lifecycle & In-Memory Data Management

**Question:** Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures to prevent data loss or race conditions.

The JAX-RS follows a per-request lifecycle for resource classes. This means the runtime - in this case Jersey running on Apache Tomcat - instantiates a brand new object of every `@Path`-annotated class each time an HTTP request arrives, and discards that object immediately after the response is sent. This is in direct contrast to the Singleton pattern, where a single shared instance is reused across all requests.

This has a critical consequence for in-memory data management. Any data stored as an instance field would be lost the moment the request ends. If `RoomResource` declared `private Map<String, Room> rooms = new HashMap<>()` as an instance variable, every POST request would store a room into a fresh, empty map that gets immediately discarded - making the data invisible to every subsequent request.

To prevent this, all shared state is stored in the `DataStore` class using static fields. Static fields belong to the class itself, not to any instance - they are initialised once when the JVM first loads the class and persist for the entire application lifetime, shared across every resource instance and every request.

However, static shared state introduces race conditions. Multiple HTTP requests can arrive simultaneously and be processed by different threads at the same moment. Java's standard `HashMap` is not thread-safe - two threads writing to it concurrently can silently corrupt the data structure. To address this, all three data stores use `ConcurrentHashMap`, which uses internal segmented locking to allow safe concurrent access without requiring any explicit synchronisation code from the developer.

---

### Part 1.2  - HATEOAS and Hypermedia-Driven Design

**Question:** Why is the provision of Hypermedia (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS - Hypermedia As The Engine Of Application State - represents the highest level of REST maturity as defined by Leonard Richardson's REST Maturity Model (Level 3). Rather than treating an API as a collection of fixed URLs clients must memorise, HATEOAS transforms the API into a self-describing, navigable system where each response contains the links and actions available from that point in the application state.
In this implementation, GET /api/v1 returns a resources map directly embedding the URIs of all primary collections. A new client can connect to this single entry point and discover the entire API surface without any prior knowledge or external documentation.

This offers three concrete advantages over static documentation. First, resilience to change - if a resource path changes in a future version, clients navigating from the discovery endpoint follow the updated link automatically, whereas clients with hardcoded URLs break silently. Second, reduced coupling - the client depends only on the entry point URL, not on a complete knowledge of the API's internal structure. Third, improved developer experience - a new developer can explore the API interactively by following links, exactly like browsing a website, rather than reading documentation before writing any code. Static documents become stale immediately after any API change; HATEOAS makes the API itself the authoritative and always-accurate source of navigational truth.


---

### Part 2.1  - Returning IDs vs Full Objects in Collection Responses

**Question:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Returning only IDs minimises payload size and reduces bandwidth - an advantage at scale with thousands of rooms. However, it forces the client to make a separate HTTP request for each room's details, creating the N+1 problem: one request for the ID list plus N additional requests for individual rooms. This multiplies latency and server load significantly.

Returning full room objects eliminates the N+1 problem - the client receives everything it needs in a single round trip, reducing latency and simplifying client code considerably. The trade-off is a larger per-response payload. In this implementation, returning full objects is the correct choice because each Room has only four lightweight fields, making the bandwidth cost negligible while the latency saving is meaningful. In production systems with deeply nested or large objects, a hybrid approach - returning summary objects with key fields only - represents the industry standard.


---

### Part 2.2  - DELETE Idempotency

**Question:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

The DELETE operation in this implementation is effectively idempotent in terms of server state, though it returns different status codes on repeated calls. On the first call, if the room exists with no sensors assigned, it is removed and the server returns 204 No Content. On every subsequent identical call, the room no longer exists in the map, so the server returns 404 Not Found.

Idempotency is defined by the effect on server state, not by the HTTP status code returned. After the first DELETE, the server state is: "this room does not exist." Every subsequent call leaves the server in exactly that same state - no further side effects occur. By this definition the operation is fully idempotent.

RFC 7231 explicitly supports this interpretation - idempotent methods may return different responses on repeated calls as long as the intended server-side effect is the same. Returning 404 on subsequent calls is correct and consistent with the REST specification, protecting against duplicate network retries without causing any unintended data modification.

---

### Part 3.1  - @Consumes and Content-Type Mismatches

**Question:** We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

@Consumes(MediaType.APPLICATION_JSON) declares a strict interface contract: this endpoint only accepts request bodies with Content-Type: application/json. When a client sends a different header such as text/plain or application/xml, JAX-RS intercepts the mismatch at the framework level - before the request ever reaches the resource method.

Jersey automatically returns 415 Unsupported Media Type. The createSensor() method is never invoked. Jersey compares the incoming Content-Type against the declared @Consumes value, and if no match is found, it rejects the request immediately. This is a protective behaviour - it prevents malformed data from reaching business logic and causing a NullPointerException or silent corruption when Jackson attempts to deserialise a non-JSON body.

This strict contract is a core principle of robust API design: clients receive a clear, specific, early diagnostic rather than a cryptic internal server error later in the processing chain.


---

### Part 3.2  - @QueryParam vs @PathParam for Filtering

**Question:** You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path. Why is the query parameter approach generally considered superior for filtering and searching collections?

@PathParam embeds a value directly in the URL path structure - for example /api/v1/sensors/TEMP-001 - identifying a specific, unique resource. @QueryParam appends optional key value pairs after ? - for example ?type=CO2 - modifying how a collection is returned without changing which resource is being addressed.

The query parameter approach is superior for three reasons. First, semantic correctness - /api/v1/sensors always refers to the full sensors collection; ?type=CO2 is a search instruction applied to that collection, not a different resource. A path-based design like /sensors/type/CO2 incorrectly implies type and CO2 have their own resource identity, violating REST's resource-oriented design principles. Second, optional by nature - the endpoint works identically with or without the parameter, requiring no additional routes for the unfiltered case. Third, natural composability - future requirements like filtering by type and status simultaneously are expressed cleanly as ?type=CO2&status=ACTIVE, whereas nested path segments produce unreadable URLs and require separate route definitions for every parameter combination.


---

### Part 4.1  - Sub-Resource Locator Pattern

**Question:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

The Sub-Resource Locator pattern delegates handling of nested resource paths to dedicated classes, rather than accumulating all logic in a single controller. In SensorResource, a method annotated with only @Path("/{sensorId}/readings") - no HTTP verb - returns a SensorReadingResource instance. JAX-RS then resolves the incoming HTTP method against that returned object to determine which method to invoke.

The primary benefit is the Single Responsibility Principle: SensorResource manages the sensor collection lifecycle - creation, retrieval, and filtering. SensorReadingResource manages historical readings for one specific sensor. Neither class knows the other's internal details. In a large API, a single monolithic controller grows to hundreds of methods - increasingly difficult to navigate, test, and modify without introducing regressions. Separate resource classes can be developed, tested, and assigned to different developers independently, with no merge conflicts. The locator method can also contain guard logic - verifying the sensor exists before delegating - cleanly separating validation from data management in a way that a flat controller structure cannot.


---

### Part 5.1  - Why 422 is More Semantically Accurate Than 404

**Question:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP 404 communicates that the resource identified by the request URL was not found on the server. When a client sends POST /api/v1/sensors, the URL is entirely valid - the sensors collection exists and is reachable. The problem is not with the URL but with the content of the JSON body: the roomId field references a room that does not exist. The URL was found; the payload's semantic content could not be processed.

HTTP 422 Unprocessable Entity was designed precisely for this scenario - the server understood the request format (valid JSON, correct Content-Type header), located the resource at the URL (valid endpoint), but could not execute the instruction due to a logical error within the payload itself. Using 422 gives the client immediate, precise diagnostic information: the problem is a broken reference inside the request body, not a URL typo. This specificity directly reduces debugging time and makes the API significantly more developer-friendly than a vague 404 pointing to the wrong problem.

---

### Part 5.2a  - Cybersecurity Risks of Exposing Stack Traces

**Question:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing a raw Java stack trace in an HTTP response is a serious information disclosure vulnerability. An attacker gains four categories of sensitive intelligence from a single trace. First, internal file paths - the exact server directory structure is revealed (e.g. /opt/tomcat9/webapps/SmartCampusAPI/WEB-INF/classes/com/smartcampus/...), showing precisely where code is stored on disk. Second, library names and versions - every dependency in the call stack is exposed (e.g. org.glassfish.jersey 2.41), enabling the attacker to search known CVE databases for those exact versions and craft targeted exploits. Third, internal logic and code structure - method names, class names, and line numbers map the application's architecture, revealing attack surfaces and unprotected entry points. Fourth, data patterns - exception messages often contain field names and actual values (e.g. NullPointerException on field roomId), exposing internal naming conventions and data types.

The GlobalExceptionMapper<Throwable> in this implementation eliminates all these risks: it intercepts every unhandled exception, logs the full technical detail to the server-side log file for developers, and returns only a generic, uninformative message to the client.


---

### Part 5.2b  - Why Filters Are Superior to Per-Method Logging

**Question:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Manually inserting Logger.info() into every resource method violates the DRY principle (Don't Repeat Yourself) and creates a serious maintenance burden. With ten or more endpoints, logging code must be added individually to each method. Any new endpoint added in the future requires the developer to remember to include logging again. If logging requirements change - for example, adding a request correlation ID or changing the timestamp format - every single method across every resource class must be updated individually.

A JAX-RS filter implements logging as a cross-cutting concern defined in one single place. ApiLoggingFilter is registered once via @Provider and automatically intercepts every request and response across the entire API without any modification to resource classes. Resource classes remain focused purely on business logic - no logging code pollutes them. The filter operates at the correct architectural layer - the HTTP boundary - capturing method, URI, and status code before and after business logic executes. This is a direct application of Aspect-Oriented Programming (AOP) principles: cross-cutting concerns are separated from core business logic, producing a codebase that is more modular, more readable, and significantly easier to maintain long-term.

---
