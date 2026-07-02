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

@Entity
@Table(name="instructor_cr_branch")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter

/**
 * Enidade que víncula um usuário a uma CR-filial, no sistema de gerenciamento de compras.
 */
public class CrInstructor {

    /**
     * Identificador único da CR-instrutor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com usuário, vários usuários pertencem a um CR-filial.
     */
    @OneToMany
    @JoinColumn(name="instructor_cr_branch_id")
    @NonNull
    private List<User> instructors;

    /**
     * Relacionamento com CR-filial, uma CR-filial tem vários usuários.
     */
    @ManyToOne
    @JoinColumn(name="cr_branch")
    @NonNull
    private CrBranch crBranch;
}
