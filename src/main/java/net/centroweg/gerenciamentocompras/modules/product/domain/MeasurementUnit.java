package net.centroweg.gerenciamentocompras.modules.product.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa uma unidade de medida no sistema de gerenciamento de compras.
 *
 * <p>Esta classe é mapeada para a tabela {@code measurement_unit} no banco de dados
 * e é utilizada para categorizar produtos de acordo com sua unidade de medida
 * (ex: kg, litro, metro, unidade, etc.).</p>
 *
 * @author Lucas Schlei
 * @version 1.0
 * @since 1.0
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "measurement_unit")
public class MeasurementUnit {
    /**
     * Identificador único da unidade de medida.
     *
     * <p>Gerado automaticamente pelo banco de dados com estratégia de auto-incremento.</p>
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo da unidade de medida.
     *
     * <p>Exemplo: "Quilograma", "Litro", "Metro".</p>
     * <p>Campo obrigatório — não pode ser nulo.</p>
     */

    @Column(nullable = false)
    private String name;

    /**
     * Abreviação da unidade de medida.
     *
     * <p>Exemplo: "kg", "L", "m".</p>
     * <p>Campo obrigatório — não pode ser nulo.</p>
     */

    @Column(nullable = false)
    private String abbreviation;

    /**
     * Construtor para criação de uma unidade de medida sem ID definido.
     *
     * <p>Utilizado principalmente ao persistir uma nova entidade,
     * onde o {@code id} será gerado automaticamente pelo banco de dados.</p>
     *
     * @param name         nome completo da unidade de medida; não deve ser {@code null}
     * @param abbreviation abreviação da unidade de medida; não deve ser {@code null}
     */

    public MeasurementUnit(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    
}
