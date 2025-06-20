# Distributed Energy Project

This repository contains six Spring Boot microservices wired together:

- EnergyProducerService (port 8081)
- EnergyUserService (port 8082)
- EnergyUsageService (port 8083)
- EnergyPercentageService (port 8084)
- EnergyAPI (port 8080)
- EnergyGUI (JavaFX, port 8085)

Use Docker Compose to build and run all services together.

## Quick Start

```bash
docker-compose up --build
```

Ensure RabbitMQ is accessible at `rabbitmq:5672` and its UI at `http://localhost:15672`.

See `PROJECT_IMPORTANT_STEPS.md` for detailed setup.
