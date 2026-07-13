# 08 — Como Rodar e Testar

## Pré-requisitos
- Java 21+
- Maven 3.9+ (ou o wrapper `./mvnw`)
- Um PostgreSQL acessível (produção/dev). Para testes, nada externo é preciso (usa H2).

## Variáveis de ambiente (`.env` na raiz, NÃO versionado)

O `application.properties` importa o `.env` automaticamente. Variáveis usadas:

```properties
# Banco (PostgreSQL / Supabase)
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

# Rota do frontend usada nos e-mails enviados aos solicitantes
REQUESTER_REQUEST_URL_TEMPLATE=http://localhost:3000/docente/solicitacoes/{requestId}

# Rota do frontend usada nos e-mails genéricos enviados aos responsáveis/coordenadores
COORDINATOR_REQUESTS_URL=http://localhost:3000/coordenador/solicitacoes

# Opcional
PORT=8080
```

> ⚠️ **Nunca** comite o `.env` nem exponha esses segredos em código, logs, PRs ou docs.

## Rodar localmente

```bash
./mvnw spring-boot:run
```
- API: `http://localhost:8080`
- Swagger UI (só em profile `dev`): `http://localhost:8080/swagger-ui.html`
- Profile ativo padrão: `dev` (definido em `application.properties`).

## Build / empacotar

```bash
./mvnw clean package          # gera o jar em target/
./mvnw clean package -DskipTests
```

## Docker

`Dockerfile` multi-stage (build com `maven:3.9.9-eclipse-temurin-21`, runtime `eclipse-temurin:21-jre`),
empacota com `-DskipTests` e expõe a porta 8080.

```bash
docker build -t sgc-back .
docker run -p 8080:8080 --env-file .env sgc-back
```

## Testes

```bash
./mvnw test
```

Dois tipos, ambos sem banco externo (H2, profile `test`):

- **Unitários** — Mockito, isolam a lógica de um caso de uso, sem subir o contexto Spring.
  Nome: `<CasoDeUso>ImplTest` (ex.: `CreateUserImplTest`). Ficam em
  `src/test/.../modules/<mod>/service/...` espelhando o código de produção.
- **Integração** — em `src/test/.../integration/`. Sobem o contexto completo com
  `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")` + `@Transactional`,
  usam **MockMvc** para simular HTTP e H2 em memória. Segurança simulada com
  `springSecurity()` / `@WithMockUser`. Há um teste de integração por recurso
  (`UserIntegrationTest`, `RequestStatusIntegrationTest`, `AuditLogIntegrationTest`, etc.).

### Detalhes importantes dos testes de integração
- MockMvc **não** reverte `@Transactional` (cada request HTTP tem sua própria transação);
  por isso os testes limpam os repositórios no `@BeforeEach`/`@AfterEach`, **na ordem filho→pai** (respeitando FKs).
- Segredos de teste (JWT, cpf-secret, cloudinary, mail) já estão fake em `application-test.properties`.
- Projeto usa `tools.jackson.databind.ObjectMapper` (Jackson 3 / Spring Boot 4).

## Ao concluir uma mudança
Rode, no mínimo:
```bash
./mvnw compile        # confirma que compila
./mvnw test           # confirma que os testes passam
```
E, para mudanças em endpoints, valide manualmente pelo Swagger (dev).
