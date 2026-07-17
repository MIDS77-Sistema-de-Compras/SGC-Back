package net.centroweg.gerenciamentocompras.modules.product.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa um produto(product) no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * Identificador único do produto, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do produto, não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Descrição do produto, não pode ser nula.
     */
    private String description;

    /**
     * Preço unitário do produto, não pode ser nulo.
     */
    @Column(nullable = false)
    private Double price;

    /**
     * Tipo ou categoria do produto, não pode ser nulo.
     */
    @Column(nullable = false)
    private String type;

    /**
     * Código identificador único do produto, não pode ser nulo.
     */
    @Column(unique = true, nullable = false)
    private String code;

}