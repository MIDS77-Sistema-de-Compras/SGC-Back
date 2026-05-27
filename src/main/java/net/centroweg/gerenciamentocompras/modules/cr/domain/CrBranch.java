package net.centroweg.gerenciamentocompras.modules.cr.domain;

import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public CrBranch(Branch branch, Cr cr, User responsibleUser) {
        this.branch = branch;
        this.cr = cr;
        this.responsibleUser = responsibleUser;
    }
}
