# NEXT CHALLENGE

### Project created as part of the course taught by Movile.

## System Requirements

- Docker
- MongoDb
- RabbitMQ
- HAProxy

## Docker

```
$ cd $WORKSPACE/next
$ docker-compose -f docker-compose.yml up -d
```

## API Guide
Source files of the Rest APIs for each component are found in [`SWAGGER`] format (https://swagger.io/solutions/api-documentation/).
After starting each service, its respective rendered documentation in `JSON` format can be found at:

[`http://localhost:{port}/{service}/v1/api-docs`](http://localhost:{port}/{service}/v1/api-docs)


## Non-Functional Requirements

Must be prepared to be fault tolerant and resilient.

Uses container / kubernetes for delivery / orchestration.

## Advanced
Automation of tests to be implemented with [`Cucumber`](https://docs.cucumber.io/), [`Dredd`](http://dredd.org/en/latest/) and [`JMeter`](https://jmeter.apache.org/).

## License
Copyright (C) 2018 ddval, Inc.

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License version 2 as published by the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.