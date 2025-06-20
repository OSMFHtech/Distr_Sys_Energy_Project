## Project Important Steps & Setup Guide

This guide walks through the final assembly of your **Distributed Energy System**: wiring all six Spring Boot microservices, RabbitMQ, SQL initialization, Docker Compose, and running in IntelliJ (no errors).

---

### 1. Folder Structure

Your top‐level `DistributedEnergyProject/` should contain exactly:

```
DistributedEnergyProject/
├── docker-compose.yml         # Or in root
├── EnergyProducerService/     # port 8081
├── EnergyUserService/         # port 8082
├── EnergyUsageService/        # port 8083
├── EnergyPercentageService/   # port 8084
├── EnergyAPI/                 # port 8080
└── EnergyGUI/                 # port 8085
```

Each of the six folders is an independent IntelliJ project (or module). When opening in IntelliJ:
1. **File → Open**  
2. Select the service folder (e.g. `EnergyProducerService`)  
3. Repeat in new window for each service

---

### 2. RabbitMQ Broker

Add this service to your `docker-compose.yml` above the app services:

```yaml
services:
  rabbitmq:
    image: rabbitmq:3.9-management
    hostname: rabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"
```

All microservices use these Spring properties (in `application.properties` for each service):

```properties
spring.rabbitmq.host=rabbitmq
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

Go to http://localhost:15672 (guest/guest) to confirm the exchange/queues appear.

---

### 3. SQL Schema Initialization

Although JPA auto‐creates tables, you can add explicit SQL under `src/main/resources/`:

- **schema.sql**
  ```sql
  CREATE TABLE production_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP,
    produced_kw INT
  );
  ```

- **data.sql** (optional initial seed)
  ```sql
  INSERT INTO production_records (timestamp, produced_kw)
  VALUES (CURRENT_TIMESTAMP, 0);
  ```

Similarly for `user_profiles` and `usage_records`:

```sql
CREATE TABLE user_profiles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255)
);

CREATE TABLE usage_records (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  timestamp TIMESTAMP,
  used_kw INT
);
```

Place these files alongside `application.properties` for each service.

---

### 4. Docker Compose

Use the `docker-compose.yml` at the root to wire all services including RabbitMQ:

```bash
docker-compose up --build
```

Services:
- RabbitMQ UI: http://localhost:15672  
- Producer API: http://localhost:8081/producer/current  
- User API: http://localhost:8082/user/{id}  
- Usage API: http://localhost:8083/usage/{userId}  
- Percentage API: http://localhost:8084/percentage/{userId}  
- EnergyAPI: http://localhost:8080/energy/current  
- EnergyGUI: http://localhost:8085/dashboard

---

### 5. Message Flow

1. **Producer** publishes events to `energy-exchange` with `producer.new`.  
2. **User** publishes profiles with `user.new`.  
3. **Usage** publishes usage with `usage.new`.  
4. **Percentage** aggregates via Feign or listening to events.  
5. **EnergyAPI** aggregates REST endpoints: `/producer/current`, `/user/{id}`, `/usage/{id}`, `/percentage/{id}` → `/energy/current`.  
6. **EnergyGUI** refreshes by calling EnergyAPI.

---

### 6. Running in IntelliJ

For each service:
1. Open the folder in IntelliJ.  
2. Wait for Maven import.  
3. Ensure `application.properties` and optional SQL files are in `src/main/resources`.  
4. Run the main application class.  
5. Check console logs for correct port.  
6. (Optional) H2 console: `http://localhost:808X/h2-console`  

---

### 7. Verification

- Test via `curl` as shown above.  
- Validate GUI displays the aggregated data correctly.

With these steps and the updated code for all six components, your distributed system will run seamlessly. Good luck!
