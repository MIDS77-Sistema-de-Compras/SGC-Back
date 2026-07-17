# 06 — Convenções de Código (OBRIGATÓRIO ler antes de escrever código)

> Regra número 1: **imite o código que já existe no módulo em que você está mexendo.**
> Não introduza estilos novos.

## 1. Um caso de uso = uma classe

Cada operação é sua própria classe `@Service` (ex.: `CreateUserImpl`, `DeleteUserImpl`,
`FindUserByIdImpl`). Existe uma **interface agregadora** (ex.: `UserIntrf`) e uma **fachada**
(ex.: `UserServiceImpl`) que injeta os casos de uso e delega. O **controller depende só da
interface agregadora**.

## 2. ⚠️ Nomenclatura de pastas varia POR MÓDULO

O esquema **canônico** (padrão a seguir) é `service/usecases/serviceImpl/` +
`service/usecases/serviceIntrf/`. A maioria dos módulos já segue isso; `cr` e `provision`
ainda destoam (dívida conhecida). Antes de criar um arquivo, **olhe o módulo alvo**:

| Módulo | Impl dos casos de uso | Interfaces |
|---|---|---|
| `user`, `request`, `product`, `notification`, `auth`, `shared/audit` | `service/usecases/serviceImpl/` | `service/usecases/serviceIntrf/` |
| `cr` *(destoa)* | `service/<recurso>service/<recurso>impl/` + `functionality/` | `service/<recurso>service/<recurso>interface/` |
| `provision` *(destoa)* | `service/` (classes soltas) + `service/interfaces/` | `service/interfaces/` |

> Os módulos layer-based já foram padronizados para `usecases/serviceImpl/serviceIntrf`.
> Faltam `cr` (organizado por recurso — reestruturação grande) e `provision` (classes soltas).
> **Não "conserte" esses dois por conta própria** — siga o padrão local; se for padronizar,
> faça como tarefa combinada e separada.

## 3. DTOs são `record`

- Entrada em `presentation/dto/request/`, saída em `presentation/dto/response/`.
- Acesso por método-acessor: `dto.name()`, `dto.email()`.
- Validação com Bean Validation (`@NotNull`, `@NotBlank`, `@Email`…); controller usa `@Valid`.
- **Nunca** exponha uma entidade JPA direto na resposta — sempre um DTO via mapper.

## 4. Mapper manual (`@Component`)

Métodos `toEntity(dto)`, `toDTO(entity)`, `toDTOList(List)`, conversão campo a campo.
**Não** usamos MapStruct.

## 5. Injeção de dependência

`@RequiredArgsConstructor` (Lombok) + campos `private final`. **Nunca** `@Autowired` em campo.

## 6. Exceções e erros

- Erros de negócio = classe em `domain/exception/` que **estende `BusinessException`**
  (que carrega um `HttpStatus`). Mensagens **em português**, específicas.
  Ex.: `throw new UserNotFoundException(id);`
- O `GlobalExceptionHandler` (`@RestControllerAdvice`) captura e devolve JSON padronizado.
  **Nunca** monte `ResponseEntity` de erro manualmente no controller/serviço.
- Formato de erro (`ApiError`, record):
  ```json
  { "timestamp": "...", "status": 404, "message": "Usuário não encontrado com id: 42", "errors": null }
  ```
  `errors` é um `Map<campo, mensagem>` preenchido só em falha de validação (400).

## 7. Controllers

- `@RestController` + `@RequestMapping("/rota")` + `@RequiredArgsConstructor`.
- Cada endpoint: `@Operation(description = "...")` (Swagger) e retorna `ResponseEntity<DTO>`.
- Operações de **escrita** (create/update/delete) levam `@Auditable(action = "ACAO_EM_MAIUSCULO")`;
  o id do alvo é anotado com `@AuditParam("user")` ou `@AuditParam("request")`. Ver `07-seguranca.md`.
- Listagens usam `Pageable` → `Page<DTO>`.

## 8. Comunicação entre módulos

Só via `service/api/*PublicApi`. Nunca importe o repositório de outro módulo.

## 9. Idioma e estilo

- **Português** em JavaDoc, comentários e mensagens de erro.
- JavaDoc nas classes e métodos públicos, no mesmo tom do código existente.
- Lombok para reduzir boilerplate (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`).

## 10. Ao criar um recurso novo, a ordem é

1. Entidade (`domain/entity`) + exceções (`domain/exception`).
2. Repositório (`infrastructure/persistence`).
3. DTOs request/response (records) em `presentation/dto`.
4. Caso(s) de uso (`service/usecases/...`) + registrar na interface agregadora e na fachada.
5. Mapper (`service/mapper`).
6. Controller (`presentation/controller`) com Swagger + auditoria.
7. Testes (unitário Mockito + integração MockMvc/H2) — ver `08-como-rodar-e-testar.md`.
8. Compilar e rodar os testes.

## Dívidas técnicas conhecidas (não replicar)

- Campos injetados **duplicados** em alguns casos de uso (ex.: `CreateUserImpl` injeta
  `userMapper`+`mapper` e `userRepository`+`repository`). Ao mexer, prefira **um** campo.
- **Imports duplicados** em alguns controllers (ex.: `UserController`). Não copie os duplicados.
- Nomenclatura de pastas de `service` inconsistente **apenas em `cr` e `provision`** (item 2);
  os demais módulos já seguem o padrão canônico `usecases/serviceImpl/serviceIntrf`.
