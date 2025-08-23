# 🚗 Carpooling Connect

> A full-stack web application for **city ride-sharing** featuring secure bookings, live tracking, emergency support, and robust admin tools.
> Frontend part - https://github.com/CoDe-WiZaDd-18-DOTCOM/Carpooling-Frontend
> Live Link - https://carpoolingconnect.vercel.app/

## 📚 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [User Roles](#-user-roles)
- [Key Features](#-key-features)
  - [Ride Management](#ride-management)
  - [Booking System](#booking-system)
  - [Admin Dashboard](#admin-dashboard)
  - [Rating \& Review System](#rating--review-system)
  - [SOS Emergency Alert System](#sos-emergency-alert-system)
  - [Live Location Sharing](#live-location-sharing)
  - [JWT Authentication](#jwt-authentication)
- [Ride Matching Algorithm](#-ride-matching-algorithm)
- [RabbitMQ Setup and Email Retry Architecture](#-rabbitmq-setup-and-email-retry-architecture)
- [Security Highlights](#-security-highlights)
- [Environment Setup](#-environment-setup-env-required)
- [Data Models](#8-data-models-mongodb)
- [AI Chatbot Integration](#ai-chatbot-integration)
- [Summary](#-summary)


## 📖 Overview

The Carpooling System is a robust full-stack web application tailored for **city ride-sharing**. It enables users to offer or join shared rides, facilitates live location sharing and emergency alerts, and provides comprehensive admin controls along with a ratings and review ecosystem.

## ⚙️ Tech Stack

| Layer | Technologies Used |
| :-- | :-- |
| **Frontend** | ReactJS, Tailwind CSS, Axios |
| **Backend** | Spring Boot (Java), Spring Data MongoDB, Spring Security, JWT |
| **Database** | MongoDB (NoSQL) |
| **Queue** | RabbitMQ (email/event dispatch) |
| **Other APIs** | Nominatim (location search \& geolocation) |
| **Auth** | JWT-based custom authentication |
| **Hosting** | Localhost development; production-ready codebase |

## 👥 User Roles

- **Passenger:** Search for rides, book, review drivers.
- **Driver:** Offer rides, approve/reject bookings, manage routes.
- **Admin:** Manage users, rides, and bookings via protected dashboard.


## 🚀 Key Features

### Ride Management

- Source \& destination (location search)
- Intermediate stops with arrival times (`LocalTime`)
- Seat capacity and available seats
- Endpoint Examples:
  - `/rides/me` — Driver’s rides
  - `/rides/ride/{id}` — Ride details
- Deletion or completion marks ride as CLOSED


### Booking System

- Passengers select pickup/drop from the route
- Backend checks:
  - Seat availability
  - Prevents duplicate requests
- Booking Status:
  - `PENDING`, `APPROVED`, `REJECTED`
- Endpoints:
  - `/bookings/me` — Passenger’s bookings
  - `/bookings/incoming` — Approvals queue for drivers
  - `/bookings/by-ride/{id}` — Bookings for a ride


### Admin Dashboard

- Endpoints:
  - `/admin/users` — Manage users
  - `/admin/rides` — Manage rides
- Role-restricted (ADMIN only)


### Rating \& Review System

- One review per ride/booking after completion
- Eligible verification via `/reviews/check`


### SOS Emergency Alert System

- SOS button during rides; attaches current location
- Notifies city authority from `sos_authorities` collection
- Persistent storage in `sos_alerts`


### Live Location Sharing

- Real-time location updates for ride-in-progress
- Endpoint: `POST /share-location/{bookingId}`


### JWT Authentication

- JWT stored in localStorage
- Role-based access: ADMIN, DRIVER, PASSENGER
- Spring Security filter for token verification


## 🧠 Ride Matching Algorithm

Determines valid rides based on ordered route stops and pickup/drop points.

**Key Checks:**

- Pickup must precede drop in the stop list
- Optionally match by `arrivalTime`

```java
public class RouteStop {
    private Location location;
    private LocalTime arrivalTime;
}
```

- **Data Models:**
  - `RouteStop`: String location, LocalTime arrivalTime
  - `Ride`: List<RouteStop> route
  - `BookingRequest`: String pickupLocation, String dropLocation, LocalTime requestedTime
- **Logic:** Pickup index < drop index in route; optional time proximity check


## 🐇 RabbitMQ Setup and Email Retry Architecture

**Queues Used:**

- `emailQueue` — Main queue
- `emailQueue.dlq` — Dead Letter Queue
- `emailQueue.dlq.retry` — For post-TTL retry

**Lifecycle:**

1. Email job sent to `emailQueue`
2. On failure: Moves to `emailQueue.dlq`
3. After 2-min TTL: Routed to `emailQueue.dlq.retry`
4. On final failure: Persisted in `failed_emails`

## 🔐 Security Highlights

- All APIs protected via JWT
- Spring Security enforces token \& role checks
- Admin endpoints restricted to `ADMIN`
- Optimistic locking via

```java
@Version
private Long version;
```

- 409 Conflict returned for concurrent updates (clients must refresh)


## 🛠️ Environment Setup (.env Required)

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


## 🧬 Data Models (MongoDB)

| Collection | Purpose |
| :-- | :-- |
| `user` | JWT-authenticated users |
| `rides` | Rides offered (driver) |
| `bookings` | Booking requests |
| `reviews` | Ratings \& comments |
| `sos_alerts` | Emergency alerts/messages |
| `sos_authorities` | Authority info by city |
| `failed_emails` | Logs of email send failures |
| `banned_list` | Admin-banned users |
| `analytical_summary` | Aggregated stats \& insights |

## 🧠 AI Chatbot Integration

- **Endpoint:** `/public/chat-bot`
- Powered by Gemini 2.5 Flash (Google AI API)
- Prompts sourced from `ai-context.txt`
- Context cached on startup for fast response


## ✅ Summary

A modular, scalable ride-sharing platform with:

- Role-based access for driver, passenger, and admin
- Real-time SOS alerts \& live location tracking
- Smooth booking, reviewing, and management workflows
- Robust email queuing via RabbitMQ with retry mechanism
- Optimistic locking for consistency
- Smart AI-powered chat assistance
- Tech: Spring Boot, MongoDB, ReactJS, RabbitMQ
