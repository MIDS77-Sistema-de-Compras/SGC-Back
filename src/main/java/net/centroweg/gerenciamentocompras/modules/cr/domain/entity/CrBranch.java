package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.util.List;

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

    @ManyToMany
    @JoinTable(
            name = "cr_branch_responsible_users",
            joinColumns = @JoinColumn(name = "cr_branch_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> responsibleUsers;

    /**
     * Cria um vínculo entre CR e Filial, opcionamlente com um responsável
     *
     * @param branch filial associada
     * @param cr CR associado
     * @param responsibleUsers usuário/usuários responsável (pode ser nulo)
     */
    public CrBranch(Branch branch, Cr cr, List<User> responsibleUsers) {
        this.branch = branch;
        this.cr = cr;
        this.responsibleUsers = responsibleUsers;
    }
}
