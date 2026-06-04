<div align="center">

 
# Sistema de Gerenciamento de Compras
 
**Backend corporativo para solicitações, aprovações e controle de compras**
 
[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=openjdk)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![H2](https://img.shields.io/badge/H2-Dev_DB-003DA5?style=flat-square)](https://h2database.com/)
[![MySQL](https://img.shields.io/badge/MySQL-Produção-4479A1?style=flat-square&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Lombok](https://img.shields.io/badge/Lombok-Boilerplate_Free-pink?style=flat-square)](https://projectlombok.org/)
 
<br/>
</div>

 
## 📖 Sobre o Projeto
 
O **SGC** é o backend de um sistema corporativo de **solicitação e controle de compras**, desenvolvido pela **Centroweg**. Ele centraliza todo o fluxo de aquisições — desde a solicitação feita por um colaborador até a aprovação do gestor e emissão do pedido ao fornecedor.
 
A API REST exposta pelo sistema é consumida pelo frontend web, por integrações com sistemas de ERP, ou diretamente via ferramentas como o Postman.
 

 
## 🏗️ Arquitetura
 
O projeto segue a arquitetura em **camadas (Layered Architecture)**, padrão consolidado em aplicações Spring Boot. Cada camada tem uma responsabilidade única e bem definida.
 
```
┌─────────────────────────────────────────────────────────────┐
│                       CLIENTE                               │
│          (Frontend Web, Mobile, Postman, ERP)               │
└─────────────────────┬───────────────────────────────────────┘
                      │  HTTP Requests (JSON)
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  🌐  CONTROLLER  (Camada de Apresentação)                   │
│  Recebe requisições HTTP, valida entrada, retorna respostas │
│  Usa DTOs — nunca expõe entidades diretamente               │
└─────────────────────┬───────────────────────────────────────┘
                      │  Chama métodos de serviço
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  ⚙️  SERVICE  (Camada de Negócio)                           │
│  Toda a lógica da aplicação mora aqui                       │
│  Valida regras, orquestra operações, lança exceções         │
└─────────────────────┬───────────────────────────────────────┘
                      │  Consulta e persiste dados
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  🗃️  REPOSITORY  (Camada de Persistência)                   │
│  Interfaces JPA que abstraem o banco de dados               │
│  Queries automáticas por convenção de nome                  │
└─────────────────────┬───────────────────────────────────────┘
                      │  SQL gerado pelo Hibernate
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  🗄️  BANCO DE DADOS                                         │
│  H2 (desenvolvimento/testes)  │  MySQL (produção)           │
└─────────────────────────────────────────────────────────────┘
```
 
---
 
## 📁 Estrutura de Pastas
 
```
SGC-Back/
│
├── 📄 pom.xml                              → Dependências e build Maven
├── 📄 .gitignore
│
└── src/
    ├── main/
    │   ├── java/net/centroweg/gerenciamentocompras/
    │   │   │
    │   │   ├── 🚀 GerenciamentocomprasApplication.java   ← Ponto de entrada
    │   │   │
    │   │   ├── 📁 config/                 ← Configurações globais do Spring
    │   │   │   ├── CorsConfig.java        → Libera origens do frontend
    │   │   │   ├── SecurityConfig.java    → Regras de autenticação
    │   │   │   └── BeanConfig.java        → Beans customizados
    │   │   │
    │   │   ├── 📁 controller/             ← Endpoints REST (porta de entrada HTTP)
    │   │   │   ├── CompraController.java
    │   │   │   └── FornecedorController.java
    │   │   │
    │   │   ├── 📁 service/                ← Regras de negócio
    │   │   │   ├── CompraService.java
    │   │   │   └── FornecedorService.java
    │   │   │
    │   │   ├── 📁 repository/             ← Acesso ao banco via JPA
    │   │   │   ├── CompraRepository.java
    │   │   │   └── FornecedorRepository.java
    │   │   │
    │   │   ├── 📁 model/                  ← Entidades e Enums
    │   │   │   ├── entity/
    │   │   │   │   ├── Compra.java        → Tabela `compras`
    │   │   │   │   └── Fornecedor.java    → Tabela `fornecedores`
    │   │   │   └── enums/
    │   │   │       └── StatusCompra.java  → Estados de uma compra
    │   │   │
    │   │   └── 📁 shared/                 ← Código reutilizável entre módulos
    │   │       ├── dto/                   → Objetos de transferência de dados
    │   │       │   ├── request/           → O que chega do cliente
    │   │       │   └── response/          → O que retorna ao cliente
    │   │       ├── exception/             → Erros customizados + handler global
    │   │       └── util/                  → Helpers e utilitários
    │   │
    │   └── resources/
    │       └── application.properties     ← Configurações da aplicação
    │
    └── test/
        └── java/net/centroweg/gerenciamentocompras/
            └── GerenciamentocomprasApplicationTests.java
```
 
---
 
## 📚 Camadas em Detalhe
 
### 🌐 Controller — Camada de Apresentação
 
> **Responsabilidade única:** receber requisições HTTP e devolver respostas.
 
Os controllers são o ponto de contato entre o mundo externo e a aplicação. Eles **não tomam decisões de negócio** — apenas recebem dados, validam o formato com `@Valid` e delegam para o `Service`.
 
```java
@RestController
@RequestMapping("/api/compras")
public class CompraController {
 
    @GetMapping          // GET /api/compras
    public List<CompraResponse> listar() { ... }
 
    @GetMapping("/{id}") // GET /api/compras/42
    public CompraResponse buscar(@PathVariable Long id) { ... }
 
    @PostMapping         // POST /api/compras
    public CompraResponse criar(@Valid @RequestBody CompraRequest dto) { ... }
 
    @PutMapping("/{id}") // PUT /api/compras/42
    public CompraResponse atualizar(@PathVariable Long id, @RequestBody CompraRequest dto) { ... }
 
    @DeleteMapping("/{id}") // DELETE /api/compras/42
    public void remover(@PathVariable Long id) { ... }
}
```
 
**Rotas disponíveis:**
 
| Método | Rota | Ação |
|--------|------|------|
| `GET` | `/api/compras` | Lista todas as compras |
| `GET` | `/api/compras/{id}` | Busca uma compra por ID |
| `GET` | `/api/compras/status/{status}` | Filtra por status |
| `POST` | `/api/compras` | Cria nova solicitação |
| `PUT` | `/api/compras/{id}` | Atualiza dados |
| `PATCH` | `/api/compras/{id}/aprovar` | Aprova a compra |
| `PATCH` | `/api/compras/{id}/reprovar` | Reprova a compra |
| `DELETE` | `/api/compras/{id}` | Remove a solicitação |
| `GET` | `/api/fornecedores` | Lista fornecedores |
| `POST` | `/api/fornecedores` | Cadastra fornecedor |
 
---
 
### ⚙️ Service — Camada de Negócio
 
> **Responsabilidade única:** onde vivem as regras da aplicação.
 
É a camada mais importante do sistema. Toda validação de fluxo, regra de aprovação, conversão de dados e coordenação entre componentes acontece aqui.
 
```java
@Service
public class CompraService {
 
    // Regra: só pode aprovar se status for PENDENTE
    public CompraResponse aprovar(Long id) {
        Compra compra = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada"));
 
        if (compra.getStatus() != StatusCompra.PENDENTE) {
            throw new BusinessException("Apenas compras PENDENTES podem ser aprovadas");
        }
 
        compra.setStatus(StatusCompra.APROVADA);
        return toResponse(repository.save(compra));
    }
}
```
 
**O que o Service faz:**
- ✅ Valida regras de negócio (quem pode aprovar, quais transições de status são válidas)
- ✅ Converte entidades ↔ DTOs (nunca expõe entidades para fora)
- ✅ Lança exceções customizadas com mensagens claras
- ✅ Orquestra chamadas a múltiplos repositories quando necessário
---
 
### 🗃️ Repository — Camada de Persistência
 
> **Responsabilidade única:** comunicar com o banco de dados.
 
Interfaces que estendem `JpaRepository`. O Spring Data JPA gera o SQL automaticamente a partir do nome dos métodos.
 
```java
public interface CompraRepository extends JpaRepository<Compra, Long> {
 
    // Spring gera: SELECT * FROM compras WHERE status = ?
    List<Compra> findByStatus(StatusCompra status);
 
    // Spring gera: SELECT * FROM compras WHERE fornecedor_id = ? AND status = ?
    List<Compra> findByFornecedorIdAndStatus(Long fornecedorId, StatusCompra status);
 
    // Query customizada quando necessário
    @Query("SELECT c FROM Compra c WHERE c.valor > :minimo ORDER BY c.dataSolicitacao DESC")
    List<Compra> findAltasCompras(@Param("minimo") BigDecimal minimo);
}
```
 
---
 
### 📦 Model — Entidades e Enums
 
> **Responsabilidade única:** representar o domínio da aplicação e o esquema do banco.
 
#### 🧾 `Compra` — Entidade principal
 
Representa uma **solicitação de compra** feita por um colaborador.
 
```java
@Entity
@Table(name = "compras")
@Data                    // Lombok: getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    @Column(nullable = false)
    private String descricao;           // O que está sendo comprado
 
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal valor;           // Valor total estimado
 
    @Enumerated(EnumType.STRING)
    private StatusCompra status;        // Estado atual no fluxo
 
    private LocalDate dataSolicitacao;  // Quando foi solicitado
    private String solicitante;         // Quem solicitou
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;      // Relacionamento N:1 com fornecedor
 
    @Column(columnDefinition = "TEXT")
    private String observacoes;         // Notas adicionais
}
```
 
---
 
#### 🏭 `Fornecedor` — Parceiro comercial
 
Representa uma **empresa fornecedora** cadastrada no sistema.
 
```java
@Entity
@Table(name = "fornecedores")
@Data
@Builder
public class Fornecedor {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank
    private String razaoSocial;         // Nome da empresa
 
    @CNPJ                              // Validação de CNPJ via Hibernate Validator
    @Column(unique = true)
    private String cnpj;
 
    @Email
    private String email;              // Contato principal
 
    private String telefone;
    private String endereco;
 
    @Column(nullable = false)
    private Boolean ativo = true;      // Soft delete: desativa em vez de deletar
}
```
 
---
 
#### 🔖 `StatusCompra` — Máquina de estados
 
Cada compra percorre um fluxo bem definido de estados:
 
```
                    ┌──────────────┐
                    │   PENDENTE   │  ← Estado inicial ao criar
                    └──────┬───────┘
              ┌────────────┴────────────┐
              ▼                         ▼
       ┌─────────────┐          ┌─────────────┐
       │  APROVADA   │          │  REPROVADA  │
       └──────┬──────┘          └─────────────┘
              ▼
       ┌─────────────────┐
       │  EM_ANDAMENTO   │  ← Pedido enviado ao fornecedor
       └──────┬──────────┘
              ▼
       ┌─────────────┐
       │  CONCLUÍDA  │  ← Item recebido e conferido
       └─────────────┘
 
       ┌─────────────┐
       │  CANCELADA  │  ← Cancelamento em qualquer etapa
       └─────────────┘
```
 

```
 
---
 
### 🔗 Shared — Código Compartilhado
 
> Utilitários, DTOs e tratamento de erros reutilizados por toda a aplicação.
 
#### `dto/` — Data Transfer Objects
 
Separam o **contrato da API** da estrutura interna das entidades. Protegem o banco de exposição acidental.
 
```java
// O que chega na requisição POST /api/compras
public record CompraRequest(
    @NotBlank String descricao,
    @NotNull @DecimalMin("0.01") BigDecimal valor,
    @NotNull Long fornecedorId,
    String observacoes
) {}
 
// O que retorna ao cliente
public record CompraResponse(
    Long id,
    String descricao,
    BigDecimal valor,
    StatusCompra status,
    LocalDate dataSolicitacao,
    String solicitante,
    String fornecedorNome
) {}
```
 
#### `exception/` — Tratamento de Erros
 
Exceções customizadas e um handler global que formata todos os erros de forma padronizada.
 
```java
// Exceções customizadas
public class ResourceNotFoundException extends RuntimeException { ... }
public class BusinessException extends RuntimeException { ... }
 
// Handler global — captura tudo e retorna JSON padronizado
@ControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
    }
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Retorna todos os campos inválidos com suas mensagens
        ...
    }
}
```
 
**Formato padrão de erro:**
```json
{
  "status": 404,
  "erro": "Compra não encontrada",
  "timestamp": "2024-06-04T10:30:00"
}
```
 
---
 
### ⚙️ Config — Configurações Globais
 
| Arquivo | O que faz |
|---|---|
| `CorsConfig.java` | Libera o frontend para chamar a API (evita erro de CORS no navegador) |
| `SecurityConfig.java` | Define quais rotas são públicas e quais precisam de autenticação |
| `BeanConfig.java` | Registra beans customizados como `ModelMapper` ou `ObjectMapper` |
 
---
 
## 🛠️ Stack Tecnológica
 
| Tecnologia | Versão | Por que foi escolhida |
|---|---|---|
| ☕ **Java** | 21 LTS | Versão com suporte de longo prazo, Records, Pattern Matching |
| 🌱 **Spring Boot** | 4.0.6 | Framework mais completo para APIs REST em Java |
| 🗄️ **Spring Data JPA** | — | Elimina SQL boilerplate, queries por convenção de nome |
| ✅ **Spring Validation** | — | Validação declarativa com anotações (`@NotBlank`, `@Email`) |
| 🌐 **Spring Web MVC** | — | Mapeamento de rotas REST com `@RestController` |
| 🔧 **Lombok** | — | Elimina getters/setters/builders repetitivos com anotações |
| 🧪 **H2 Database** | — | Banco em memória para rodar localmente sem instalar nada |
| 🐬 **MySQL** | — | Banco relacional robusto para o ambiente de produção |
| 🔁 **Spring DevTools** | — | Reinicialização automática ao salvar código |
 
---
 
## ⚙️ Configurações
 
### `application.properties`
 
```properties
# ── Aplicação ──────────────────────────────────────────────
spring.application.name=gerenciamentocompras
server.port=8080
 
