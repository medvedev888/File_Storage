FROM openjdk:23-ea-17-jdk-bullseye
WORKDIR /app
CMD ["./gradlew", "clean", "bootJar"]
COPY build/libs/*.jar /app/File_Storage.jar
EXPOSE 8080
CMD ["java", "-jar", "File_Storage.jar"]
