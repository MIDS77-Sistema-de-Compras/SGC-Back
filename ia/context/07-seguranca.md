# 07 — Segurança e Auditoria

## Autenticação (JWT via cookie)

- Login em `/auth/**` (público). Aceita **e-mail OU CPF** como identificador
  (`CustomUserDetailsService.loadUserByUsername`): se o input for numérico, ele é limpo
  (`\D` removido) e **hasheado** (HMAC-SHA256) para bater com o CPF armazenado.
- Em caso de sucesso, `JwtService.generateToken` emite um JWT **HS256** com claims
  `role`, `nome`, `cpf`, subject = e-mail, issuer `sgs-api`, expiração configurável
  (`security.jwt.expiration-time`, em horas).
- O token trafega em um **cookie chamado `jwt`**.
- `SecurityFilter` (extends `OncePerRequestFilter`) lê o cookie a cada requisição, valida o
  token e popula o `SecurityContext` com um `UserPrincipal`. Token inválido → `InvalidTokenException`.

## Autorização

- `WebSecurityConfig`: sessão **STATELESS**, CSRF desabilitado, `@EnableMethodSecurity` (permite `@PreAuthorize` em métodos).
- Endpoints **públicos**: `POST /users` (cadastro) e `/auth/**`. Em profile `dev`, o Swagger também é liberado. **Todo o resto exige autenticação.**
- **Roles**: `Role.name` (ex.: `COMPRADOR`, `GESTOR`, `ADMIN`). `UserPrincipal.getAuthorities()`
  devolve a role como authority. Usuário **ADMIN não pode ser criado pela API** (`RoleNotAllowedException`).
- CORS: libera origens `http://localhost:3000/3001/3002`, métodos `GET/POST/PUT/PATCH/DELETE`,
  credenciais habilitadas, expõe header `Authorization`.

## Hashing de dados sensíveis

- **Senha:** `BCryptPasswordEncoder` (bean `PasswordEncoder`). Nunca em texto claro.
- **CPF:** `CpfHasher` faz **HMAC-SHA256** com a chave `app.security.cpf-secret` (do `.env`).
  O CPF é armazenado e comparado **sempre hasheado** — nunca em claro no banco.
- **Nunca logue nem retorne** senha ou CPF em claro. Ao buscar por CPF, hasheie o input antes.

## Cookies

- `app.cookies.secure` = `false` em dev, `true` em prod/base.

## Auditoria (AOP — `shared/audit`)

Registra quem fez qual ação de forma **transversal e best-effort**.

- Anote o endpoint com **`@Auditable(action = "NOME_DA_ACAO")`**.
  - `targetFromReturn = true` extrai o id do alvo a partir do objeto retornado (ex.: criação).
- Anote o parâmetro identificador com **`@AuditParam("user")`** ou **`@AuditParam("request")`**
  para o aspecto saber quem/o-que é o alvo.
- `AuditLogAspect` (`@AfterReturning`) resolve o agente (usuário logado) e o alvo via
  `AuditLogPublicApi` e salva um `AuditLog` (`userAgent`, `userTarget`, `typeAction`, `request`, `timestamp`).
- **Princípio importante:** auditoria **nunca pode quebrar a operação de negócio**. Se não há
  usuário resolvível ou o lookup falha, o aspecto simplesmente ignora (`safeLookup` engole a exceção).
- Consulta dos logs: endpoints `/audit-logs` (com filtro por `Specification`).

### Ao criar um endpoint de escrita
Adicione `@Auditable` com uma `action` clara em MAIÚSCULAS (ex.: `CRIAR_SOLICITACAO`,
`APROVAR_SOLICITACAO`) e marque o id do alvo com `@AuditParam(...)`. Siga `UserController` como referência.
