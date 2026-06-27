package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entidade que representa o Sector(Bloco).
 */
@Entity
@Table(name = "sector")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sector {

    /**
     * Identificador único do Sector, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome do Sector.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Relacionamento com a entidade CR, várias CRs pertencem a um Setor.
     */
    @OneToMany(mappedBy = "sector")
    private List<Cr> crs;

    /**
     * Construtor utilizado para crir um novo Sector, sem ID e relacionamento definido.
     * @param name nome do sector.
     */
    public Sector(String name) {
        this.name = name;
    }
}
