package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.MeasurementUnit;
import net.centroweg.gerenciamentocompras.modules.product.domain.entity.Product;

/**
 * Entidade que representa um item de produto(item request product) dentro de uma requisição de compra.
 */
@Table(name = "itemRequestProduct")
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestProduct {

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
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    /**
     * Relacionamento com a entidade produto, vários itens podem referenciar o mesmo produto.
     */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Relacionamento com a entidade unidade de medida, vários itens podem compartilhar a mesma unidade de medida.
     */
    @ManyToOne
    @JoinColumn(name = "measurement_unit_id", nullable = false)
    private MeasurementUnit measurementUnit;

    /**
     * Quantidade solicitada do produto, não pode ser nula.
     */
    private Double quantity;

    /**
     * Relacionamento com a entidade status, vários itens podem compartilhar o mesmo status.
     */
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status statusId;

    /**
     * Informações adicionais sobre o item.
     */
    @Column(nullable = true)
    private String additionalInformations;


}
