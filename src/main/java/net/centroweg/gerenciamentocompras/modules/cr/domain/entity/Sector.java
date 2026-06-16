package net.centroweg.gerenciamentocompras.modules.cr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "sector")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "sector")
    private List<Cr> crs;

    public Sector(String name) {
        this.name = name;
    }
}
