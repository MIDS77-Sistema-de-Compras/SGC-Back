package net.centroweg.gerenciamentocompras.modules.cr.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String code;

    private Boolean master;

    public Cr(String name, String code, Boolean master) {
        this.name = name;
        this.code = code;
        this.master = master;
    }
}
