FROM maven:3.8.2-openjdk-11 AS build
WORKDIR /app
COPY src src
COPY pom.xml .
RUN mvn clean install

FROM tomcat:9-jre11-openjdk
COPY --from=build app/target/ROOT.war /usr/local/tomcat/webapps/
EXPOSE 8080