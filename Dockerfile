FROM openjdk:23-ea-17-jdk-bullseye
WORKDIR /app
COPY build/libs/File_Storage-0.0.1-SNAPSHOT.jar /app/File_Storage.jar
ENTRYPOINT ["java", "-jar", "File_Storage.jar"]