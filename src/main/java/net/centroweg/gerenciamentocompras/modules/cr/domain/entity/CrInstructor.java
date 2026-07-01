package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import java.util.List;

import jakarta.persistence.*;
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
public class CrInstructor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany
    @JoinColumn(name="instructor_cr_branch_id")
    @NonNull
    private List<User> instructors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cr_branch")
    @NonNull
    private CrBranch crBranch;
}
