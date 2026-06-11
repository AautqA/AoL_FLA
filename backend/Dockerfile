FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY src/ src/
RUN mkdir -p out && \
    find src/main/java -name "*.java" | xargs javac -d out
EXPOSE 8080
CMD ["java", "-cp", "out", "com.dinereserve.DineReserveApplication"]
