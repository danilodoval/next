FROM maven:3.5-jdk-8 AS build

WORKDIR /builddir

COPY src src/
COPY pom.xml ./

RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine AS release

WORKDIR /core

# Gets jar builded from build image
COPY --from=build /builddir/target/driver-*.jar  ./driver.jar
COPY docker-entrypoint.sh /usr/local/bin/

EXPOSE 8080

ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["driver"]
