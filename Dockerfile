FROM eclipse-temurin:latest

# Create a directory for certs
RUN mkdir -p /opt/certs

COPY global-bundle.pem /opt/certs/global-bundle.pem

# Import into JVM truststore
RUN keytool -importcert -trustcacerts \
    -alias aws-rds \
    -file /opt/certs/global-bundle.pem \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit \
    -noprompt

# Copy application
ADD build/libs/users-0.0.1-SNAPSHOT.jar /tmp/users-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/tmp/users-0.0.1-SNAPSHOT.jar"]