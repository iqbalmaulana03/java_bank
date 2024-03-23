FROM eclipse-temurin:17

LABEL mentainer="maulanamasiqbal@gmail.com"

WORKDIR /app

COPY target/javabank-0.0.1-SNAPSHOT.jar /app/javabank.jar

ENTRYPOINT ["java", "-jar", "javabank.jar"]