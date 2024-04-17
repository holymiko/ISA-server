FROM maven:3.6.3-amazoncorretto-15 AS build
WORKDIR /app

ENV PROFILE prod
ENV TIME_ZONE "Europe/Prague"

COPY pom.xml .
COPY src ./src
COPY txt/src ./txt/src
COPY txt/export/tickers ./txt/export/tickers

RUN mvn clean
# RUN mvn package -DskipTests spring-boot:repackage # this works
RUN mvn package -P $PROFILE  # Production exclusive Dockerfile
# mvn package automatically triggeres spring-boot:repackage due to pom.xml build plugin

# Dynamic loading of project.version from pom.xml throw application.properties
ENTRYPOINT java -Dspring.profiles.active=$PROFILE \
                -Duser.timezone=$TIME_ZONE \
                -jar \
                /app/target/ISA-server-$(grep 'project.version=\S*' <<< cat ./target/classes/application.properties | cut -d '=' -f 2).jar
