# Payconiq Stock Service

The Payconiq stock service allows querying, creating and updating stocks within the underlying data source.  
Initially, some sample stocks are loaded from a configuration file in the project root

An application context listener populates this when the application is initially started and the `stock` table is empty.  

## Running the application

The application is built on `spring-boot` and is therefore easily runnable via the built-in maven plugin:

    ./mvnw spring-boot:run

## Using the application

The stock service is accessible on `http:/localhost:8080/stock-service`