FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY eduHub/build/libs/eduHub-0.0.1-SNAPSHOT.jar eduHub.jar

ENTRYPOINT ["java", "-jar", "eduHub.jar"]
