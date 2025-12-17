# =========================
# Stage 1: build (com Maven)
# =========================
FROM --platform=linux/amd64 maven:3.9.6-eclipse-temurin-21 AS build

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar cache de dependências
COPY pom.xml .

# Baixa dependências offline (cachê de camada do Docker)
RUN mvn -B dependency:go-offline

# Copia o código-fonte completo depois (coloca em layout Maven)
# Move os arquivos Java para `src/main/java` dentro do container para o Maven
COPY src/ ./src/main/java/

# Gera o JAR sem executar testes (mais rápido para builds locais)
RUN mvn -B package -DskipTests

# =========================
# Stage 2: runtime (JDK)
# =========================
FROM --platform=linux/amd64 amazoncorretto:21

# Diretório de trabalho no runtime
WORKDIR /app

# Copia o JAR gerado do stage de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Spring Boot
EXPOSE 8080

# Comando padrão para rodar o Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
