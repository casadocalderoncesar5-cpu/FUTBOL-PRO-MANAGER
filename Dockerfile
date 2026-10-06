# ========================================================
# ETAPA 1: Construcción (Build) con Maven y JDK 21
# ========================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar definición del proyecto y código fuente
COPY pom.xml .
COPY src ./src

# Compilar y empaquetar el JAR ejecutable de Spring Boot
RUN mvn clean package -DskipTests

# ========================================================
# ETAPA 2: Imagen ligera de ejecución en producción (Alpine JRE 21)
# ========================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el archivo JAR generado en la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Render asigna dinámicamente la variable de entorno PORT (por defecto 8091)
ENV PORT=8091
EXPOSE 8091

# Parámetros optimizados para la capa gratuita de Render (512MB RAM)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
