# 📡 Telecom Billing System (In Progress)

> 🚧 **This project is currently under active development.**  
> All services are being built and tested iteratively by the contributors.

A microservices-based telecom billing system that simulates a real-time CDR pipeline, applies business rules, calculates billing, handles user payments, and supports notification and audit logging — with full observability and scalability.

---

**Authors:<br>
_Srivathsa_ <br>
_Akanksha_**

## 🧩 Project Architecture

```text
                     [Customer Call/Event]  
                              ⬇️
                  [CDR Generator Service (A)]
                              ⬇️ Kafka Topic: cdr.raw
                  [Enrichment Service (A)]
                              ⬇️ Kafka Topic: cdr.enriched
     ┌-------------------------┌-------------------------------------------┌-
     ▼                         ▼                                           ▼
[Rating + Billing (B)]   [Fraud Detection Service (B)]              [Audit Logger (A)]
     ▼                                                                     ▼
Kafka Topic: billing.status / alerts                            Kafka Topic: audit.logs
     ▼
[User Service (A)]
     ▼
[Payment Service (A)]
     ▼
[Notification Service (A)]
     ▼
[Client UI / External Access (B)]

Monitoring:
  └── Prometheus, Grafana, ELK Stack
  └── All services expose metrics/logs to these tools

Monitoring:
  └── Prometheus, Grafana, ELK Stack
  └── All services expose metrics/logs to these tools

---

## 🧩 Microservices Breakdown

| Service                     | Owner         | Description                                                  |
|-----------------------------|---------------|--------------------------------------------------------------|
| `cdr-generator`             | Srivathsa (A) | Simulates call data records using Faker.                     |
| `cdr-enrichment`            | Srivathsa (A) | Enriches raw CDRs with user, plan, and metadata.             |
| `rating-billing`            | Akanksha (B)  | Applies rules, rates calls, generates billing info.          |
| `fraud-detection`           | Akanksha (B)  | Identifies suspicious patterns in CDRs.                      |
| `audit-logger`              | Srivathsa (A) | Logs activity for compliance/debug.                          |
| `user-service`              | Srivathsa (A) | Manages telecom users, plan linking, etc.                    |
| `payment-service`           | Srivathsa (A) | Handles balances, payments, and recharges.                   |
| `notification-service`      | Srivathsa (A) | Sends alerts via email/SMS based on topics.                  |
| `client-ui / api-gateway`   | Akanksha (B)  | Exposes REST APIs to external apps or admin dashboards.      |

---

## ⚙️ Tech Stack

- **Backend**: Java 17, Spring Boot, Spring Kafka
- **Messaging**: Kafka (Confluent), Avro + Schema Registry
- **Monitoring**: Prometheus + Grafana
- **Logging**: ELK Stack (Elastic, Logstash, Kibana)
- **Deployment**: Docker & Docker Compose
- **Configuration**: Spring Cloud Config
- **Service Discovery**: Eureka (Optional in future)

---

## 🗃️ Kafka Topics Used

- `cdr.raw`
- `cdr.enriched`
- `billing.status`
- `fraud.alerts`
- `audit.logs`
- `payment.status`
- `notification.outbox`

---
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Docker & Docker Compose
- Git
- Maven/Gradle
- Confluent Kafka (via Docker)

### Setup Instructions

1. Clone the repo  
   ```bash
   git clone https://github.com/srivathsa49/telecomProject.git
   cd telecomProject```

```bash docker-compose -f docker-compose.yml up -d```


### Build and Run Each Service:
```bash
cd <service-name>
./gradlew build
java -jar build/libs/<jar-file>.jar```
