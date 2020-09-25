#
# Build stage
#
FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install


#
# Package stage
#
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/zoo-app-0.0.1-SNAPSHOT.jar zoo-app.jar
ENTRYPOINT ["java","-jar","/zoo-app.jar"]
