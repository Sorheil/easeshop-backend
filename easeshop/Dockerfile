FROM maven:latest AS build
WORKDIR /app
COPY pom.xml /app/
COPY src /app/src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/easeshop.jar /app/easeshop.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/easeshop.jar"]