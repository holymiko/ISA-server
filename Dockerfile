FROM maven:3.6.3-amazoncorretto-15 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY docker/ScrapApp-0.0.1-SNAPSHOT.jar .

RUN mvn clean
RUN mvn package -DskipTests

RUN echo "Go0o"
RUN ls -la ./target
# RUN ls -la .
RUN echo $(find target/ -name '*.jar' -print -quit)
RUN echo $( stat target/ScrapApp-0.0.1-SNAPSHOT.jar)

# CMD ["cp",  "./ScrapApp-0.0.1-SNAPSHOT.jar", "/app/your-app.jar"]
RUN ls -la .
RUN echo $( stat target/ScrapApp-0.0.1-SNAPSHOT.jar)


#RUN mvn install -DskipTests
#CMD ["java", "-jar", "/app/your-app.jar"]
CMD ["java", "-jar", "ScrapApp-0.0.1-SNAPSHOT.jar"]
#CMD ["java", "src/main/java/home/holymiko/InvestmentScraperApp/InvestmentScraperApp"]



#RUN export JAR_FILE=$(find /app/target/ -name '*.jar' -print -quit)
#RUN echo "JAR_FILE $JAR_FILE"
# RUN echo  "The value of MY_VARIABLE is ${JAR_FILE}"
# COPY ${JAR_FILE} application.jar