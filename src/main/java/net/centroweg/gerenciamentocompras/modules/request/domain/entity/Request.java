package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.cr.domain.entity.CrBranch;
import net.centroweg.gerenciamentocompras.modules.user.domain.entity.User;
import org.hibernate.annotations.BatchSize;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma solicitação(request) de compra no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Request {

    /**
     * Identificador único da requisição, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Data e hora de criação da requisição, preenchida automaticamente e não pode ser alterada após a criação.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate;

    /**
     * Relacionamento com a entidade filial, várias requisições podem pertencer à mesma filial.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private CrBranch crBranch;

    /**
     * Relacionamento com a entidade status, várias requisições podem compartilhar o mesmo status.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;

    /**
     * Data e hora da última atualização da requisição, não pode ser nula.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Indica se a requisição está ativa, não pode ser nulo.
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * Relacionamento com a entidade usuário, vários usuários podem ter criado a mesma requisição.
     */
    @BatchSize(size = 30)
    @ManyToMany
    @JoinTable(
            name = "request_users",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> createdByUsers = new ArrayList<>();

    /**
     * Retorno ou observação registrada sobre a requisição.
     */
    private String feedback;

    /**
     * Relacionamento com a entidade item de requisição de serviço, uma requisição pode ter vários itens.
     */
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestProvision> itemRequestProvisions = new ArrayList<>();

    /**
     * Relacionamento com a entidade item de requisição de produto, uma requisição pode ter vários itens.
     */
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestProduct> itemRequestProducts = new ArrayList<>();

    /**
     * Relacionamento com a entidade anexo, uma requisição pode ter vários anexos.
     */
    @BatchSize(size = 30)
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestAttachment> attachments = new ArrayList<>();

    /**
     * Construtor utilizado para criar uma nova requisição, sem ID definido.
     * @param crBranch filial à qual a requisição pertence.
     * @param status situação inicial da requisição.
     */
    public Request(CrBranch crBranch, Status status) {
        this.crBranch = crBranch;
        this.status = status;
    }

    /**
     * Define a data de criação e a data de atualização da requisição antes da persistência inicial.
     */
    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
        if (this.requestDate == null) {
            this.requestDate = LocalDateTime.now();
        }
    }

    /**
     * Atualiza a data de atualização da requisição antes de qualquer atualização no banco de dados.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
