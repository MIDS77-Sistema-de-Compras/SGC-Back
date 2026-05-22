package net.centroweg.gerenciamentocompras.modules.cr.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String name;
    private long code;
    private boolean master;

    public Cr(String name, long code, boolean master) {
        this.name = name;
        this.code = code;
        this.master = master;
    }
}
