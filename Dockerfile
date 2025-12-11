FROM eclipse-temurin:latest

ADD build/libs/users-0.0.1-SNAPSHOT.jar /tmp/users-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/tmp/users-0.0.1-SNAPSHOT.jar"]