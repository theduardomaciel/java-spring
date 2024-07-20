# Spring Boot REST API

Este projeto é um exemplo de uma simples API RESTful desenvolvida com Spring Boot.

### Documentação

A documentação foi implementada com o pacote `springdoc-openapi`, que gera automaticamente a documentação da API em formato OpenAPI 3.0.

A documentação é gerada automaticamente a partir dos métodos dos controladores.

Para acessar a documentação, execute o projeto e acesse a URL `http://localhost:8080/swagger-ui.html` ou use o pseudônimo `http://localhost:8080/docs`.

> [!NOTE]  
> Para alterar o pseudônimo, edite o arquivo `application.properties` e edite a seguinte propriedade:
> ```properties
> springdoc.api-docs.path=/docs
> ```

A documentação também está disponível em formato JSON na URL `http://localhost:8080/v3/api-docs`.