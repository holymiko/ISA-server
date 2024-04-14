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

# TODO remove hardcoded version, remove hardcoded profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/target/ISA-server-1.2.1.jar"]

# Attemps to COPY from /app/target/ to /app
# Attemps to use VERSION variable
#RUN ls -la .
#RUN ls -la ./target
#RUN find target/ -name '*.jar' -exec echo "JAR file found: {}" \;
#RUN export VERSION=$(find target/ -name '*.jar' -print -quit)
#RUN echo $VERSION
#RUN echo $( stat target/$VERSION)

#RUN export JAR_FILE=$(find /app/target/ -name '*.jar' -print -quit)
#RUN echo "JAR_FILE $JAR_FILE"
# RUN echo  "The value of MY_VARIABLE is ${JAR_FILE}"
# COPY ${JAR_FILE} application.jar