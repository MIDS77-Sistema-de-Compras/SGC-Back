package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.provision.domain.entity.Provision;

/**
 * Entidade que representa um item de serviço(item request provision) dentro de uma requisição de compra.
 */
@Entity
@Table(name = "item_request_service")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemRequestProvision {

    /**
     * Identificador único do item, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com a entidade solicitação, vários itens pertencem a uma solicitação.
     */
    @ManyToOne
    @NonNull
    private Request request;

    /**
     * Relacionamento com a entidade serviço, vários itens podem referenciar o mesmo serviço.
     */
    @ManyToOne
    @NonNull
    private Provision provision;

    /**
     * Relacionamento com a entidade situação, vários itens podem compartilhar a mesma situação.
     */
    @ManyToOne
    @NonNull
    private Status status;

    /**
     * Informações adicionais sobre o item.
     */
    private String additionalInformation;

    /**
     * Construtor utilizado para criar um novo item de requisição de serviço, sem ID definido.
     * @param request solicitação à qual o item pertence.
     * @param provision serviço referente ao item.
     * @param status situação atual do item.
     * @param additionalInformation informações adicionais sobre o item.
     */
    public ItemRequestProvision(@NonNull Request request, @NonNull Provision provision, @NonNull Status status, String additionalInformation) {
        this.request = request;
        this.provision = provision;
        this.status = status;
        this.additionalInformation = additionalInformation;
    }
}
