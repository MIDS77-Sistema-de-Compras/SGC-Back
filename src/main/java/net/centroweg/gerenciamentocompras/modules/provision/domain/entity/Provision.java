package net.centroweg.gerenciamentocompras.modules.provision.domain.entity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.entity.ItemRequestProvision;

/**
 * Entidade que representa o módulo serviço, mapeada diretamente com o banco de dados, no sistema de gerenciamento de compras.
 */
@Entity
@Table(name="provision")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Provision {

    /**
     * Identificador único do serviço, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do serviço, não pode ser nulo.
     */
    @NonNull
    private String name;

    /**
     * Valor total do serviço, não pode ser nulo.
     */
    @NonNull
    private Double totalValue;

    /**
     * Descrição do serviço, não pode ser nulo.
     */
    @NonNull
    private String description;

    /**
     * Relacionamento com a entidade item de requisição de serviço, um serviço pode ter vários itens.
     */
    @OneToMany(mappedBy = "provision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestProvision> itemRequestProvisions = new ArrayList<>();

}
