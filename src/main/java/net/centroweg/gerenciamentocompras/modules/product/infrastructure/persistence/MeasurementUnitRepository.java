package net.centroweg.gerenciamentocompras.modules.product.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import net.centroweg.gerenciamentocompras.modules.product.domain.MeasurementUnit;


/**
 * Repositório JPA para a entidade {@link MeasurementUnit}.
 *
 * <p>Estende {@link JpaRepository}, fornecendo operações CRUD padrão
 * e paginação para a entidade {@code MeasurementUnit}, além de
 * consultas personalizadas definidas nesta interface.</p>
 *
 * <p>Esta interface é gerenciada pelo Spring Data JPA, que gera
 * automaticamente a implementação em tempo de execução.</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 * @see MeasurementUnit
 * @see JpaRepository
 */
public interface MeasurementUnitRepository extends JpaRepository <MeasurementUnit, Long>{

    /**
     * Busca uma unidade de medida pela sua abreviação.
     *
     * <p>Realiza uma consulta na tabela {@code measurement_unit} filtrando
     * pelo campo {@code abbreviation}. A busca é sensível a maiúsculas
     * e minúsculas conforme a configuração do banco de dados.</p>
     *
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     * Optional<MeasurementUnit> unit = repository.findByAbbreviation("kg");
     * unit.ifPresent(u -> System.out.println(u.getName()));
     * }</pre>
     *
     * @param abbreviation a abreviação da unidade de medida a ser buscada;
     *                     não deve ser {@code null}
     * @return um {@link Optional} contendo a {@link MeasurementUnit} encontrada,
     *         ou {@link Optional#empty()} caso nenhuma corresponda à abreviação informada
     */

    Optional<MeasurementUnit> findByAbbreviation(String abbreviation);

    
}