# ── Banco de Dados (Desenvolvimento com H2) ────────────────
spring.datasource.url=jdbc:h2:mem:sgcdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
 
# ── H2 Console (acessar via browser) ──────────────────────
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
 
# ── JPA / Hibernate ────────────────────────────────────────
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
 
# ── Produção: substitua pelo bloco MySQL abaixo ────────────
# spring.datasource.url=jdbc:mysql://localhost:3306/sgc?useSSL=false
# spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# spring.datasource.username=${DB_USER}
# spring.datasource.password=${DB_PASS}
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
 
---
 
## 🚀 Como Rodar
 
### Pré-requisitos
 
- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/download.cgi) *(ou use o Maven Wrapper incluso)*
- MySQL *(apenas para produção)*
### Desenvolvimento local (H2 — sem instalar banco)
 
```bash
# 1. Clone o repositório
git clone https://github.com/centroweg/SGC-Back.git
cd SGC-Back
 
# 2. Compile e suba a aplicação
./mvnw spring-boot:run
 
# 3. API disponível em:
# http://localhost:8080/api
 
# 4. Console do H2 (banco visual no browser):
# http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:sgcdb | User: sa | Senha: (vazia)
```
 
### Produção (MySQL)
 
```bash
# Configure as variáveis de ambiente antes de rodar
export DB_URL=jdbc:mysql://seu-host:3306/sgc
export DB_USER=seu_usuario
export DB_PASS=sua_senha
 
# Empacote e rode
./mvnw clean package -DskipTests
java -jar target/gerenciamentocompras-0.0.1-SNAPSHOT.jar
```
 
