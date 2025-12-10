## 🧠 Desafio Técnico – Sistema de Gestão de Projetos e Demandas

API RESTful desenvolvida em Java com Spring Boot para o gerenciamento de projetos e tarefas, como parte de um desafio técnico. O sistema permite a criação, listagem e acompanhamento de projetos e suas respectivas tarefas.

Esse desafio foi feito com base no desafio proposto pelo professor [Matheus Ferreira](https://www.youtube.com/@matheuslf/videos).
- Link para o vídeo do desafio: [Desafio](https://youtu.be/f4ZsfiL7f3Y?si=cOtzxx1AVlzl9G_U)
- Repositório original: [Repositório](https://github.com/matheuslf/dev.matheuslf.desafio.inscritos)


## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Security (Autenticação com JWT e chaves RSA)
- PostgreSQL 17
- Maven
- MapStruct
- Bean Validation
- JUnit 5, Mockito e RestAssured(MockMvc) para testes

## Funcionalidades

- CRUD completo para Projetos.
- CRUD completo para Tarefas (vinculadas a um Projeto).
- Sistema de autenticação de usuários com geração de token JWT através de criptografia com chaves RSA
- Criptografia de senhas de usuários.
- Tratamento de exceções e validação de dados de entrada.
- Mapeamento de DTOs otimizado com MapStruct.

## Pré-requisitos para Execução

Para executar o projeto, é necessário ter o ambiente configurado com:

- JDK 21 ou superior.
- Maven 3.9+.
- Uma instância do PostgreSQL em execução.

As credenciais do banco de dados e as chaves RSA para a assinatura dos tokens JWT são carregadas a partir de um arquivo `secrets.properties`, que não está incluído no controle de versão por razões de segurança. A aplicação está configurada para importar estas variáveis a partir desse arquivo na raiz do projeto, conforme indicado no `application.properties`.

## Como Executar
**1. Clone o repositório:**
```bash
git clone https://github.com/mateusbaldu/dev.matheuslf.desafio.inscritos
cd dev.matheuslf.desafio.inscritos
```


**2. Crie e configure o arquivo de segredos:**
```bash
secrets.properties:
# Configuração do Banco de Dados PostgreSQL
DB.URL=jdbc:postgresql://localhost:5432/seu_banco
DB.USERNAME=seu_usuario
DB.PASSWORD=sua_senha

# Chaves RSA para assinatura do JWT
# Certifique-se de que as chaves estão em uma única linha (substitua pelo conteúdo real das chaves)
jwt.public.key=NOME_DO_ARQUIVO_DA_CHAVE_PUBLICA
jwt.private.key=NOME_DO_ARQUIVO_DA_CHAVE_PRIVADA
```

**3. Gere suas chaves RSA:**
Caso não possua um par de chaves RSA, você pode gerá-las utilizando o OpenSSL com os seguintes comandos:
```bash
# Gerar a chave privada
openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048

# Extrair a chave pública da chave privada
openssl rsa -in app.key -pubout -out app.pub
```

**4. Execute a aplicação:**
Utilize o Maven para iniciar a aplicação.
```bash
./mvnw spring-boot:run
```
A API estará disponível em http://localhost:8080


## Endpoints da API

A seguir, a lista de endpoints disponíveis na aplicação. Para endpoints que exigem autenticação, é necessário enviar o token JWT no cabeçalho Authorization (ex: Authorization: Bearer <seu_token>).

## Autenticação e Usuários

**POST /project-manager/login :**

Realiza a autenticação de um usuário e retorna um token JWT.

Corpo da Requisição:

```bash
{
  "email": "usuario@exemplo.com",
  "password": "senha_forte_123"
}
```

**POST /project-manager/users/new :**

Cria um novo usuário.

Corpo da Requisição:

```bash
{
  "name": "Nome do Usuário",
  "email": "usuario@exemplo.com",
  "password": "senha_forte_123"
}
```

**PUT /project-manager/users :**

Atualiza os dados do usuário autenticado. (Endpoint protegido)

Corpo da Requisição:
```bash
{
  "name": "Novo Nome do Usuário",
  "email": "novo_email@exemplo.com"
}
```

**DELETE /project-manager/users :**

Remove o usuário autenticado. (Endpoint protegido)

## Projetos

**POST /project-manager/projects :**

Cria um novo projeto. (Endpoint protegido)

Corpo da Requisição:

```bash
{
  "name": "Nome do Novo Projeto",
  "description": "Descrição detalhada sobre o projeto.",
  "startDate": "2025-10-30",
  "endDate": "2026-04-22"
}
```

**GET /project-manager/projects :**

Lista todos os projetos de forma paginada. (Endpoint protegido)

## Tarefas

**POST /project-manager/tasks :**

Cria uma nova tarefa vinculada a um projeto. (Endpoint protegido)

Corpo da Requisição:
```bash
{
  "title": "Desenvolver funcionalidade de login",
  "description": "Criar o endpoint e a lógica de autenticação.",
  "priority": "HIGH",
  "status": "TODO",
  "dueDate": "2025-11-15",
  "projectId": 1
}
```

**GET /project-manager/tasks :**

Lista tarefas com filtros opcionais (status, priority, projectId). (Endpoint protegido)

```bash
Exemplo de URL com filtros: /project-manager/tasks?status=DOING&priority=HIGH&projectId=1
```

**PUT /project-manager/tasks/{id}/status :**

Atualiza o status de uma tarefa específica. (Endpoint protegido)

Corpo da Requisição:

```bash
{
  "status": "DONE"
}
```

**DELETE /project-manager/tasks/{id} :**
Remove uma tarefa específica. (Endpoint protegido)
