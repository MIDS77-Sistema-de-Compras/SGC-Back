package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * Entidade que representa o bloco(sector) no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "sector")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sector {

    /**
     * Identificador único do bloco, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome do bloco.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Relacionamento com a entidade CR, um bloco possui várias CRs.
     */
    @OneToMany(mappedBy = "bloco")
    private List<Cr> crs;

    /**
     * Construtor utilizado para crir um novo bloco, sem ID e relacionamento definido.
     * @param name nome do bloco.
     */
    public Sector(String name) {
        this.name = name;
    }
}
