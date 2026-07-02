package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

/**
 * Entidade que representa um Centro de Responsabilidade(CR) no sistema de gerenciamento de compras.
 */
@BatchSize(size = 30)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cr {

    /**
     * Identificador único de um CR, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do CR, não pode ser nulo.
     */
    @Column(nullable=false)
    private String name;

    /**
     * Código identificador do CR, não pode ser nulo.
     */
    @Column(nullable=false)
    private String code;

    /**
     * Indica se é o CR master da estrutura organizacional.
     */
    private Boolean master;

    /**
     * Relacionamento com a entidade bloco(sector), um bloco pode ter vários CRs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Sector bloco;

    /**
     * Construtor utilizado para criação de novos CRs, sem ID e relacionamento definido.
     * @param name nome do CR.
     * @param code código do CR.
     * @param master booleano que indica se ele é um CR master(true) ou não(false).
     */
    public Cr(String name, String code, Boolean master) {
        this.name = name;
        this.code = code;
        this.master = master;
    }

    /**
     * Construtor utilizado para criação de novos CRs, sem ID definido.
     * @param name nome do CR.
     * @param code código do CR.
     * @param master booleano que indica se ele é um CR master(true) ou não(false).
     * @param bloco relacionamenro com a classe bloco.
     */
    public Cr(String name, String code, Boolean master, Sector bloco) {
        this.name = name;
        this.code = code;
        this.master = master;
        this.bloco = bloco;
    }
}