### Docker *(opcional)*
 
```bash
# Build da imagem
docker build -t sgc-back .
 
# Subir com MySQL via Docker Compose
docker-compose up -d
```
 
---
 
## 🗄️ Banco de Dados
 
### Diagrama de Relacionamento
 
```
┌──────────────────────────────┐        ┌──────────────────────────────┐
│         COMPRAS              │        │        FORNECEDORES          │
├──────────────────────────────┤        ├──────────────────────────────┤
│ 🔑 id           BIGINT PK   │  N:1   │ 🔑 id           BIGINT PK   │
│    descricao    VARCHAR(500) │───────▶│    razao_social VARCHAR(255) │
│    valor        DECIMAL(10,2)│        │    cnpj         VARCHAR(18)  │
│    status       VARCHAR(20)  │        │    email        VARCHAR(255) │
│    data_solic.  DATE         │        │    telefone     VARCHAR(20)  │
│    solicitante  VARCHAR(255) │        │    endereco     TEXT         │
│    observacoes  TEXT         │        │    ativo        BOOLEAN      │
│ 🔗 fornecedor_id BIGINT FK  │        └──────────────────────────────┘
└──────────────────────────────┘
```
 
### Dados de exemplo para desenvolvimento
 
```sql
-- Fornecedores
INSERT INTO fornecedores (razao_social, cnpj, email, ativo)
VALUES ('Tech Supplies Ltda', '12.345.678/0001-90', 'contato@techsupplies.com', true),
       ('Office Solutions', '98.765.432/0001-10', 'vendas@officesolutions.com', true);
 
-- Compras
INSERT INTO compras (descricao, valor, status, data_solicitacao, solicitante, fornecedor_id)
VALUES ('Notebook Dell i7 16GB', 8500.00, 'PENDENTE', CURRENT_DATE, 'João Silva', 1),
       ('Cadeiras ergonômicas (10 un)', 15000.00, 'APROVADA', CURRENT_DATE, 'Maria Santos', 2);
```
 
