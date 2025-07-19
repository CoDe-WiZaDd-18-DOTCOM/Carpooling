# Carpooling System – Technical \& Functional Documentation

## Overview

The Carpooling System is a robust full-stack web application tailored for **university or city ride-sharing**. It enables users to offer or join shared rides, facilitates live location sharing and emergency alerts, and provides comprehensive admin controls along with a ratings and review ecosystem.

## Tech Stack

| Layer | Technologies Used                                                 |
| :-- |:------------------------------------------------------------------|
| **Frontend** | ReactJS, Tailwind CSS, Axios                                      |
| **Backend** | Spring Boot (Java), Spring Data MongoDB, <br>Spring Security, JWT |
| **Database** | MongoDB (NoSQL)                                                   |
| **Queue** |  RabbitMQ (email/event dispatch)                                  |
| **Other APIs** | Nominatim (location search \& geolocation)                        |
| **Auth** | JWT-based custom authentication                                   |
| **Hosting** | Localhost development; production-ready codebase                  |

## User Roles

- **Passenger:** Can search for rides, make booking requests, review drivers.
- **Driver:** Can offer rides, approve/reject bookings, and view routes.
- **Admin:** Manages users, rides, and bookings via a protected dashboard.


## Key Features

### 1. Ride Management

- **Ride Creation:**
  Drivers enter:
    - Source \& destination (location search)
    - Intermediate stops
    - Arrival times (`LocalTime`)
    - Seat capacity \& available seats
- **Storage:**
  Each route stop is modeled:

```java
public class RouteStop {
    private Location location;
    private LocalTime arrivalTime;
}
```

- **Viewing \& Deletion:**
    - Drivers see their rides at `/rides/me`.
    - Full ride details: `/rides/ride/{id}`.
    - Deletion/completion via frontend; on completion, ride is marked `CLOSED` and new bookings are disabled.


### 2. Booking System

- **Booking Request:**
  Passengers select pickup/drop points from the ride route.
  Backend validations:
    - Seat availability
    - No duplicate requests
    - Bookings stored with `PENDING`, `APPROVED`, or `REJECTED` status
- **Approval \& Cancellation:**
    - Drivers manage requests at `/bookings/incoming`.
    - Approval updates seat counts.
    - Both parties can cancel; before approval, cancelled seats return to availability.
- **Booking View:**
    - `/bookings/me`: Current user’s bookings
    - `/bookings/by-ride/{id}`: All bookings for a specific ride


### 3. Admin Dashboard

- **Endpoints:**
    - `/admin/users`: View all users, roles, profiles
    - `/admin/rides`: View all rides
- **Permissions:**
    - Delete users or rides
    - View any ride or booking in detail
    - Restricted to `ADMIN` role via backend security


### 4. Rating \& Review System

- **Eligibility:**
  After ride is `CLOSED`, passengers are prompted to rate and comment on the driver.

- **Submission Flow:**
    - Review shown if ride is closed \& not already reviewed
    - Backend POST `/reviews/check` verifies status
    - Booking marked as rated after submission


### 5. SOS Emergency Alert System

- **Emergency Messages:**
    - Available during rides
    - Geolocation automatically attached
- **Backend:**
    - Alerts stored in `sos_alerts` collection
    - Email sent to city authority (via `sos_authorities` collection)
    - `SosAuthorityService` handles city-email mapping


### 6. Live Location Sharing

- **Feature:**
  Passengers can share live location to driver/authority during a ride.
- **API:**
    - `POST /share-location/{bookingId}`
    - Uses Geolocation API
    - Location is stored temporarily and viewable in logs/alerts


### 7. JWT Authentication

- **Design:**
    - Custom JWT for login
    - JWT info (role, email) stored in `users_jwt` collection and in `localStorage`
- **Role-Based Access:**
    - Features for drivers, passengers, and admins are gated by token info
    - Token validated via a Spring Security filter


### 8. Data Models (MongoDB)

