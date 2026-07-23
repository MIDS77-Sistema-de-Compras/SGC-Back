package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
import org.hibernate.annotations.BatchSize;

/**
 * Entidade que representa uma situação(status) aplicável a requisições e seus itens no sistema de gerenciamento de compras.
 */
@BatchSize(size = 30)
@Entity
@Table(name = "status")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Status implements StatusIntrf {

    /**
     * Identificador único da situação, gerado automaticamente pelo banco de dados.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome da situação, não pode ser nulo e deve ser único.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Descrição da situação, não pode ser nula.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Relacionamento com a entidade item de requisição de serviço, uma situação pode estar associada a vários itens.
     */
    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestProvision> itemRequestProvisions = new ArrayList<>();

    /**
     * Construtor utilizado para criar uma nova situação, sem ID definido.
     * @param name nome da situação.
     * @param description descrição da situação.
     */
    public Status(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
