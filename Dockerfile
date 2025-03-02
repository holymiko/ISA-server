FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app

# ARG loads value from docker-compose
ARG PROFILE_ARG
# ENV makes value persistant for ENTRYPOINT
ENV PROFILE $PROFILE_ARG
ENV TIME_ZONE "Europe/Prague"

COPY pom.xml .
COPY src ./src
COPY txt/src ./txt/src
COPY txt/export/tickers ./txt/export/tickers

RUN mvn clean
# RUN mvn package -DskipTests spring-boot:repackage # this works
RUN mvn package -P $PROFILE
# mvn package automatically triggeres spring-boot:repackage due to pom.xml build plugin

# Dynamic loading of project.version from pom.xml throw application.properties
ENTRYPOINT java -Dspring.profiles.active=$PROFILE \
                -Duser.timezone=$TIME_ZONE \
                -jar \
                /app/target/ISA-server-$(grep 'project.version=\S*' <<< cat ./target/classes/application.properties | cut -d '=' -f 2).jar
