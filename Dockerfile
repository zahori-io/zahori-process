# Etapa de construcción
FROM maven:3.9.11-eclipse-temurin-17-alpine as javabuilder

WORKDIR /code
COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src /code/src
RUN mvn clean package

# Etapa de ejecución
FROM eclipse-temurin:17.0.9_9-jre-alpine

# Crear grupo y usuario no privilegiado "zahori"
#RUN addgroup -S zahori && adduser -S zahori -G zahori

# Create logs directory and set ownership and permissions
#RUN mkdir -p /usr/app/logs && \
#    chown zahori:zahori /usr/app/logs && \
#    chmod 755 /usr/app/logs

# Create target for evidences directory and set ownership and permissions
#RUN mkdir -p /usr/app/target && \
#    chown zahori:zahori /usr/app/target && \
#    chmod 755 /usr/app/target

# Cambiar a usuario "zahori"
#USER zahori

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=javabuilder /code/target/app.jar /usr/app/app.jar

# Establecer el directorio de trabajo
WORKDIR /usr/app

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/usr/app/app.jar"]
