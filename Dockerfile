FROM openjdk:17

EXPOSE 8080

WORKDIR /app

COPY . /app

CMD ["java", "-jar", "/app/target/JeasAdmin-0.0.1-SNAPSHOT.jar"]