FROM maven:3.6.3-amazoncorretto-15 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY txt/src ./txt/src
COPY txt/export/tickers ./txt/export/tickers

RUN mvn clean
# RUN mvn package -DskipTests spring-boot:repackage # this works
RUN mvn package -P prod  # Production exclusive Dockerfile TODO Solve switching profiles
# mvn package automatically triggeres spring-boot:repackage due to pom.xml build plugin

# TODO remove hardcoded profile
# Dynamic loading of project.version from pom.xml throw application.properties
ENTRYPOINT java -Dspring.profiles.active=prod -jar /app/target/ISA-server-$(grep 'project.version=\S*' <<< cat ./target/classes/application.properties | cut -d '=' -f 2).jar
