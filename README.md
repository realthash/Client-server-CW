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
7. [Conceptual Report  -  Question Answers](#conceptual-report--question-answers)
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
    "message": "Room 'LIB-301' cannot be deleted because it still has 1 sensor(s) assigned to it."
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
    "message": "Sensor 'TEMP-002' is currently under MAINTENANCE and cannot accept new readings."
}
```

---

## Conceptual Report  - Question Answers

---

### Part 1.1  - JAX-RS Resource Lifecycle & In-Memory Data Management

**Question:** Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures to prevent data loss or race conditions.

By default, JAX-RS follows a **per-request lifecycle** for resource classes. This means the runtime  - in this case Jersey running on Apache Tomcat  - instantiates a brand new object of every `@Path`-annotated class each time an HTTP request arrives, and discards that object immediately after the response is sent. This is in direct contrast to the Singleton pattern, where a single shared instance would be reused across all requests.

This design decision has a critical consequence for in-memory data management. Because each resource instance is freshly created and then thrown away, any data stored as an **instance field** (a regular non-static field) would be lost the moment the request ends. For example, if `RoomResource` declared `private Map<String, Room> rooms = new HashMap<>()` as an instance variable, every POST request would store a room into a fresh, empty map that gets immediately discarded  - making the data completely invisible to the next request.

To prevent this data loss, all shared state in this implementation is stored in the `DataStore` class using **static fields**. Static fields belong to the class itself rather than to any particular instance, meaning they are initialised once when the JVM first loads the class and persist for the entire lifetime of the application  - shared across every resource instance and every request.

However, this introduces a second problem: **race conditions**. In a web server environment, multiple HTTP requests can arrive simultaneously and be processed by different threads at the exact same moment. Java's standard `HashMap` is not thread-safe  - if two threads attempt to write to it concurrently, the internal structure of the map can become corrupted, leading to lost data or unpredictable behaviour. To address this, this implementation uses `ConcurrentHashMap` for all three data stores (rooms, sensors, and readings). `ConcurrentHashMap` is specifically designed for concurrent access  - it uses internal segmented locking to allow multiple threads to read and write safely without the risk of data corruption, without requiring the developer to write any explicit synchronisation code.

---

### Part 1.2  - HATEOAS and Hypermedia-Driven Design

**Question:** Why is the provision of Hypermedia (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS  - Hypermedia As The Engine Of Application State  - represents the highest level of REST maturity, as defined by Leonard Richardson's REST Maturity Model (Level 3). Rather than treating an API as a simple collection of fixed URLs that clients must know in advance, HATEOAS transforms the API into a **self-describing, navigable system**. Each response contains not only the requested data but also the links and actions available to the client from that point, effectively guiding the client through the application's state machine.

In this implementation, the discovery endpoint at `GET /api/v1` returns a structured JSON object that includes a `resources` map  - directly embedding the URIs of all primary collections such as `/api/v1/rooms` and `/api/v1/sensors`. A completely new client can connect to this single entry point and discover the entire API surface without consulting any external document.

This approach offers three concrete advantages over static documentation. First, it provides **resilience to change**  - if a resource path is restructured in a future version of the API, any client that navigates from the discovery endpoint will automatically follow the updated links, whereas a client that has hardcoded URLs from a static document will break silently. Second, it **reduces coupling** between the client and the server  - the client depends only on the entry point URL, not on a full knowledge of the API's internal structure. Third, it **improves developer experience**  - a new developer integrating with the API can explore it interactively from the root endpoint, much like browsing a website by following links, rather than reading through lengthy documentation before writing a single line of code.

By contrast, static documentation such as a PDF or a wiki page becomes stale the moment the API changes, requires manual maintenance, and cannot be consumed programmatically by client applications. HATEOAS eliminates this problem by making the API itself the authoritative source of navigational truth.

---

### Part 2.1  - Returning IDs vs Full Objects in Collection Responses

**Question:** When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client-side processing.

Returning only IDs from `GET /api/v1/rooms` minimises the size of each response payload, which reduces network bandwidth consumption  - a significant advantage when the collection contains thousands of rooms. However, this forces the client to make a separate HTTP request for each room it needs details about, a pattern known as the N+1 problem: one request to get the list of IDs, then N additional requests to fetch each room individually. This increases latency and places more load on both the network and the server.

Returning full room objects in the list response eliminates the N+1 problem entirely  - the client receives everything it needs in a single round trip, which reduces latency and simplifies client-side code. The trade-off is a larger payload per response. In this implementation, returning full objects is the correct choice because each Room object is lightweight (four fields), the performance cost of the larger payload is negligible, and it significantly improves the experience for API consumers. In a production system with very large or deeply nested objects, a hybrid approach  - returning a summary object with key fields rather than either extreme  - would be the industry standard.

---

### Part 2.2  - DELETE Idempotency

**Question:** Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

In this implementation, the DELETE operation is **effectively idempotent** in terms of server state, though it returns different HTTP status codes across repeated calls. On the first call, if the room exists and has no sensors, it is removed from the data store and the server returns `204 No Content`. On every subsequent call with the same room ID, the room no longer exists in the map, so the server returns `404 Not Found`.

The key point is that idempotency is defined by the **effect on server state**, not by the HTTP status code returned. After the first successful DELETE, the server state is: "this room does not exist." Every subsequent DELETE call leaves the server in exactly the same state  - the room still does not exist. No additional side effects occur. By this definition, the operation is idempotent.

The HTTP specification (RFC 7231) supports this interpretation  - it states that idempotent methods may return different responses on repeated calls, as long as the intended effect on the server is the same. Returning `404` on subsequent calls is therefore both correct and consistent with the REST specification. This behaviour protects against accidental duplicate requests from network retries without causing any unintended data modification.

---

### Part 3.1  - @Consumes and Content-Type Mismatches

**Question:** We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?

The `@Consumes(MediaType.APPLICATION_JSON)` annotation declares a contract between the server and its clients  - it tells JAX-RS that the POST endpoint will only accept request bodies formatted as `application/json`. When a client sends a request with a different `Content-Type` header, such as `text/plain` or `application/xml`, JAX-RS intercepts the mismatch before the request even reaches the resource method.

Specifically, JAX-RS returns an automatic `415 Unsupported Media Type` response. This happens at the framework level  - the `createSensor()` method is never invoked. Jersey compares the `Content-Type` header of the incoming request against the media types declared in `@Consumes`, and if no match is found, it rejects the request immediately. This is a protective behaviour  - it prevents malformed or unexpected data formats from reaching business logic and potentially causing a `NullPointerException` or data corruption when Jackson tries to deserialise a non-JSON body.

This design promotes strict interface contracts in the API, which is an important principle of robust RESTful service design. Clients are informed clearly and early about the expected format, rather than receiving a cryptic internal server error.

---

### Part 3.2  - @QueryParam vs @PathParam for Filtering

**Question:** You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path. Why is the query parameter approach generally considered superior for filtering and searching collections?

`@PathParam` extracts a value that is embedded directly within the URL path structure, such as `/api/v1/sensors/TEMP-001`, where `TEMP-001` identifies a specific, unique resource. `@QueryParam` extracts optional key-value pairs appended after a `?` in the URL, such as `/api/v1/sensors?type=CO2`, which modifies how a collection is returned without changing which resource is being addressed.

The query parameter approach is superior for filtering for three reasons. First, it correctly models the semantics of the operation  - `/api/v1/sensors` always refers to the sensors collection as a whole, and the `?type=CO2` qualifier is a search instruction applied to that collection, not a different resource. Using a path segment like `/api/v1/sensors/type/CO2` incorrectly implies that `type` and `CO2` are sub-resources with their own identity, which violates REST's resource-oriented design. Second, query parameters are inherently optional  - the `GET /api/v1/sensors` endpoint works correctly whether `?type=` is provided or not, with no change to the URL structure or method signature. A path-based design would require a separate route for the unfiltered case. Third, query parameters compose naturally  - a future requirement to filter by both type and status could be expressed cleanly as `?type=CO2&status=ACTIVE`, whereas nesting this in the path would produce an unwieldy and confusing URL such as `/api/v1/sensors/type/CO2/status/ACTIVE`.

---

### Part 4.1  - Sub-Resource Locator Pattern

**Question:** Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path in one massive controller class?

The Sub-Resource Locator pattern is a JAX-RS mechanism that delegates the handling of nested resource paths to dedicated classes, rather than accumulating all endpoint logic inside a single controller. In this implementation, `SensorResource` handles the `/api/v1/sensors` collection, and when a request arrives for `/api/v1/sensors/{sensorId}/readings`, it delegates to a separate `SensorReadingResource` instance via a locator method that carries no HTTP verb annotation.

The primary architectural benefit is the **Single Responsibility Principle**  - each class has one clearly defined concern. `SensorResource` manages the sensor collection lifecycle (creation, retrieval, filtering). `SensorReadingResource` manages the historical data for one specific sensor. Neither class needs to know the internal implementation details of the other.

In a large, real-world API this separation becomes critical for maintainability. If every nested path were defined in one massive controller class, that class would grow to hundreds of methods, making it difficult to navigate, test, and modify without introducing regressions. Separate resource classes can be developed, tested, and deployed independently. They can also be assigned to different developers without causing merge conflicts. Furthermore, the locator method itself can contain guard logic  - verifying the sensor exists before handing off to the sub-resource  - which keeps validation concerns cleanly separated from data management concerns.

---

### Part 5.1  - Why 422 is More Semantically Accurate Than 404

**Question:** Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?

HTTP 404 Not Found communicates that the resource identified by the **request URL** could not be located on the server. When a client sends `POST /api/v1/sensors`, the URL is entirely valid  - the sensors collection exists and is reachable. The problem lies not with the URL but with the content of the JSON body: the `roomId` field references a room that does not exist. The URL was found; the semantic content of the payload was not processable.

HTTP 422 Unprocessable Entity was designed precisely for this scenario. It communicates that the server understood the request format (valid JSON, correct Content-Type), located the resource (valid URL), but could not process the instruction because of a logical or referential error within the payload itself. Using 422 gives the client far more precise diagnostic information  - it knows immediately that the problem is inside the request body, specifically a broken reference, rather than assuming it made a typo in the URL. This precision reduces debugging time and makes the API significantly more developer-friendly.

---

### Part 5.2a  - Cybersecurity Risks of Exposing Stack Traces

**Question:** From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?

Exposing a raw Java stack trace in an HTTP response is a serious information disclosure vulnerability. An attacker gains several categories of sensitive intelligence from a single stack trace.

First, **internal file paths**  - the trace reveals the exact directory structure of the server (e.g. `/opt/tomcat9/webapps/SmartCampusAPI/WEB-INF/classes/com/smartcampus/...`), which tells an attacker precisely where files are stored and how the application is organised. Second, **library names and versions**  - the trace exposes every third-party dependency in the call stack (e.g. `org.glassfish.jersey 2.39.1`, `com.fasterxml.jackson 2.15`), allowing the attacker to look up known CVEs (Common Vulnerabilities and Exposures) for those exact versions and craft targeted exploits. Third, **internal logic and code structure**  - the method names, class names, and line numbers in the trace reveal the application's internal architecture, making it significantly easier to identify attack surfaces, find unprotected methods, or understand how business rules are enforced. Fourth, **data patterns**  - exception messages often contain actual data values (e.g. `NullPointerException on field roomId`) which can expose field names, data types, and internal naming conventions.

The `GlobalExceptionMapper` in this implementation eliminates all of these risks by intercepting every unhandled exception, logging the full technical detail server-side for developers, and returning only a generic, uninformative message to the client.

---

### Part 5.2b  - Why Filters Are Superior to Per-Method Logging

**Question:** Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?

Manually inserting `Logger.info()` statements inside every resource method violates the **DRY principle** (Don't Repeat Yourself) and creates a serious maintenance burden. With ten or more endpoints, logging must be added to each method individually, and every new endpoint added in the future requires the developer to remember to add it again. If logging requirements change  - for example, adding a correlation ID or a timestamp format  - every single method must be updated.

A JAX-RS filter implements logging as a **cross-cutting concern**  - a behaviour that applies uniformly across the entire application from a single point of definition. `ApiLoggingFilter` is registered once via `@Provider` and automatically intercepts every request and response without any modification to the resource classes themselves. This means resource classes remain focused purely on business logic, with no logging code polluting them. The filter is also the correct architectural layer for this concern  - it operates at the HTTP boundary, capturing the raw method, URI, and status code before and after the business logic runs, which is exactly the information needed for API observability and debugging. This pattern is an application of **Aspect-Oriented Programming (AOP)** principles, where cross-cutting concerns are separated from core business logic to improve modularity, readability, and maintainability.

---
