package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.cr.domain.Cr;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

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
