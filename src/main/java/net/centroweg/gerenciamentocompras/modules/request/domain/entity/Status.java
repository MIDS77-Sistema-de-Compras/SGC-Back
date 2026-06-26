package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.request.domain.intfr.StatusIntrf;
import org.hibernate.annotations.BatchSize;



@BatchSize(size = 30)
@Entity
@Table(name = "status")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Status implements StatusIntrf {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRequestProvision> itemRequestProvisions = new ArrayList<>();

    public Status(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
