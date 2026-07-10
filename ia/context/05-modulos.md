# 05 — Módulos

> Este arquivo cobre TODOS os módulos, inclusive os que o `README.md` não documenta
> (`auth`, `notification`, `report`, `shared/audit`).

## `user` — Usuários e Perfis
Gerencia usuários (`User`) e perfis de acesso (`Role`).
- Senha em **BCrypt**, CPF em **HMAC-SHA256** (nunca em texto claro).
- Valida unicidade de e-mail/CPF (`UniquenessValidator`) antes de persistir.
- Não permite criar usuário com role `ADMIN` via API (`RoleNotAllowedException`).
- Upload de foto de perfil via Cloudinary.
- Endpoints: `/users` (CRUD, `/me`, `/me/change-password`) e `/roles`.
- Expõe `UserPublicApi` para outros módulos.

## `auth` — Autenticação
Login e emissão de JWT; recuperação de senha.
- Login por **e-mail OU CPF** (`CustomUserDetailsService`); o token vai em **cookie `jwt`**.
- `SecurityFilter` (OncePerRequestFilter) valida o token a cada requisição.
- `UserPrincipal` implementa `UserDetails`. `JwtService` gera/valida (HS256).
- Endpoints: `/auth/**` (públicos). Ver `07-seguranca.md`.

## `cr` — Centros de Responsabilidade, Filiais, Setores, Instrutores
Estrutura organizacional. Entidades: `Cr`, `Branch`, `CrBranch`, `Sector`, `CrInstructor`.
- Filtros por `Specification` (`CrBranchSpecifications`).
- Endpoints: `/crs`, `/branches`, `/sectors`, `/cr-branches`, instrutores — CRUD.
- **Nota:** este módulo tem a subdivisão de service mais granular (um subpacote por recurso).

## `request` — Solicitações de Compra *(domínio central)*
Coração do sistema. Entidades: `Request`, `Status`, `ItemRequestProduct`,
`ItemRequestProvision`, `RequestAttachment`.
- **Fluxo de status via Strategy** (`domain/strategy/`): `InicialStatusImpl`, `ApprovedStatusImpl`,
  `RecusedStatusImpl`, `InServiceStatusImpl`, `DeliveredStatusImpl`, `CancelledStatusImpl`, `UnderReview`.
- Muitas regras de negócio (ver as exceções em `domain/exception`: já aprovada, não editável,
  justificativa de recusa obrigatória, etc.).
- Anexos enviados ao Cloudinary; itens de produto e de serviço.
- Tem `service/validator` para regras e `infrastructure/listener` para eventos.

## `provision` — Serviços (Provisões)
Solicitação de **serviço** (mão de obra), não de produto. Entidade `Provision`
(name, totalValue, description). Endpoints `/provisions` (CRUD). Expõe `ProvisionPublicApi`.

## `product` — Produtos e Unidades de Medida
Entidades `Product` (name, price, type, code único) e `MeasurementUnit` (name, abbreviation).
Endpoints `/products` e `/measurement-units` (CRUD + busca por abreviação).

## `notification` — Notificações
Notifica usuários sobre eventos das solicitações. Entidade `Notification`.
- Cria notificação, lista por usuário, lista não-vistas, marca como vista.
- `infrastructure/email/NotificationEmailService` envia e-mail; `infrastructure/listener`
  reage a eventos de aplicação (processamento assíncrono via `@EnableAsync`).
- Endpoints `/notifications`.

## `report` — Relatórios *(NÃO IMPLEMENTADO)*
Apenas o esqueleto de pastas existe (`domain/`, `infrastructure/`, `presentation/`, `service/`),
**sem nenhuma classe Java**. É um módulo planejado/scaffold. Ao implementar, siga o padrão dos demais.

## `shared/` — Código transversal
- `shared/exception/` — `BusinessException`, `GlobalExceptionHandler`, `ApiError`, `InvalidFileException`.
- `shared/audit/` — auditoria por AOP (`@Auditable`, `AuditLogAspect`, `AuditLog`, endpoints `/audit-logs`). Ver `07-seguranca.md`.
- `shared/cloudinary/` — `CloudinaryService` (upload, validação de tamanho/máx. 10 MB).
- `shared/email/` — `EmailSenderService` + componentes de template HTML (título, parágrafo, botão, footer, layout) e logos inline.
- `shared/security/` — `CurrentUserService` (obtém o usuário autenticado do `SecurityContext`).
- `shared/MessageDTO` — DTO genérico de mensagem simples.
- `shared/events/` — pasta vazia (reservada para eventos de aplicação).
