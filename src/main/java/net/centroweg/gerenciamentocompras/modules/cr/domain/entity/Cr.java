package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.Sector;
import org.hibernate.annotations.BatchSize;

/**
 * Entidade que representa um Centro de Responsabilidade (CR).
 * <p>
 * Um CR é uma unidade organizacional que pode ser classificada como
 * master, agrupando filiais ({@link Branch}) por meio de {@link CrBranch}.
 * </p>
 */
@BatchSize(size = 30)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cr {

    /** Identificador único gerado automaticamente pelo banco de dados. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do Centro de Responsabilidade. Não pode ser nulo. */
    @Column(nullable=false)
    private String name;

    /** Código identificador do Centro de Responsabilidade. Não pode ser nulo. */
    @Column(nullable=false)
    private String code;

    /** Indica se este CR é o CR master da estrutura organizacional. */
    private Boolean master;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sector sector;

    /**
     * Construtor utilizado para criação de novos CRs sem ID definido.
     *
     * @param name   nome do CR
     * @param code   código do CR
     * @param master {@code true} se for o CR master
     */
    public Cr(String name, String code, Boolean master) {

        this.name = name;
        this.code = code;
        this.master = master;
    }

    public Cr(String name, String code, Boolean master, Sector sector) {
        this.name = name;
        this.code = code;
        this.master = master;
        this.sector = sector;
    }
}
