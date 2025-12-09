# CRM Backend (Spring Boot)

Projeto backend em Java Spring Boot para um CRM — scaffolding inicial.

Como executar (PowerShell):

1. Compilar:
```
mvn clean package
```

2. Rodar via Maven (desenvolvimento):
```
mvn spring-boot:run
```

3. Rodar JAR gerado:
```
java -jar target\crm-backend-0.0.1-SNAPSHOT.jar
```

4. Rodar em porta alternativa:
```
java -jar target\crm-backend-0.0.1-SNAPSHOT.jar --server.port=8081
```

Observações:
- A aplicação usa MongoDB (config em `src/main/resources/application.properties`).
- O `DataSeeder` popula a coleção `users` na primeira execução, se vazia.
