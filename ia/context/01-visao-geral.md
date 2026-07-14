# 01 — Visão Geral

## O que é

O **SGC (Sistema de Gerenciamento de Compras)** é o backend do sistema corporativo de
**solicitação e controle de compras** do **Centroweg**. Expõe uma **API REST** consumida
pelo frontend web da empresa.

Pacote base: `net.centroweg.gerenciamentocompras`
Time: **MIDS 77** (autores vistos no código: Maria Eduarda Zabel, Hugo Paim, André, Leandro, Gabriel Fagundes, Lucas Schlei).

## Domínio em uma frase

Um colaborador abre uma **solicitação (Request)** de compra de **produtos** e/ou de
**serviços (provisions)**, vinculada ao **Centro de Responsabilidade + Filial (CrBranch)**
onde trabalha. A solicitação percorre um **fluxo de status** (inicial → aprovado/recusado →
em atendimento → entregue, podendo ser cancelada) até ser concluída. O sistema **notifica**
os envolvidos por e-mail/notificação e registra tudo em **auditoria**.

## Glossário (termos do negócio → código)

| Termo | Significado | Onde no código |
|---|---|---|
| **Request** (Solicitação) | Pedido de compra aberto por colaborador(es) | `modules/request` |
| **Provision** (Provisão) | Solicitação de **serviço** (mão de obra: manutenção, conserto, instalação), não de produto | `modules/provision` |
| **Product** / MeasurementUnit | Produto e sua unidade de medida (kg, L, m…) | `modules/product` |
| **ItemRequestProduct** | Item de produto dentro de uma solicitação | `modules/request` |
| **ItemRequestProvision** | Item de serviço dentro de uma solicitação | `modules/request` |
| **CR** (Centro de Responsabilidade) | Unidade organizacional; pode ser `master`; pertence a um `Sector` | `modules/cr` |
| **Branch** (Filial) | Filial da empresa | `modules/cr` |
| **CrBranch** | Vínculo CR ↔ Filial, com usuários responsáveis | `modules/cr` |
| **Sector** (Setor) | Agrupa CRs | `modules/cr` |
| **CrInstructor** | Instrutores associados a um CrBranch | `modules/cr` |
| **Status** | Estado da solicitação no fluxo | `modules/request` |
| **Role** | Perfil de acesso do usuário (ex: COMPRADOR, GESTOR, ADMIN) | `modules/user` |
| **AuditLog** | Registro de quem fez qual ação | `shared/audit` |

## Quem usa

Colaboradores da Centroweg com diferentes **roles** (perfis). O acesso é autenticado por
**JWT** e algumas ações são restritas por perfil (ver `07-seguranca.md`).
