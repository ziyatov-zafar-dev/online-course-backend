#FROM openjdk:17
#ADD target/logisticsApplication-0.0.1-SNAPSHOT.jar app.jar
#VOLUME /simple.app
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app.jar"]


FROM maven:3.8.5-openjdk-17 as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0-jdk-slim
COPY --from=build /target/online-course-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/demo.jar"]