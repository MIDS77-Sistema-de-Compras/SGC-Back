package net.centroweg.gerenciamentocompras.modules.product.domain.exception;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private String type;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String variation;

}

