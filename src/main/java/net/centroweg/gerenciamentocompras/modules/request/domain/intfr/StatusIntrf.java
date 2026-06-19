package net.centroweg.gerenciamentocompras.modules.request.domain.intfr;

/**
 * Interface responsável por definir os métodos
 * de acesso às informações de um status.
 *
 * <p>Fornece os dados básicos de identificação
 * e descrição do status.</p>
 *
 * @author André
 * @since 1.0
 */
public interface StatusIntrf {
    /**
     * Retorna o nome do status.
     *
     * @return nome do status
     */
    String getName();
    /**
     * Retorna a descrição do status.
     *
     * @return descrição do status
     */
    String getDescription();
}
