# Alpine Linux with OpenJDK JRE
FROM openjdk:12-oracle

# copy WAR into image
COPY ./build/libs/contactApi-1.0-SNAPSHOT.jar /app.jar
COPY config.yaml /config.yaml

# run application with this command line
CMD ["/usr/bin/java", "-jar", "/app.jar"]