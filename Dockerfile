FROM openjdk:17
VOLUME /tmp
EXPOSE 8090
ADD ./target/ms-mqttclient-0.0.1-SNAPSHOT.jar mqttclient.jar
ENTRYPOINT ["java", "-jar", "/mqttclient.jar"]