---
 
## 🧪 Testes
 
```bash
# Rodar todos os testes (usa H2 em memória automaticamente)
./mvnw test
 
# Testes com relatório de cobertura
./mvnw test jacoco:report
# Relatório em: target/site/jacoco/index.html
```
 
Os testes utilizam **H2 em memória** — nenhum banco externo é necessário para rodar a suite de testes.
 
---
 
## 📋 Exemplo de Uso da API
 
### Criar uma solicitação de compra
 
```http
POST /api/compras
Content-Type: application/json
 
{
  "descricao": "Monitor 27 polegadas 4K",
  "valor": 3200.00,
  "fornecedorId": 1,
  "observacoes": "Para o departamento de design"
}
```
 
**Resposta:**
```json
{
  "id": 15,
  "descricao": "Monitor 27 polegadas 4K",
  "valor": 3200.00,
  "status": "PENDENTE",
  "dataSolicitacao": "2024-06-04",
  "solicitante": "Carlos Oliveira",
  "fornecedorNome": "Tech Supplies Ltda"
}
```
 
### Aprovar uma compra
 
```http
PATCH /api/compras/15/aprovar
```
 
**Resposta:**
```json
{
  "id": 15,
  "status": "APROVADA",
  ...
}
```
 
---
 
## 👥 Organização
 
Desenvolvido por **MIDS 77**
 
Pacote base: `net.centroweg.gerenciamentocompras`
 
---
 
> 💡 *Este projeto está em desenvolvimento ativo. A estrutura de pacotes e os endpoints podem evoluir conforme novas funcionalidades são implementadas.*
