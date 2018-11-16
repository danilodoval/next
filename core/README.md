# CORE

Core Service.

## System Requirements

- Java 1.8+
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

    $ java -jar target/core-0.0.1-SNAPSHOT.jar

The `jar` file is also made to be fully executable in `Unix` systems and can alternatively be executed like any
executable binary:

    $ target/core-0.0.1-SNAPSHOT.jar

> See [Setting Up Developer Environment](#markdown-header-setting-up-developer-environment) section for more details on
the required presets before running the application.

## Docker

```
$ cd $WORKSPACE/snack-bar
$ docker build -t core .
$ docker run --rm -d -p 8080:8080 --name core -e CORE_MONGODB_URI="mongodb://localhost/next_core" -e CORE_DATA_MONGO="localhost" core
```

## Environment Variables

The externalized configuration of this application is grouped by both *Required* and *Optional* environment variables,
as shown in the following tables.

### Required Configuration

These are mandatory variables which the values depend on the environment in which the application is deployed. The
default values are useful only in case when running the application in the developer's local environment.

| Name | Description | Default |
| ---- | ----------- | ------- |
| RABBIT_URI | URL of the database. | amqp://guest:guest@localhost:5672 |

### Optional Configuration

The variables listed here allow fine tuning of the application, usually for debugging purposes in development time.
Default values must follow tested/referenced conventions, if applied, to be ready for production environment.

| Name | Description | Default |
| ---- | ----------- | ------- |
| CORE_SERVER_PORT | Server HTTP port. | 9080 |
| CORE_LOG_PATH | Set the relative path where the *app.log* file will be generated. | logs |
| CORE_LOG_LEVEL | Set the app log level. | INFO |

## API Guide

The [`api.yaml`](src/main/resources/docs/swaggger/v1/api.yaml) is the application APIs documentation source file in
[`AsyncAPI`](https://www.asyncapi.com/v1/guide/) format.
After starting the application, the rendered documentation, in `YAML` format, can be found at:

[`http://localhost:8080/api/v1/api-docs`](http://localhost:8080/api/v1/api-docs)
