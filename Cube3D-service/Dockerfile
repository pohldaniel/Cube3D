FROM maven:3.9-eclipse-temurin-22-alpine AS builder

COPY settings.xml /
COPY src /src
COPY pom.xml /
RUN mvn install -DskipTests -s settings.xml -f pom.xml
RUN cp target/Cube3D-service*.jar target/Cube3D-service.jar

FROM openjdk:22-jdk AS deployer
COPY --from=builder target/Cube3D-service.jar /Cube3D-service.jar
EXPOSE 8080
EXPOSE 443
EXPOSE 80
ENTRYPOINT ["java","-Xms256m","-Xmx2048m","-jar","/Cube3D-service.jar"]