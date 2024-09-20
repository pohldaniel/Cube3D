FROM maven:3.9-eclipse-temurin-22-alpine AS builder

COPY settings.xml /
COPY src /src
COPY etc/userCerts /etc/userCerts
COPY pom.xml /
RUN mvn install -DskipTests -s settings.xml -f pom.xml
RUN cp target/Security-service*.jar target/Security-service.jar

FROM openjdk:22-jdk AS deployer
COPY --from=builder target/Security-service.jar /Security-service.jar
COPY --from=builder etc/userCerts /etc/userCerts
EXPOSE 8443
EXPOSE 443
ENTRYPOINT ["java","-Xms256m","-Xmx2048m","-jar","/Security-service.jar"]