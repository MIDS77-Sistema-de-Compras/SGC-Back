package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.hibernate.annotations.BatchSize;
import java.util.List;

/**
 * Entidade que representa o vínculo entre um Centro de Responsabilidade(CR) e uma filial(branch) no sistema de gerenciamento de compras.
 */

@BatchSize(size = 30)
@Entity
@Table(name = "cr_branch")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CrBranch {

    /**
     * Identificador único da CR-filial, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com a entidade filial, uma filial pode ter vários CR-filiais.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    /**
     * Relacionamento com a entidade CR, uma CR pode ter vários CR-filiais.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cr_id")
    private Cr cr;

    /**
     * Relacionamento com a entidade usuário(user), vários usuários podem ter vários CR-filiais.
     */
    @BatchSize(size = 30)
    @ManyToMany
    @JoinTable(
            name = "cr_branch_responsible_users",
            joinColumns = @JoinColumn(name = "cr_branch_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> responsibleUsers;

    /**
     * Construtor utilizado para criar um novo CR-filial, sem ID definido.
     * @param branch filial associada.
     * @param cr CR associado.
     * @param responsibleUsers usuário/os responsável/is, podendo ser nulo também.
     */
    public CrBranch(Branch branch, Cr cr, List<User> responsibleUsers) {
        this.branch = branch;
        this.cr = cr;
        this.responsibleUsers = responsibleUsers;
    }
}
