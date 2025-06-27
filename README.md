# Distributed Energy Project

This repository contains six Spring Boot microservices wired together:

the structure of the component diagram from bottom to top, as you requested:

    Energy Producer / Energy User

        producer message / user message --> RabbitMQ

    RabbitMQ

        update message --> Current Percentage Service

        producer/user message --> Usage Service

    Current Percentage Service

        update percentage table --> PostgreSQL

    Usage Service

        update usage table --> PostgreSQL

    PostgreSQL

        read tables --> Spring Boot REST API

    Spring Boot REST API

        GET /energy/current --> JavaFX GUI

        GET /energy/historical?start=...&end=... --> JavaFX GUI

    JavaFX GUI

database : docker exec -it $(docker ps -qf "ancestor=postgres:15") psql -U energyuser -d energydb

\dt
SELECT * FROM usage_record;
SELECT * FROM hourly_usage;

DELETE FROM hourly_usage;
-- If you have a table for current percentage, e.g.:
-- DELETE FROM current_percentage;
