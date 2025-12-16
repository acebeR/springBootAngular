# Stage 1: build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml ./

# Baixa dependências em modo offline (aproveita cache de camada do Docker)
RUN mvn -B dependency:go-offline

# Copia o código-fonte só depois que as dependências estiverem em cache
COPY src ./src

# Gera o JAR sem testes (use -DskipTests para acelerar builds locais)
RUN mvn -B package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o JAR gerado do stage 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
