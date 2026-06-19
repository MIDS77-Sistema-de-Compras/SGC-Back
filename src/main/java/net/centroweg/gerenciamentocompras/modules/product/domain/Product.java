package net.centroweg.gerenciamentocompras.modules.product.domain;

import jakarta.persistence.*;
import lombok.*;



/**
 * Entidade que representa um produto no sistema de gerenciamento de compras.
 *
 * <p>Esta classe é mapeada para a tabela {@code product} no banco de dados
 * e contém as informações essenciais de um produto, como nome, descrição,
 * preço, tipo e código identificador único.</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
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
     * Identificador único do produto.
     *
     * <p>Gerado automaticamente pelo banco de dados com estratégia de auto-incremento.</p>
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do produto.
     *
     * <p>Campo obrigatório — não pode ser nulo.</p>
     *
     * <p>Exemplo: "Parafuso M8", "Cabo de Aço 10mm".</p>
     */

    @Column(nullable = false)
    private String name;
    /**
     * Descrição detalhada do produto.
     *
     * <p>Campo opcional — pode ser nulo.</p>
     *
     * <p>Utilize este campo para informações adicionais sobre o produto,
     * como especificações técnicas, material, aplicações, entre outros.</p>
     */
    private String description;

    /**
     * Preço unitário do produto.
     *
     * <p>Campo obrigatório — não pode ser nulo.</p>
     *
     * <p>O valor deve ser representado em reais (BRL).
     * Exemplo: {@code 29.90}.</p>
     */
    @Column(nullable = false)
    private Double price;
    /**
     * Tipo ou categoria do produto.
     *
     * <p>Campo obrigatório — não pode ser nulo.</p>
     *
     * <p>Utilizado para classificar o produto dentro do sistema.
     * Exemplo: "Matéria-Prima", "Insumo", "Produto Acabado".</p>
     */
    @Column(nullable = false)
    private String type;
    /**
     * Código identificador único do produto.
     *
     * <p>Campo obrigatório e único — não pode ser nulo nem duplicado no banco de dados.</p>
     *
     * <p>Geralmente corresponde ao SKU ou código interno do produto.
     * Exemplo: {@code "PRD-00123"}.</p>
     */
    @Column(unique = true, nullable = false)
    private String code;

}