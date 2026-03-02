# Agregador de Investimentos

[PROJETO EM CONSTRUCAO]
Este projeto esta em fase de desenvolvimento. A base do sistema e os testes unitarios iniciais ja foram implementados, e novas funcionalidades serao adicionadas em breve.

## Descricao
Uma API REST construida em Java com Spring Boot. O objetivo do projeto e servir como um agregador de investimentos. Atualmente, o sistema conta com o modulo de gerenciamento de usuarios (CRUD completo) e testes unitarios da camada de servico.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- JUnit 5 e Mockito (Testes Unitarios)

## Endpoints da API (Usuarios)

A API possui a rota principal `/v1/users` com as seguintes operacoes:

- POST /v1/users : Cria um novo usuario.
- GET /v1/users : Lista todos os usuarios cadastrados.
- GET /v1/users/{userId} : Retorna os detalhes de um usuario especifico baseado no UUID.
- PUT /v1/users/{userId} : Atualiza os dados (nome, email ou senha) de um usuario.
- DELETE /v1/users/{userId} : Deleta um usuario do sistema.

## Estrutura do Banco de Dados
A entidade de usuario salva automaticamente os seguintes dados:
- ID (UUID gerado automaticamente)
- Nome
- E-mail
- Senha
- Data de Criacao (Timestamp gerado automaticamente)
- Data de Atualizacao (Timestamp gerado automaticamente)