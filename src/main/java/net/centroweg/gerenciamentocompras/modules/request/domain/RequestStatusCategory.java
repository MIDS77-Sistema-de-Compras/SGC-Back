package net.centroweg.gerenciamentocompras.modules.request.domain;

/**
 * Categoria de alto nível de um status de {@code Request}, usada para que
 * clientes (ex.: o front-end) saibam classificar uma solicitação como
 * pendente ou concluída sem precisar interpretar o nome livre do status.
 *
 * <p>{@code Status} é uma entidade de nome livre (cadastrada via CRUD em
 * {@code /status}), então essa categoria é resolvida a partir do nome pelo
 * {@link net.centroweg.gerenciamentocompras.modules.request.service.mapper.request.RequestStatusCategoryResolver}.</p>
 *
 * @author André
 * @since 1.0
 */
public enum RequestStatusCategory {
    /**
     * A solicitação ainda não chegou a um resultado final
     * (aguardando aprovação, aprovada ou em atendimento).
     */
    PENDENTE,

    /**
     * A solicitação chegou a um resultado final (entregue, cancelada ou recusada).
     */
    CONCLUIDA
}
