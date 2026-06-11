FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY src/ src/
RUN mkdir -p out && \
    find src/main/java -name "*.java" | xargs javac -d out
EXPOSE 5001
CMD ["java", "-cp", "out", "com.dinereserve.DineReserveApplication"]