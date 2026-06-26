package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

/**
 * Entidade que representa uma filial (branch) no sistema de gerenciamento de compras.
 *
 * <p>Uma branch é uma unidade organizacional que pode ser associada a um ou mais
 * Centros de Resultado (CR) por meio da entidade {@code CrBranch}.</p>
 *
 * @author Leandro
 */
@Setter
@Getter
@BatchSize(size = 30)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Branch {

    /**
     * Identificador único da branch, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da branch. Não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Construtor utilizado para criar uma nova branch informando apenas o nome.
     *
     * @param name nome da branch
     */
    public Branch(String name) {
        this.name = name;
    }
}
