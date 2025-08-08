# ---------- Stage 1: Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml ./
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
# app.jar kopyala
COPY --from=builder /app/target/*.jar /app/app.jar

# Portu dışa aç (compose'ta da map edeceğiz)
EXPOSE ${PORT}

# Spring'e portu ilet (olmasa da çalışır, sadece netlik için)
ENV SERVER_PORT=${PORT}

ENTRYPOINT ["java","-jar","/app/app.jar"]
