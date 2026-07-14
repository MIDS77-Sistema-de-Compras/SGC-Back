# 02 — Arquitetura

## Estilo: Monólito Modular

Uma única aplicação Spring Boot, mas o código é organizado em **módulos de domínio
independentes**, cada um com suas próprias camadas internas. Coesão de negócio sem a
complexidade operacional de microsserviços.

```
src/main/java/net/centroweg/gerenciamentocompras/
├── GerenciamentocomprasApplication.java   ← classe main (@SpringBootApplication)
├── config/                                 ← configs globais (security, mail, cloudinary, swagger, async)
├── shared/                                 ← código transversal usado por vários módulos
└── modules/                                ← um pacote por domínio
    ├── user/  cr/  request/  provision/  product/  auth/  notification/  report/
```

## Camadas de um módulo

Ordem de dependência: `presentation → service → (domain, infrastructure)`. **Nunca inverta.**

```
modules/<modulo>/
├── domain/
│   ├── entity/       → entidades JPA (mapeadas pro banco)
│   ├── exception/    → exceções do domínio (estendem BusinessException)
│   └── strategy/ intfr/  → padrões de domínio (ex: Strategy de status no módulo request)
├── infrastructure/
│   ├── persistence/  → interfaces Spring Data JPA (repositórios) + Specifications
│   └── listener/     → listeners de eventos de aplicação (quando houver)
├── service/
│   ├── api/          → *PublicApi: fachada de comunicação ENTRE módulos
│   ├── mapper/       → conversão entidade ↔ DTO (@Component)
│   └── usecases/     → 1 classe por caso de uso + interface agregadora + fachada Impl
└── presentation/
    ├── controller/   → @RestController (fronteira HTTP)
    └── dto/
        ├── request/  → DTOs de entrada (records, com Bean Validation)
        └── response/ → DTOs de saída (records)
```

## Regra de ouro: comunicação entre módulos

Um módulo **NÃO** acessa o repositório/entidade interna de outro módulo diretamente.
A conversa passa por uma **Public API**: uma interface `*PublicApi` (+ `*PublicApiImpl`)
em `service/api/` que expõe só o necessário.

Exemplos reais:
- `auth` busca usuário via `AuthPublicApi.findByEmailOrCpf(...)`.
- O aspecto de auditoria (`shared/audit`) resolve usuários/solicitações via `AuditLogPublicApi`.
- `user` expõe `UserPublicApi`.

Ao precisar de dados de outro módulo, **procure/crie um método na Public API dele** — não importe o repositório alheio.

## Fluxo típico de uma requisição HTTP

```
Cliente (frontend)
   │  HTTP + cookie "jwt"
   ▼
SecurityFilter (valida JWT do cookie)  ── auth/filter
   ▼
@RestController (presentation/controller)   → recebe DTO request, @Valid
   ▼
Interface agregadora do serviço (ex: UserIntrf)
   ▼
Fachada *ServiceImpl → delega ao caso de uso específico (ex: CreateUserImpl)
   ▼
Mapper (DTO → entidade)  +  Repository (Spring Data JPA)  +  regras de negócio
   ▼
Mapper (entidade → DTO response)
   ▼
ResponseEntity<DTO>  → volta ao cliente
```

Erros de negócio sobem como `BusinessException` e são convertidos em JSON padronizado
pelo `GlobalExceptionHandler` (ver `06-convencoes.md`). Ações de escrita são registradas
por AOP via `@Auditable` (ver `07-seguranca.md`).

## Padrões de projeto usados

- **Strategy** — fluxo de status da `Request` (`request/domain/strategy/*`), cada estado é uma classe implementando `StatusIntrf`.
- **Mapper** — conversão manual entidade ↔ DTO por módulo.
- **Facade + caso-de-uso-por-classe** — cada operação isolada, agregada por uma interface/fachada.
- **Public API por módulo** — desacopla a comunicação entre módulos.
- **AOP** — auditoria transversal (`shared/audit/aspect`).
- **Specification** — filtros dinâmicos de consulta (`infrastructure/persistence/specification`).
