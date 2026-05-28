package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.cr.domain.CrBranch;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "cr_branch_id", nullable = false)
    private CrBranch crBranch;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "request_users",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> createdByUsers = new ArrayList<>();

    public Request(CrBranch crBranch, Status status) {
        this.crBranch = crBranch;
        this.status = status;
    }
}
