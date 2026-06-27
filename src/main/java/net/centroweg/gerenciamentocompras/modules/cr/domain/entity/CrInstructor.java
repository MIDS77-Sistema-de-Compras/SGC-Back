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
 * Enidade que víncula um docente a uma CR-Branch.
 */
public class CrInstructor {

    /**
     * Identificador único da CR-Instructor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com Usuários, vários usuários pertencem a um CR-Branch.
     */
    @OneToMany
    @JoinColumn(name="instructor_cr_branch_id")
    @NonNull
    private List<User> instructors;

    /**
     * Relacionamento com CR-Branch, uma CR-Branch tem vários docentes.
     */
    @ManyToOne
    @JoinColumn(name="cr_branch")
    @NonNull
    private CrBranch crBranch;
}
