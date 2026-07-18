package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

/**
 * Entidade que víncula um usuário a um CR-filial, no sistema de gerenciamento de compras.
 */
@Entity
@Table(name="instructor_cr_branch")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CrInstructor {

    /**
     * Identificador único do CR-instrutor, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com usuário, um CR-instrutor tem vários usuários.
     */
    @OneToMany
    @JoinColumn(name="instructor_cr_branch_id")
    @NonNull
    private List<User> instructors;

    /**
     * Relacionamento com CR-filial, vários CR-instrutores pertencem a um CR-filial.
     */
    @ManyToOne
    @JoinColumn(name="cr_branch")
    @NonNull
    private CrBranch crBranch;
}
