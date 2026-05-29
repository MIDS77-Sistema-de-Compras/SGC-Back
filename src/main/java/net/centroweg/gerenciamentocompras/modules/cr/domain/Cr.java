package net.centroweg.gerenciamentocompras.modules.cr.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um Centro de Resultado (CR).
 * <p>
 * Um CR é uma unidade organizacional que pode ser classificada como
 * master, agrupando filiais ({@link Branch}) por meio de {@link CrBranch}.
 * </p>
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cr {

    /** Identificador único gerado automaticamente pelo banco de dados. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Nome do Centro de Resultado. Não pode ser nulo. */
    @Column(nullable=false)
    private String name;

    /** Código identificador do Centro de Resultado. Não pode ser nulo. */
    @Column(nullable=false)
    private String code;

    /** Indica se este CR é o CR master da estrutura organizacional. */
    private boolean master;

    /**
     * Construtor utilizado para criação de novos CRs sem ID definido.
     *
     * @param name   nome do CR
     * @param code   código do CR
     * @param master {@code true} se for o CR master
     */
    public Cr(String name, String code, boolean master) {
        this.name = name;
        this.code = code;
        this.master = master;
    }
}
