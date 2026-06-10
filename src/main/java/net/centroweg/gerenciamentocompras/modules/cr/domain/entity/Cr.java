package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

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

    @ManyToOne
    private Sector sector;

    public Cr(String name, String code, boolean master, Sector sector) {
        this.name = name;
        this.code = code;
        this.master = master;
        this.sector = sector;
    }
}
