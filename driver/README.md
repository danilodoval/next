# DRIVER

Driver Service.

## System Requirements

- Java 1.8+
- MongoDb
- RabbitMQ

## Building

### Executing Unit and Integration Tests (*requires* `Docker`)

    $ ./mvn clean install

### Executing only Unit Tests

    $ ./mvn clean package

### Executing SonarQube scanner

    $ ./mvn clean install sonar:sonar -P{profile_name}

## Running

The build stage generates a *fat* `jar` file that can be executed using `java -jar` command:

    $ java -jar target/driver-0.0.1-SNAPSHOT.jar

The `jar` file is also made to be fully executable in `Unix` systems and can alternatively be executed like any
executable binary:

    $ target/driver-0.0.1-SNAPSHOT.jar

> See [Setting Up Developer Environment](#markdown-header-setting-up-developer-environment) section for more details on
the required presets before running the application.

## Docker

```
$ cd $WORKSPACE/driver
$ docker build -t driver .
$ docker run --rm -d -p 8080:8080 --name driver -e DRIVER_MONGODB_URI="mongodb://localhost/next_driver" -e DRIVER_DATA_MONGO="localhost" driver
```

## Environment Variables

The externalized configuration of this application is grouped by both *Required* and *Optional* environment variables,
as shown in the following tables.

### Required Configuration

These are mandatory variables which the values depend on the environment in which the application is deployed. The
default values are useful only in case when running the application in the developer's local environment.

| Name | Description | Default |
| ---- | ----------- | ------- |
| DRIVER_MONGODB_URI | URL of the database. | mongodb://localhost/next_driver |
| DRIVER_DATA_MONGO | Points to the URL of the database for the mongobee to perform data migration. | localhost |
| DRIVER_WEB_CLIENT_URL |	Web App URL(s) to mark as allowed in CORS configuration. | http://localhost:3000,http://localhost:5000

### Optional Configuration

The variables listed here allow fine tuning of the application, usually for debugging purposes in development time.
Default values must follow tested/referenced conventions, if applied, to be ready for production environment.

| Name | Description | Default |
| ---- | ----------- | ------- |
| DRIVER_SERVER_PORT | Server HTTP port. | 8080 |
| DRIVER_LOG_PATH | Set the relative path where the *app.log* file will be generated. | logs |
| DRIVER_LOG_LEVEL | Set the app log level. | INFO |

## API Guide

The [`api.yaml`](src/main/resources/docs/swaggger/v1/api.yaml) is the application Rest APIs documentation source file in
[`SWAGGER`](https://swagger.io/solutions/api-documentation/) format.
After starting the application, the rendered documentation, in `JSON` format, can be found at:

[`http://localhost:8080/api/v1/api-docs`](http://localhost:8080/api/v1/api-docs)
