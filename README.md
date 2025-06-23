# Distributed Energy Project

This repository contains six Spring Boot microservices wired together:

- EnergyProducerService (port 8181)
example :  curl.exe -X POST "http://localhost:8181/producer/publish" -H "Content-Type: application/json" -d '{\"producedKw\":15}'
or : curl.exe -X POST "http://localhost:8181/producer/publish" -H "Content-Type: application/json" -d "{\"producedKw\":10}"
- EnergyUserService (port 8182)
  example : Invoke-RestMethod -Uri "http://localhost:8182/user/profile" -Method POST -Body '{"name":"John Doe","email":"john@example.com"}' -ContentType "application/json"
  or Invoke-WebRequest -Uri "http://localhost:8182/user/consume" `
>>   -Method Post `
>>   -Headers @{ "Content-Type" = "application/json" } `
>>   -Body '{"userId":1,"m":5}'

- EnergyUsageService (port 8183)
  example : curl.exe -X POST "http://localhost:8183/usage/publish?userId=1&usedKw=10"
  or : curl "http://localhost:8183/usage/aggregate/by-type"
  or :$response = Invoke-WebRequest -Uri "http://localhost:8183/usage/aggregate/by-type" $response
- EnergyPercentageService (port 8184)
- EnergyAPI (port 8180)
- EnergyGUI (JavaFX, port 8185)

Use Docker Compose to build and run all services together.

## Quick Start

```bash
docker-compose up --build
```

Ensure RabbitMQ is accessible at `rabbitmq:5672` and its UI at `http://localhost:15672`.

See `PROJECT_IMPORTANT_STEPS.md` for detailed setup.
