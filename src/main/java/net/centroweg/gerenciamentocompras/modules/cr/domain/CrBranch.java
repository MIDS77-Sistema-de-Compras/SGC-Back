package net.centroweg.gerenciamentocompras.modules.cr.domain;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa o vínculo entre um Centro de Responsabilidade (CR) e uma filial (Branch).
 */

@Entity
@Table(name = "cr_branch")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "cr_id")
    private Cr cr;

    @ManyToOne
    @JoinColumn(name = "responsible_user_id", nullable = true)
    private User responsibleUser;

    /**
     * Cria um vínculo entre CR e Filial, opcionamlente com um responsável
     *
     * @param branch filial associada
     * @param cr CR associado
     * @param responsibleUser usuário responsável (pode ser nulo)
     */
    public CrBranch(Branch branch, Cr cr, User responsibleUser) {
        this.branch = branch;
        this.cr = cr;
        this.responsibleUser = responsibleUser;
    }
}
