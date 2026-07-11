# 03 — Tecnologias

## Stack principal

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 21 | Linguagem |
| **Spring Boot** | 4.0.6 | Framework base (parent do `pom.xml`) |
| **Maven** | 3.9.x | Build (há Maven Wrapper `mvnw`) |
| **Spring Data JPA** | — | Persistência via repositórios |
| **Spring Security** | — | Autenticação/autorização (`@EnableWebSecurity`, `@EnableMethodSecurity`) |
| **Spring Validation** | — | Bean Validation nos DTOs |
| **Spring Mail** | — | Envio de e-mail (SMTP Gmail) |
| **Lombok** | — | Getters/setters/construtores (`@Getter`, `@RequiredArgsConstructor`, etc.) |
| **SpringDoc OpenAPI** | 2.8.6 | Swagger UI (`/swagger-ui.html`) |
| **jjwt** | 0.11.5 | Geração/validação de JWT |
| **Cloudinary** (http5) | 2.2.0 | Upload de imagens/anexos |
| **PostgreSQL** | — | Banco de **produção** (Supabase) |
| **H2** | — | Banco em memória, usado nos **testes** |
| **AspectJ / Spring AOP** | — | Auditoria transversal |

> Observação: o `pom.xml` também declara `mysql-connector-j`, mas o datasource configurado
> é PostgreSQL (`org.postgresql.Driver`). Trate PostgreSQL como o banco oficial.

## Configurações globais (`config/`)

| Classe | Papel |
|---|---|
| `config/security/WebSecurityConfig` | `SecurityFilterChain`, CORS, CSRF off, sessão STATELESS, `PasswordEncoder` (BCrypt), `AuthenticationManager` |
| `config/security/CpfHasher` | Bean que faz hash **HMAC-SHA256** do CPF (chave em `app.security.cpf-secret`) |
| `config/CloudinaryConfig` | Bean `Cloudinary` a partir das credenciais |
| `config/MailConfig` | Bean `JavaMailSender` (SMTP Gmail, STARTTLS) |
| `config/OpenApiConfig` | Metadados do Swagger |
| `config/AsyncConfig` | `@EnableAsync` (processamento assíncrono, ex.: e-mails/notificações) |

## Onde ficam as configurações (`application*.properties`)

- `application.properties` — base; ativa profile `dev`; lê variáveis do `.env` via `spring.config.import=optional:file:.env[.properties]`.
- `application-dev.properties` — `ddl-auto=update`, `show-sql=true`, Swagger ON, cookies não-secure.
- `application-prod.properties` — `ddl-auto=validate`, `show-sql=false`, Swagger OFF, cookies secure.
- `src/test/resources/application-test.properties` — H2 em memória, `ddl-auto=create-drop`, segredos fake.

As variáveis de ambiente necessárias estão listadas em `08-como-rodar-e-testar.md`.
