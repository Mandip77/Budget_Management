# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy Maven files first to leverage Docker cache
COPY pom.xml .
# Download dependencies (optional optimization)
RUN mvn -q -e -DskipTests dependency:go-offline

# Now copy source and build
COPY src ./src
RUN mvn -q -e -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the fat jar built above (adjust name if different)
COPY --from=builder /build/target/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
