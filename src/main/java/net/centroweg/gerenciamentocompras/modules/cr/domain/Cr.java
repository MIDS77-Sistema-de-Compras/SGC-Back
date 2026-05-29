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
    private long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String code;

    private boolean master;

    public Cr(String name, String code, boolean master) {
        this.name = name;
        this.code = code;
        this.master = master;
    }
}
