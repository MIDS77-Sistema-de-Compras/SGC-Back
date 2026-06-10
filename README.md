<div align="center">

# SGC — Sistema de Gerenciamento de Compras

**Backend corporativo para solicitações e controle de compras da Centroweg**

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Produção-4169E1?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![H2](https://img.shields.io/badge/H2-Testes-003DA5?style=flat-square)](https://h2database.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![Lombok](https://img.shields.io/badge/Lombok-✓-pink?style=flat-square)](https://projectlombok.org/)

<br/>
</div>

## 📖 Contexto

O **SGC** é o backend do sistema corporativo de **solicitação e controle de compras** do **Centroweg**, desenvolvido a pedido de **Tissi**. Ele centraliza o fluxo de aquisições: um colaborador abre uma solicitação de compra ou de serviço (provision), vinculada ao seu Centro de Responsabilidade (CR) e filial, e essa solicitação percorre um ciclo de estados até ser entregue ou cancelada.

A API REST é consumida pelo frontend web da empresa.

---

## 🏗️ Arquitetura — Monólito Modular

O projeto adota **Monólito Modular** (*Modular Monolith*): uma única aplicação Spring Boot, mas com o código organizado em **módulos de domínio independentes**, cada um com suas próprias camadas internas. Isso dá coesão de negócio sem a complexidade operacional de microsserviços.

```
src/main/java/net/centroweg/gerenciamentocompras/
│
├── modules/
│   ├── user/        ← Usuários e perfis de acesso
│   ├── cr/          ← Centros de Responsabilidade e Filiais
│   ├── request/     ← Solicitações de compra (domínio central)
│   ├── provision/   ← Solicitações de serviço
│   └── product/     ← Unidades de medida de produtos
│
├── shared/          ← Código reutilizado por todos os módulos
│
└── config/          ← Configurações globais (segurança, CORS)
```

Cada módulo segue a mesma estrutura interna de camadas:

```
modules/<nome>/
├── domain/                ← Coração do módulo: entidades e regras
│   ├── entity/            → Classes JPA mapeadas para o banco
│   ├── exception/         → Exceções específicas deste domínio
│   └── strategy/          → Implementações do padrão Strategy (quando aplicável)
│
├── infrastructure/
│   └── persistence/       → Interfaces Spring Data JPA (repositórios)
│
├── presentation/          ← Fronteira HTTP do módulo
│   ├── controller/        → Endpoints REST
│   └── dto/
│       ├── request/       → O que chega do cliente
│       └── response/      → O que retorna ao cliente
│
└── service/               ← Lógica de negócio
    ├── usecases/
    │   ├── serviceIntrf/  → Contratos (interfaces)
    │   └── serviceImpl/   → Implementações dos casos de uso
    └── mapper/            → Conversão entre entidade ↔ DTO
```

---

## 📦 Módulos

### `user` — Usuários e Perfis

Gerencia os usuários do sistema e seus papéis de acesso.

**Entidades:**
- `User` — colaborador com nome, e-mail, ramal, status ativo/inativo e papel (role)
- `Role` — perfil de acesso (ex: COMPRADOR, GESTOR)

**Segurança aplicada neste módulo:**
- Senha armazenada como hash **BCrypt**
- CPF armazenado como hash **HMAC-SHA256** (com chave secreta configurável via `app.security.cpf-secret`) — o CPF nunca fica em texto claro no banco
- Validação de unicidade de e-mail e CPF antes de persistir

**Endpoints:**

| Método | Rota | Ação |
|--------|------|------|
| `POST` | `/users` | Cria usuário |
| `GET` | `/users` | Lista todos |
| `GET` | `/users/userId/{id}` | Busca por ID |
| `GET` | `/users/userName/{name}` | Busca por nome |
| `PUT` | `/users/userId/{id}` | Atualiza usuário |
| `DELETE` | `/users/userId/{id}` | Remove usuário |

---

### `cr` — Centros de Responsabilidade e Filiais

Gerencia a estrutura organizacional da empresa: os **CRs** (Centros de Responsabilidade) e as **Filiais**, além da associação entre eles (`CrBranch`).

**Entidades:**
- `Cr` — Centro de Responsabilidade com nome, código e flag `master`
- `Branch` — Filial da empresa
- `CrBranch` — Vínculo entre um CR e uma Filial, com responsável atribuído

**Endpoints:** `/crs`, `/branches`, `/cr-branches` — CRUD completo para cada recurso.

---

### `request` — Solicitações de Compra *(domínio central)*

Módulo principal do sistema. Representa as solicitações de compra abertas pelos colaboradores.

**Entidades:**
- `Request` — solicitação vinculada a um `CrBranch`, com status, data e usuários criadores
- `Status` — estado da solicitação no fluxo de aprovação

**Fluxo de status (padrão Strategy):**

```
    INICIAL
       │
   ┌───┴───┐
   ▼       ▼
APROVADO  RECUSADO
   │
   ▼
EM ATENDIMENTO
   │
   ▼
ENTREGUE

(CANCELADO pode ocorrer em qualquer etapa)
```

Cada transição é implementada como uma estratégia independente (`ApprovedStatusImpl`, `RecusedStatusImpl`, `InServiceStatusImpl`, `DeliveredStatusImpl`, `CancelledStatusImpl`), o que permite adicionar ou modificar transições sem alterar a lógica central.

**Endpoints:**

| Método | Rota | Ação |
|--------|------|------|
| `POST` | `/requests` | Abre solicitação |
| `GET` | `/requests` | Lista todas |
| `GET` | `/requests/{id}` | Busca por ID |
| `PUT` | `/requests/{id}` | Atualiza |
| `DELETE` | `/requests/{id}` | Inativa |

---

### `provision` — Serviços (Provisões)

Gerencia as **provisões de serviço** — quando a necessidade não é comprar um produto, mas contratar alguém para realizar algo (ex: uma manutenção, um conserto, uma instalação). O termo "provision" no sistema representa esse tipo de solicitação de serviço.

**Entidade:** `Provision` — nome, valor total e descrição do serviço.

**Endpoints:** `/provisions` — CRUD completo.

---

### `product` — Produtos e Unidades de Medida

Gerencia as unidades de medida utilizadas nos produtos das solicitações.

**Entidade:** `MeasurementUnit` — unidade de medida com nome e abreviação.

**Endpoints:** `/measurement-units` — CRUD completo + busca por abreviação.

---

## 🔗 Shared — Código Compartilhado

A pasta `shared/` contém código que **nenhum módulo específico possui**, mas todos podem usar:

```
shared/
├── exception/
│   ├── BusinessException.java     → Exceção base de negócio (carrega HttpStatus)
│   ├── GlobalExceptionHandler.java → @RestControllerAdvice — captura todas as exceções
│   └── ApiError.java              → Formato padrão de resposta de erro
└── MessageDTO.java                → DTO genérico para respostas de mensagem simples
```

**Por que `BusinessException` importa:** todas as exceções de domínio (ex: `UserNotFoundException`, `RequestNotFoundException`) estendem `BusinessException`. O `GlobalExceptionHandler` captura qualquer `BusinessException` e retorna automaticamente o status HTTP correto definido na exceção, sem precisar tratar cada tipo individualmente.

**Formato padrão de erro:**
```json
{
  "status": 404,
  "message": "Usuário não encontrado com id: 42",
  "errors": null
}
```

---

## ⚙️ Config — Configurações Globais

```
config/security/
├── WebSecurityConfig.java  → Define regras de autenticação, CORS e o PasswordEncoder (BCrypt)
└── CpfHasher.java          → Bean que realiza o hash HMAC-SHA256 do CPF
```

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 4.0.6 | Framework base |
| **Spring Data JPA** | — | Abstração do banco via repositórios |
| **Spring Security** | — | Autenticação e autorização |
| **Spring Validation** | — | Validação de DTOs com anotações |
| **PostgreSQL** | — | Banco de dados de produção (Supabase) |
| **H2** | — | Banco em memória para testes |
| **Lombok** | — | Redução de boilerplate (getters, construtores) |
| **SpringDoc OpenAPI** | 2.8.6 | Documentação automática Swagger UI |
| **jjwt** | 0.11.5 | Geração e validação de tokens JWT |
| **BCrypt** | — | Hash de senhas |
| **HMAC-SHA256** | — | Hash de CPF |

---

## ⚙️ Configuração

O projeto usa variáveis de ambiente definidas em `.env` (não versionado):

```properties
# Banco de dados (Supabase)
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/postgres
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<senha>

# Chave para hash do CPF
CPF_SECRET=<chave-secreta>
```

O `application.properties` lê essas variáveis automaticamente via `spring.config.import=optional:file:.env[.properties]`.

---

## 🚀 Como Rodar

### Pré-requisitos

- Java 21+
- Maven 3.9+ (ou use o Maven Wrapper do IntelliJ)
- Banco PostgreSQL acessível (ou configure as variáveis para usar H2 localmente)

### Rodando localmente

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd SGC-Back

# 2. Crie o arquivo .env com as variáveis (veja seção Configuração)

# 3. Suba a aplicação
./mvnw spring-boot:run

# API disponível em: http://localhost:8080
# Swagger UI em:     http://localhost:8080/swagger-ui.html
```

### Rodando os testes

Os testes de integração usam **H2 em memória** — nenhum banco externo necessário:

```bash
./mvnw test
```

---

## 🧪 Testes

O projeto possui dois tipos de teste:

**Testes unitários** (`CreateUserImplTest`) — usam Mockito para isolar a lógica de serviço sem subir o contexto Spring.

**Testes de integração** (`UserIntegrationTest`) — sobem o contexto completo com `@SpringBootTest`, usam MockMvc para simular requisições HTTP e H2 em memória como banco.

O perfil `test` é ativado via `@ActiveProfiles("test")` e carrega `application-test.properties` com as configurações do H2.

---

## 👥 Equipe

Desenvolvido por **MIDS 77** para a **Centroweg**

Pacote base: `net.centroweg.gerenciamentocompras`
