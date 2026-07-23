package net.centroweg.gerenciamentocompras.modules.provision.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.centroweg.gerenciamentocompras.modules.provision.domain.Provision;

/**
 * Interface extendendo o JPA para interação no banco de dados.
 * @see {@code JpaRepository<T,ID>}
 */
@Repository
public interface ProvisionRepository extends JpaRepository<Provision, Long> {

    /**
     * Busca a primeira provisão cujo nome corresponda ao informado, sem distinção
     * entre maiúsculas e minúsculas.
     *
     * <p>Aplica {@code LIMIT 1}, portanto nunca lança
     * {@code IncorrectResultSizeDataAccessException} mesmo que existam linhas com
     * variação apenas de caixa. Usado para reaproveitar a provisão existente ao
     * montar itens de serviço de uma solicitação, evitando duplicar o nome.</p>
     *
     * @param name nome a ser pesquisado; não deve ser {@code null}
     * @return um {@link Optional} com a provisão encontrada, ou vazio se não houver
     */
    Optional<Provision> findFirstByNameIgnoreCase(String name);

    /**
     * Busca uma provisão pelo nome, sem distinção entre maiúsculas e minúsculas.
     *
     * <p>Usada na criação (POST) para impedir cadastrar uma provisão com nome já
     * existente.</p>
     *
     * @param name nome a ser pesquisado; não deve ser {@code null}
     * @return um {@link Optional} com a provisão encontrada, ou vazio se não houver
     */
    Optional<Provision> findByNameIgnoreCase(String name);

    /**
     * Indica se existe outra provisão (id diferente do informado) com o mesmo nome,
     * sem distinção entre maiúsculas e minúsculas.
     *
     * <p>Usada na atualização para impedir duas provisões com nome duplicado sem
     * conflitar com o próprio registro sendo editado.</p>
     *
     * @param name nome a ser verificado; não deve ser {@code null}
     * @param id   id da provisão que deve ser ignorada na verificação
     * @return {@code true} se já existir outra provisão com esse nome
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
