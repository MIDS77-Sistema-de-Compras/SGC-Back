package net.centroweg.gerenciamentocompras.modules.product.domain.entity;

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
 * Entidade que representa uma unidade de medida(measurement unit) no sistema de gerenciamento de compras.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "measurement_unit")
public class MeasurementUnit {

    /**
     * Identificador único da unidade de medida, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da unidade de medida, não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Abreviação da unidade de medida, não pode ser nula.
     */
    @Column(nullable = false)
    private String abbreviation;

    /**
     * Construtor utilizado para criar uma nova unidade de medida, sem ID definido.
     * @param name nome da unidade de medida.
     * @param abbreviation abreviação da unidade de medida.
     */
    public MeasurementUnit(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    
}
