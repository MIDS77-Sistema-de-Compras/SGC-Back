package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import net.centroweg.gerenciamentocompras.modules.request.domain.RequestStatusCategory;

/**
 * Versão enxuta de {@link RequestResponse} para a listagem de solicitações.
 *
 * <p>A tela de listagem mostra apenas o código do CR, a data, o status e a quantidade
 * de produtos (mais os nomes, usados na busca). Este DTO carrega só isso — sem itens de
 * serviço, anexos ou solicitantes —, evitando que a listagem materialize e serialize
 * coleções que ela não exibe. O código do CR já vem resolvido, dispensando o cliente de
 * buscar a lista de CR-filiais só para traduzir o id.</p>
 */
public record RequestListItemResponse(
        Long id,
        LocalDateTime requestDate,
        LocalDateTime updatedAt,
        String crCode,
        String statusName,
        RequestStatusCategory statusCategory,
        List<String> productNames
) {}
