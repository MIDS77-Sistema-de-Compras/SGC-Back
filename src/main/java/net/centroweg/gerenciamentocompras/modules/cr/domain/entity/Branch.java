package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

/**
 * Entidade que representa uma filial(branch) no sistema de gerenciamento de compras.
 */
@Setter
@Getter
@BatchSize(size = 30)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Branch {

    /**
     * Identificador único da filial, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da filial, não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Construtor utilizado para criar uma nova filial informando apenas o nome.
     * @param name nome da filial.
     */
    public Branch(String name) {
        this.name = name;
    }
}
