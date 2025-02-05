# Scalable Messaging Platform

## Overview

Scalable Messaging Platform is a high-performance, distributed messaging system designed to handle millions of concurrent users efficiently. The system utilizes **Redis Streams** for real-time message delivery and **HBase** for persistent message storage, ensuring high availability and durability. The project is built using **Spring WebFlux** and **Spring Cloud Gateway** to enable a fully reactive, scalable architecture.

## Features

- **Real-time Messaging**: Low-latency message delivery using **Redis Streams**
- **Persistent Storage**: Message history stored in **HBase** for efficient retrieval
- **Reactive Architecture**: Fully non-blocking, event-driven design with **Spring WebFlux**
- **API Gateway**: **Spring Cloud Gateway** for secure and scalable API routing
- **Monitoring & Auto-scaling**: Integrated with **Prometheus**, **Grafana**, and **Kubernetes**

## Architecture

```
┌──────────────┐      ┌──────────────────────┐      ┌───────────────────┐
│  Client App  │ ---> │  API Gateway (SCG)   │ ---> │  Message Producer │
└──────────────┘      └──────────────────────┘      └───────────────────┘
                                                              │
                                                       (Redis Streams)
                                                              │
                                                     ┌──────────────────┐
                                                     │ Message Consumer │
                                                     └──────────────────┘
                                                              │
                                                           (HBase)
                                                              │
                                                     ┌─────────────────┐
                                                     │  Query Service  │
                                                     └─────────────────┘
```

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.x, Spring WebFlux, Spring Cloud Gateway
- **Messaging**: Redis Streams, Kafka (optional)
- **Storage**: HBase (for persistent message storage)
- **Infrastructure**: Docker, Kubernetes, Prometheus, Grafana

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 17 & Gradle

### Setup

Clone the repository:

```sh
git clone <MY_REPO_GIT>
cd scalable-messaging-platform
```

Start infrastructure services:

```sh
docker-compose up -d
```

Run the backend services:

```sh
cd backend/producer-service
./gradlew bootRun
```

## Future Enhancements

- Implement **WebSockets** for real-time message updates
- Introduce **Kafka** for high-throughput message streaming
- Optimize HBase queries for better performance