| Collection           | Purpose |
|:---------------------|:--------|
| `user`               | JWT-authenticated users |
| `rides`              | Rides offered (driver) |
| `bookings`           | Booking requests |
| `reviews`            | User ratings, comments |
| `sos_alerts`         | Emergency alerts/messages |
| `sos_authorities`    | Authority info per city |
| `failed_emails`      | Logs of email send failures with retry tracking |
| `banned_list`        | List of banned users by admin |
| `analytical_summary` | Aggregated stats and insights (e.g., rides count, user activity) |

### 9. Pagination \& Filtering

- Pagination and filters for rides and users listings (`Pageable` in Spring Boot, query params in frontend).


### 10. Email Notification 

- RabbitMQ to decouple email notifications (e.g., on SOS event)
- `EmailEvent` published to a queue; async consumer dispatches the email
- Non-blocking; enhances scalability/fault tolerance


### 11. Location Autocomplete

- **Frontend:** Nominatim OpenStreetMap API powers all location search fields in real time.
- **Backend:**

```java
public class Location {
    private String label;
    private double latitude;
    private double longitude;
}
```

## 🧠 Ride Matching Algorithm

This algorithm is responsible for determining whether a ride is a valid match for a user's booking request based on pickup and drop location order in the ride’s route, and optionally, the timing.

---

### 🧾 Data Models

#### `RouteStop`
Represents a stop in a ride's route.
- `String location`: Name of the stop.
- `LocalTime arrivalTime`: Time at which the ride is expected to reach the location.

#### `Ride`
Represents a ride created by the driver.
- `List<RouteStop> route`: Ordered list of all locations (including pickup and drop) the ride will pass through.

#### `BookingRequest`
Represents a booking made by a user.
- `String pickupLocation`: Requested pickup location.
- `String dropLocation`: Requested drop location.
- `LocalTime requestedTime`: Time the user wishes to be picked up.

---

## 🐇 RabbitMQ Setup and Email Retry Architecture

### 🧱 Queues Used

- **emailQueue**: Main queue to receive email sending jobs.
- **emailQueue.dlq**: Dead Letter Queue where failed messages from `emailQueue` are routed.
- **emailQueue.dlq.retry**: Retry queue to reattempt failed messages after TTL.


### 🔁 Message Lifecycle

1. ✅ Email job published to `emailQueue`
2. 📧 Consumer listens to `emailQueue` and tries to send email
3. ❌ On failure → message moved to `emailQueue.dlq` via `x-dead-letter-exchange`
4. ⏱ TTL applied on `emailQueue.dlq` (2 minutes)
5. 🔁 After TTL expires → message forwarded to `emailQueue.dlq.retry`
6. 📬 Consumer retries sending email from `emailQueue.dlq.retry`
7. ❌ If fails again → saved to `failed_emails` MongoDB collection
---

## Security Highlights

- All protected endpoints require JWT tokens (validated in `JwtFilter.java`)
- Strict **role-based access control**:
    - `/admin/**`: Admin only
    - Ride creation/completion: Drivers only
    - Booking requests: Passengers only
- Auth headers required for all protected APIs

## 🆕 Environment Setup (.env Required)

```env
MONGO_URI=your_mongodb_connection_string
MONGO_URI_TEST=your_test_db_connection_string
SECRET_KEY=your_jwt_secret_key
EMAIL=your_email_address
PASSWORD=your_email_password_or_app_password
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
REDIS_PASSWORD=your_redis_password
GOOGLE_CLIENT_ID=your_google_oauth_client_id
GOOGLE_CLIENT_SECRET=your_google_oauth_client_secret
RABBITMQ_URI=your_amqp_uri_with_credentials
```
---
## Future Features (Ideas \& Roadmap)

- Full-featured pagination, filtering, sorting
- Notification system (email/SMS)
- OTP verification for bookings
- Smart route matching (ML) for optimal ride pooling
- Improved ride/review history visibility

---
## Summary

This Carpooling System is a **modular, scalable ride-sharing platform** featuring:

- Fine-grained, role-based access
- Smooth booking with approval flow
- Reviews and feedback on rides
- Real-time SOS and live location
- Powerful admin dashboard
- Modern, decoupled architecture

Built with Spring Boot, MongoDB, and ReactJS, it’s ready for production and further extensibility.

