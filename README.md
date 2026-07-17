<div align="center">

# SGC — Sistema de Gerenciamento de Compras

**Backend corporativo para solicitações e controle de compras da Centroweg**

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Produção-4169E1?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![H2](https://img.shields.io/badge/H2-Testes-003DA5?style=flat-square)](https://h2database.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=flat-square&logo=jsonwebtokens)](https://jwt.io/)
[![Cloudinary](https://img.shields.io/badge/Cloudinary-Uploads-3448C5?style=flat-square&logo=cloudinary)](https://cloudinary.com/)
[![Lombok](https://img.shields.io/badge/Lombok-✓-pink?style=flat-square)](https://projectlombok.org/)

<br/>
</div>

## 📖 Contexto

O **SGC** é o backend do sistema corporativo de **solicitação e controle de compras** do **Centroweg**. Ele centraliza o fluxo de aquisições: um colaborador abre uma solicitação de compra (de **produtos** e/ou de **serviços**), vinculada ao seu Centro de Responsabilidade (CR) e filial, e essa solicitação percorre um ciclo de estados até ser entregue ou cancelada. Ao longo do caminho o sistema **notifica** os envolvidos (inclusive por e-mail) e **audita** as ações realizadas.

A API REST é consumida pelo frontend web da empresa.

> 🤖 **Para assistentes de IA:** há documentação de contexto detalhada em [`ia/context/`](ia/context/) e um índice em [`CLAUDE.md`](CLAUDE.md).

---

## 🏗️ Arquitetura — Monólito Modular

O projeto adota **Monólito Modular** (*Modular Monolith*): uma única aplicação Spring Boot, mas com o código organizado em **módulos de domínio independentes**, cada um com suas próprias camadas internas. Isso dá coesão de negócio sem a complexidade operacional de microsserviços.

```
src/main/java/net/centroweg/gerenciamentocompras/
│
├── GerenciamentocomprasApplication.java   ← classe main (@SpringBootApplication)
│
├── modules/
│   ├── user/          ← Usuários e perfis de acesso (roles)
│   ├── auth/          ← Autenticação (JWT), login e recuperação de senha
│   ├── cr/            ← Centros de Responsabilidade, Filiais, Setores e Instrutores
│   ├── request/       ← Solicitações de compra (domínio central) + itens e anexos
│   ├── provision/     ← Serviços (provisões)
│   ├── product/       ← Produtos e unidades de medida
│   ├── notification/  ← Notificações (in-app e e-mail)
│   └── report/        ← Relatórios (planejado — ainda não implementado)
│
├── shared/           ← Código reutilizado por todos os módulos (exceções, auditoria, e-mail, cloudinary, segurança)
│
└── config/           ← Configurações globais (segurança, CORS, mail, cloudinary, swagger, async)
```

Cada módulo segue a mesma estrutura interna de camadas:

```
modules/<nome>/
├── domain/                ← Coração do módulo: entidades e regras
│   ├── entity/            → Classes JPA mapeadas para o banco
│   ├── exception/         → Exceções específicas deste domínio
│   └── strategy/ intfr/   → Padrões de domínio (ex.: Strategy de status no request)
│
├── infrastructure/
│   ├── persistence/       → Interfaces Spring Data JPA (repositórios) + Specifications
│   └── listener/          → Listeners de eventos de aplicação (quando aplicável)
│
├── service/
│   ├── api/               → *PublicApi: fachada de comunicação ENTRE módulos
│   ├── mapper/            → Conversão entre entidade ↔ DTO
│   └── usecases/          → Um caso de uso por classe + interface agregadora + fachada
│
└── presentation/          ← Fronteira HTTP do módulo
    ├── controller/        → Endpoints REST
    └── dto/
        ├── request/       → O que chega do cliente (records + Bean Validation)
        └── response/      → O que retorna ao cliente (records)
```

### Comunicação entre módulos

Um módulo **não acessa** o repositório ou a entidade interna de outro módulo diretamente. A conversa passa por uma **Public API** — uma interface `*PublicApi` (+ `*PublicApiImpl`) em `service/api/` que expõe só o necessário (ex.: `UserPublicApi`, `AuthPublicApi`, `ProvisionPublicApi`, `AuditLogPublicApi`).

> ⚠️ **Nota sobre nomenclatura:** a estrutura das subpastas de `service` **varia entre módulos** (ex.: `serviceimplm`/`serviceIntrf` no `user`, `serviceImpl`/`serviceIntrf` no `request`, `crimpl`/`crinterface` no `cr`, `implementations`/`interfaces` no `auth`). É dívida técnica conhecida — ao mexer em um módulo, siga o padrão local dele.

---

## 📦 Módulos e Endpoints

> Autenticação: exceto onde indicado como **público**, todos os endpoints exigem um JWT válido (enviado no cookie `jwt`).

### `user` — Usuários e Perfis

Gerencia os usuários do sistema e seus papéis de acesso.

**Entidades:** `User` (nome, e-mail, CPF, ramal, status ativo, foto de perfil, role) e `Role` (perfil de acesso).

**Segurança aplicada:** senha em **BCrypt**; CPF em **HMAC-SHA256** (nunca em texto claro); validação de unicidade de e-mail/CPF; usuário `ADMIN` não pode ser criado pela API.

| Método | Rota | Ação |
|--------|------|------|
| `POST` | `/users` | Cria usuário *(público)* |
| `GET` | `/users` | Lista todos |
| `GET` | `/users/userId/{userId}` | Busca por ID |
| `GET` | `/users/userName/{userName}` | Busca por nome |
| `PUT` | `/users/userId/{userId}` | Atualiza usuário |
| `DELETE` | `/users/userId/{userId}` | Inativa usuário |
| `PATCH` | `/users/userId/{id}` | Atualiza foto de perfil (upload) |
| `GET` | `/users/me` | Dados do usuário logado |
| `POST` | `/users/me/change-password` | Altera a senha do usuário logado |

**Roles** — `/role`: `POST`, `GET`, `GET /RoleId/{id}`, `GET /RoleName/{name}`, `PUT /RoleId/{id}`, `DELETE /RoleId/{id}`.

---

### `auth` — Autenticação *(público)*

Login e recuperação de senha. O login aceita **e-mail OU CPF**; em caso de sucesso, o JWT é devolvido em um **cookie `jwt`** (`HttpOnly`, `Secure`, `SameSite=None`).

| Método | Rota | Ação |
|--------|------|------|
| `POST` | `/auth/login` | Autentica e emite o JWT |
| `POST` | `/auth/recovery` | Envia e-mail com token de recuperação |
| `POST` | `/auth/recovery/new?token=...` | Define nova senha a partir do token |

---

### `cr` — Centros de Responsabilidade, Filiais, Setores e Instrutores

Estrutura organizacional da empresa.

**Entidades:** `Cr` (nome, código, flag `master`, setor), `Branch` (filial), `Sector` (setor, agrupa CRs), `CrBranch` (vínculo CR ↔ Filial com responsáveis) e `CrInstructor` (instrutores de um CrBranch).

| Recurso | Rota base | Observações |
|---|---|---|
| CR | `/cr` | CRUD |
| Filial | `/branches` | CRUD |
| Setor | `/sector` | CRUD; listagem `simple` e `compound` (`GET /sector/simple`, `GET /sector/compound`) |
| Vínculo CR-Filial | `/cr-branches` | CRUD + filtros; `GET /cr-branches/branch/{branchId}`; atribuir/remover responsável (`PUT /{id}/responsible/{userId}`, `DELETE /{id}/responsible`) |
| Instrutores | `/cr-instructors` | CRUD — criação/edição/remoção exigem role **ADMIN** (`@PreAuthorize`) |

---

### `request` — Solicitações de Compra *(domínio central)*

Módulo principal. Uma solicitação é vinculada a um `CrBranch`, tem um `Status`, os usuários que a criaram, itens de produto e/ou de serviço, feedback e anexos.

**Entidades:** `Request`, `Status`, `ItemRequestProduct`, `ItemRequestProvision`, `RequestAttachment`.

**Fluxo de status (padrão Strategy):**

```
   INICIAL
      │
  (EM ANÁLISE / UnderReview)
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

Cada estado é uma estratégia independente em `domain/strategy/` (`InicialStatusImpl`, `UnderReview`, `ApprovedStatusImpl`, `RecusedStatusImpl`, `InServiceStatusImpl`, `DeliveredStatusImpl`, `CancelledStatusImpl`), implementando `StatusIntrf`.

**Solicitações** — `/requests`:

| Método | Rota | Ação |
|--------|------|------|
| `POST` | `/requests` | Abre solicitação |
| `GET` | `/requests` | Lista todas (filtros: crCode, statusName, supervisorName, datas; paginado) |
| `GET` | `/requests/{id}` | Busca por ID |
| `PUT` | `/requests/{id}` | Atualiza |
| `DELETE` | `/requests/{id}` | Inativa |
| `PATCH` | `/requests/{id}` | Atualiza feedback |
| `PATCH` | `/requests/{id}/status` | Atualiza status |
| `POST` | `/requests/{id}/attachments` | Anexa arquivos (upload) |
| `GET` | `/requests/me` | Solicitações do usuário logado |
| `GET` | `/requests/me/{id}` | Solicitação própria por ID |
| `PUT` | `/requests/me/{id}` | Atualiza a própria solicitação |
| `DELETE` | `/requests/me/{id}` | Inativa a própria solicitação |

**Status** — `/status`: `POST`, `GET`, `GET /{id}`, `GET /statusName/{name}`, `PUT /{id}`, `DELETE /{id}`.

**Itens de produto** — `/item-request-products`: CRUD.

**Itens de serviço** — `/item-provision-requests`: `POST`; `GET /request/{requestId}`; `GET /request/{requestId}/{itemId}`; `PUT /request/{itemId}`; `DELETE /request/{itemId}`.

---

### `provision` — Serviços (Provisões)

Solicitação de **serviço** (mão de obra: manutenção, conserto, instalação), não de produto.

**Entidade:** `Provision` (nome, valor total, descrição).

**Endpoints:** `/provisions` — CRUD completo.

---

### `product` — Produtos e Unidades de Medida

**Entidades:** `Product` (nome, descrição, preço, tipo, código único) e `MeasurementUnit` (nome, abreviação).

| Recurso | Rota base | Observações |
|---|---|---|
| Produto | `/products` | CRUD; `GET /products?name=` filtra por nome |
| Unidade de medida | `/measurement-unit` | Criar/listar/atualizar; `GET /measurement-unit/search?abbreviation=` busca por abreviação |

---

### `notification` — Notificações

Notifica os usuários sobre eventos das solicitações, com envio **assíncrono** de e-mail (templates HTML com logos inline).

**Entidade:** `Notification` (título, mensagem, visualizada, usuário, solicitação).

| Método | Rota | Ação |
|--------|------|------|
| `GET` | `/notifications/me` | Notificações do usuário logado (paginado) |
| `GET` | `/notifications/user/{userId}` | Notificações de um usuário |
| `GET` | `/notifications/user/{userId}/unviewed` | Não visualizadas de um usuário |
| `PATCH` | `/notifications/{id}/viewed` | Marca como visualizada |

---

### `report` — Relatórios *(não implementado)*

Módulo planejado. Atualmente existe apenas o esqueleto de pastas, **sem classes Java**. Ao implementar, siga o padrão dos demais módulos.

---

## 🔗 Shared — Código Compartilhado

```
shared/
├── exception/
│   ├── BusinessException.java      → Exceção base de negócio (carrega HttpStatus)
│   ├── GlobalExceptionHandler.java → @RestControllerAdvice — captura todas as exceções
│   ├── ApiError.java               → Formato padrão de resposta de erro
│   └── InvalidFileException.java   → Erro de validação de arquivo
├── audit/                          → Auditoria por AOP (@Auditable, AuditLogAspect, AuditLog) — endpoint GET /logs
├── cloudinary/CloudinaryService.java → Upload de imagens/anexos (máx. 10 MB)
├── email/                          → EmailSenderService + componentes de template HTML
├── security/CurrentUserService.java  → Obtém o usuário autenticado do SecurityContext
└── MessageDTO.java                 → DTO genérico para respostas de mensagem simples
```

**Por que `BusinessException` importa:** todas as exceções de domínio (ex.: `UserNotFoundException`, `RequestNotFoundException`) estendem `BusinessException`. O `GlobalExceptionHandler` captura qualquer `BusinessException` e retorna automaticamente o status HTTP correto definido na exceção, sem tratar cada tipo individualmente.

**Formato padrão de erro (`ApiError`):**
```json
{
  "timestamp": "2026-07-10T12:00:00",
  "status": 404,
  "message": "Usuário não encontrado com id: 42",
  "errors": null
}
```
O campo `errors` é um mapa `campo → mensagem`, preenchido apenas em falhas de validação (HTTP 400).

---

## 🔐 Segurança

- **Autenticação stateless por JWT** (HS256, via `jjwt`), transportado em cookie `jwt` (`HttpOnly`, `Secure`, `SameSite=None`).
- **Login por e-mail OU CPF** — o CPF informado é normalizado e hasheado (HMAC-SHA256) para comparação.
- **Senha:** hash **BCrypt**. **CPF:** hash **HMAC-SHA256** (chave em `app.security.cpf-secret`). Nenhum dos dois fica em texto claro no banco.
- `WebSecurityConfig`: CSRF desabilitado, sessão **STATELESS**, `@EnableMethodSecurity` (permite `@PreAuthorize` — usado, por exemplo, em `/cr-instructors`).
- **Endpoints públicos:** `POST /users` e `/auth/**`. Em profile `dev`, o Swagger também é liberado.
- **CORS:** libera `http://localhost:3000/3001/3002`, métodos `GET/POST/PUT/PATCH/DELETE`, com credenciais.

### Auditoria

Ações relevantes são registradas de forma transversal via **AOP**: anota-se o endpoint com `@Auditable(action = "...")` e o identificador do alvo com `@AuditParam("user"|"request")`. O `AuditLogAspect` (`@AfterReturning`) resolve o agente/alvo e persiste um `AuditLog`. A auditoria é **best-effort**: nunca quebra a operação de negócio. Consulta: `GET /logs` (com filtros).

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 4.0.6 | Framework base |
| **Spring Data JPA** | — | Abstração do banco via repositórios |
| **Spring Security** | — | Autenticação e autorização |
| **Spring Validation** | — | Validação de DTOs com anotações |
| **Spring Mail** | — | Envio de e-mail (SMTP) |
| **Spring AOP / AspectJ** | — | Auditoria transversal |
| **PostgreSQL** | — | Banco de dados de produção (Supabase) |
| **H2** | — | Banco em memória para testes |
| **Lombok** | — | Redução de boilerplate |
| **SpringDoc OpenAPI** | 2.8.6 | Documentação automática Swagger UI |
| **jjwt** | 0.11.5 | Geração e validação de tokens JWT |
| **Cloudinary** (http5) | 2.2.0 | Upload de imagens e anexos |
| **BCrypt** | — | Hash de senhas |
| **HMAC-SHA256** | — | Hash de CPF |

---

## ⚙️ Configuração

O projeto usa variáveis de ambiente definidas em `.env` (não versionado). O `application.properties` as lê via `spring.config.import=optional:file:.env[.properties]`.

```properties
# Banco de dados (Supabase / PostgreSQL)
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/postgres
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<senha>

# JWT
SECURITY_JWT_SECRET_KEY=<chave-min-32-bytes>
SECURITY_JWT_EXPIRATION_TIME=<horas>

# Hash do CPF
CPF_SECRET=<chave-secreta>

# Cloudinary (upload de imagens/anexos)
CLOUD_NAME=<...>
CLOUDINARY_API_KEY=<...>
CLOUDINARY_API_SECRET=<...>

# E-mail (SMTP Gmail)
MAIL_USERNAME=<...>
MAIL_PASSWORD=<...>

# Template da rota do frontend usada nos e-mails enviados aos solicitantes
REQUESTER_REQUEST_URL_TEMPLATE=https://sgc-front-nine.vercel.app/solicitacoes/{requestId}

# Template da rota do frontend usada nos e-mails enviados para a tela de gestão
COORDINATOR_REQUEST_URL_TEMPLATE=https://sgc-front-nine.vercel.app/solicitacoes/gestao/{requestId}

# Opcional
PORT=8080
```

Os dois templates devem conter `{requestId}`, que o backend substitui pelo ID real da solicitação. Os e-mails recebem a URL direta da solicitação; o frontend é responsável pelo login e pelo redirecionamento posterior. O backend não gera URLs de `/login` nem o parâmetro `returnTo`.

**Profiles:** `dev` (padrão — `ddl-auto=update`, Swagger ON), `prod` (`ddl-auto=validate`, Swagger OFF) e `test` (H2, `ddl-auto=create-drop`).

---

## 🚀 Como Rodar

### Pré-requisitos

- Java 21+
- Maven 3.9+ (ou o Maven Wrapper `./mvnw`)
- Banco PostgreSQL acessível (para testes, o H2 em memória é usado automaticamente)

### Rodando localmente

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd SGC-Back

# 2. Crie o arquivo .env com as variáveis (veja a seção Configuração)

# 3. Suba a aplicação
./mvnw spring-boot:run

# API disponível em: http://localhost:8080
# Swagger UI (dev) em: http://localhost:8080/swagger-ui.html
```

### Docker

```bash
docker build -t sgc-back .
docker run -p 8080:8080 --env-file .env sgc-back
```

O `Dockerfile` é multi-stage (build com Maven + Temurin 21, runtime `eclipse-temurin:21-jre`).

---

## 🧪 Testes

Os testes usam **H2 em memória** (profile `test`) — nenhum banco externo é necessário:

```bash
./mvnw test
```

**Testes unitários** — usam Mockito para isolar a lógica de um caso de uso, sem subir o contexto Spring. Nomeados `<CasoDeUso>ImplTest` (ex.: `CreateUserImplTest`).

**Testes de integração** — em `src/test/.../integration/`. Sobem o contexto completo com `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")` + `@Transactional`, usam **MockMvc** para simular requisições HTTP e segurança via `springSecurity()`/`@WithMockUser`.

> Como o MockMvc não reverte `@Transactional` (cada request HTTP tem sua própria transação), os testes de integração limpam os repositórios em `@BeforeEach`/`@AfterEach`, respeitando a ordem das FKs (filhos antes dos pais).

---

## 👥 Equipe

Desenvolvido por **MIDS 77** para a **Centroweg**.

Pacote base: `net.centroweg.gerenciamentocompras`
