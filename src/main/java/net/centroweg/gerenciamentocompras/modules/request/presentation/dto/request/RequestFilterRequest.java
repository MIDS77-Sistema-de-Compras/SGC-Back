package net.centroweg.gerenciamentocompras.modules.request.presentation.dto.request;

import java.time.LocalDate;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.Request;

/**
 * DTO de entrada para filtragem de {@link Request}. Todos os campos são opcionais.
 * @param crCode trecho do código do CR a ser filtrado.
 * @param statusName nome do status a ser filtrado.
 * @param supervisorName trecho do nome do supervisor a ser filtrado.
 * @param startDate data inicial do intervalo de criação a ser filtrado.
 * @param endDate data final do intervalo de criação a ser filtrado.
 */
public record RequestFilterRequest(
        String crCode,
        String statusName,
        String supervisorName,
        LocalDate startDate,
        LocalDate endDate
) {
}

