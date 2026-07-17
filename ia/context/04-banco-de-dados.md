# 04 — Banco de Dados

## Bancos

- **Produção:** PostgreSQL (Supabase). Driver `org.postgresql.Driver`.
- **Testes:** H2 em memória (`jdbc:h2:mem:sgc_test`), profile `test`.
- **Schema:** gerado pelo Hibernate. `ddl-auto` = `update` (dev), `validate` (prod), `create-drop` (test).
  Não há migrations (Flyway/Liquibase) — o schema vem das entidades JPA.

## Entidades e tabelas

| Entidade | Tabela | Módulo | Observações |
|---|---|---|---|
| `User` | `users` | user | name, cpf (hash), email, password (BCrypt), extensionNumber, active, createdAt/updatedAt, profilePicture, role |
| `Role` | `role` | user | name (ex: COMPRADOR, GESTOR, ADMIN); implementa `RoleLevels` |
| `Cr` | (default `cr`) | cr | name, code, master (bool), sector |
| `Branch` | (default `branch`) | cr | name |
| `CrBranch` | `cr_branch` | cr | vínculo Cr ↔ Branch + `responsibleUsers` (N:N) |
| `Sector` | `sector` | cr | name único; agrupa CRs |
| `CrInstructor` | `instructor_cr_branch` | cr | instructors (N) + crBranch |
| `Request` | `requests` | request | requestDate, crBranch, status, active, feedback, createdByUsers (N:N), itens e anexos |
| `Status` | `status` | request | name único, description; implementa `StatusIntrf` |
| `ItemRequestProduct` | (default) | request | request + product + measurementUnit + quantity + status |
| `ItemRequestProvision` | `item_request_service` | request | request + provision + status |
| `RequestAttachment` | `request_attachments` | request | metadados do anexo no Cloudinary (url, publicId, size…) |
| `Provision` | `provision` | provision | name, totalValue, description |
| `Product` | `product` | product | name, description, price, type, code (único) |
| `MeasurementUnit` | `measurement_unit` | product | name, abbreviation |
| `Notification` | `notification` | notification | title, message, viewed, createdAt, user, request |
| `Delivery` | `delivery` | delivery | expectedDeliveryAt, deliveredAt, deliveryLocation, description, proofUrl, active, createdAt/updatedAt, request, status, itens de produto/serviço |
| `DeliveryReceiver` | `delivery_receiver` | delivery | PK composta (`delivery_id` + `user_id`), confirmed, confirmedAt, observation |
| `AuditLog` | `audit_log` | shared/audit | userAgent, userTarget, typeAction, request, timestamp |

## Relacionamentos-chave

- `User` **N:1** `Role` (fetch **EAGER** — a role é usada no `SecurityFilter`/`UserPrincipal`).
- `Request` **N:1** `CrBranch`, **N:1** `Status`; **N:N** `User` (`request_users`); **1:N** itens (produto/serviço) e anexos (cascade ALL, orphanRemoval).
- `CrBranch` **N:1** `Branch`, **N:1** `Cr`, **N:N** `User` (`cr_branch_responsible_users`).
- `Cr` **N:1** `Sector`.
- `ItemRequestProduct` liga `Request` + `Product` + `MeasurementUnit` + `Status`.
- `ItemRequestProvision` liga `Request` + `Provision` + `Status`.
- `Delivery` **N:1** `Request`, **N:1** `Status`; **1:N** `DeliveryReceiver` (cascade ALL, orphanRemoval);
  **N:N** `ItemRequestProduct` (join `delivery_item_request_product`) e **N:N** `ItemRequestProvision`
  (join `delivery_item_request_service`).
- `DeliveryReceiver` **N:1** `Delivery`, **N:1** `User`; PK composta (`DeliveryReceiverId`, `@Embeddable`)
  com `unique(delivery_id, user_id)`.
- `AuditLog` **N:1** `User` (agente e alvo) e `Request` (com `OnDelete CASCADE`).

## Convenções de persistência

- IDs: `@GeneratedValue` — a maioria `IDENTITY`; algumas entidades usam `AUTO` (User, Role, Status, Sector).
- Relações "para muitos" são **LAZY** por padrão (exceto `User.role` que é EAGER por necessidade de segurança).
- **N+1:** várias entidades usam `@BatchSize(size = 30)` para mitigar; mantenha isso ao criar coleções grandes.
- **Timestamps:** via `@PrePersist`/`@PreUpdate` (ex.: `createdAt`, `updatedAt`) ou `@CreationTimestamp` (AuditLog).
- **Filtros dinâmicos:** use `Specification` (pasta `infrastructure/persistence/specification`) — já existe para `CrBranch`, `Request` e `AuditLog`.
- Repositórios são interfaces `JpaRepository`/`JpaSpecificationExecutor` em `infrastructure/persistence`.
