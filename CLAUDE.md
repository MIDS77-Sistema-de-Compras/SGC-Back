# SGC-Back — Guia para IA

Este arquivo é o ponto de entrada de contexto pro assistente de IA. Ele importa os
documentos temáticos em `ia/context/`. **Leia todos antes de mexer no código.**

O SGC (Sistema de Gerenciamento de Compras) é o backend corporativo de solicitação
e controle de compras da **Centroweg**. API REST em Spring Boot 4 / Java 21,
arquitetura de **monólito modular**, consumida por um frontend web.

## Contexto do projeto (arquivos importados)

- @ia/context/01-visao-geral.md — o que é o sistema, domínio e glossário
- @ia/context/02-arquitetura.md — monólito modular, camadas e fluxo de uma requisição
- @ia/context/03-tecnologias.md — stack, dependências e configs globais
- @ia/context/04-banco-de-dados.md — entidades, relacionamentos e persistência
- @ia/context/05-modulos.md — o que cada módulo faz
- @ia/context/06-convencoes.md — padrões de código OBRIGATÓRIOS ao escrever código aqui
- @ia/context/07-seguranca.md — autenticação JWT, hashing e auditoria
- @ia/context/08-como-rodar-e-testar.md — build, execução, variáveis de ambiente e testes

## Regras rápidas (resumo — detalhes nos arquivos acima)

1. **Siga a arquitetura em camadas** de cada módulo (`domain → infrastructure → service → presentation`). Nunca pule camadas.
2. **Um caso de uso = uma classe** (`@Service`). Veja `06-convencoes.md`.
3. **DTOs são `record`**; entidade nunca é exposta direto — sempre passa por DTO + mapper.
4. **Erros de negócio** = subclasse de `BusinessException` com `HttpStatus`. Nunca monte erro na mão no controller.
5. **Comentários, JavaDoc e mensagens de erro em português.**
6. **Módulos se comunicam via `service/api/*PublicApi`** — nunca acesse o repositório de outro módulo direto.
7. Antes de criar arquivo, **olhe uma classe análoga já existente no mesmo módulo e imite** (a nomenclatura de subpastas varia entre módulos — veja `06-convencoes.md`).

> ⚠️ O `README.md` da raiz está **desatualizado** (não cobre os módulos `auth`, `notification`, `report` nem `shared/audit`). Em caso de divergência, **o código e estes arquivos de contexto valem mais que o README.**
