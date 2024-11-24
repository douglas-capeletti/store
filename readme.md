# Store Transactions Services

This objective of this project is to save and retrieve purchase transaction from a store.
This transactions are saved in US Dollars, but can be retrieved used exchange rates from any country,
the rate used will be as close as possible from the transaction date,
otherwise will be used a rate from before this date within a 6 month range.
In case there is no exchange rate for this period or the desired country name has a typo, you will receive an error.

The exchange rates source is the treasury information available in the following link.

https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange

---

## How to run this project

### Using the Makefile

you can use the command `make` on linux or mac to execute the Makefile commands. The commands are

``` yaml
help: Show help for each of the Makefile recipes.
production: Run the production Dockerfile
development: Run the development Dockerfile (including the app itself)
development-stop: Stop the development Dockerfile
database: Run the development database
database-stop: Stop the development database
database-reset: Remove the content from database and force the application to recreate it
```

---

### Using Docker Compose directly

This project contain 3 compose file.

- `docker-compose.yaml` configuration to run the application on a pipeline or server
- `docker-compose-dev.yaml` configuration to run the application database and the console
- `docker-compose-local.yaml` configuration to run only the database and the console for local development

the server will run on port `9000` and the console on `5000`, see the compose file to have the default email and
password for it.

---

### Running locally

---
To run locally you MUST have and postgres running on port 5432,
you can have this by running the docker compose for local development or running the H2 in-memory database

``` sh
# Running with postgres on docker 
make database # (docker compose -f docker-compose-local.yaml up -d)
./gradlew clean bootRun

# Running with test profile to run the in-memory database
./gradlew clean bootRun -Dspring.profiles.active=test
```

---

### Project APIs

#### Save Purchase

``` http 
POST http://localhost:9000/api/v1/transactions/purchase
Content-Type: application/json

{
    "description": "New purchase",
    "amount": 10.5,
    "transaction_date": "2024-10-31"
}
``` 

#### Get All Purchases

``` http 
GET http://localhost:9000/api/v1/transactions/purchase
```

#### Get Purchase by id

``` http
GET http://localhost:9000/api/v1/transactions/purchase/1
``` 

#### Get Purchase with exchange rate

``` http 
GET http://localhost:9000/api/v1/transactions/purchase/1/exchange-rate/country/Brazil
```

### Project Layout

This project have 3 major layer on a specific order.

domain -> application -> infrastructure

The domain is the inner layer that contains the business rules entities.
This layer MUST not know anything from other layers

The application is the middle layers, and contains the project structure itself,
and the business rules within this structure without known about any framework implementation detail or decision.

The infrastructure is the outer layer, this layer knows that this is a SpringBoot project,
that uses JPA to map entities to a database,
format the request and response using Jackson annotations,
and consume an endpoint to find exchange rates.

This is a Clean architecture inspired layout to protect the business rules from framework changes,
this structure works well with more complex business rules due the verbosity to navigate between layers.

Another interesting point is that we can remove the domain and application layer from this project and turn this into a
separated library that doesn't contain any framework information,
and import this library to implement this project on SpringBoot, Quarkus or any other framework.

We can change things like instead of having this exchange rates from an external client, fetch from a database or a file
just from implementing a gateway to this and inject on the infrastructure (injection are on Main.java for now),
and we can do the same for Transactions, that are fetched from the database, but could be from an external client for
example.

this project also contains a flyway configured for database versioning and a postman collection for the possible
requests

That's pretty much the project, Thank you!