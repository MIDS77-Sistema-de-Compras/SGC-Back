package net.centroweg.gerenciamentocompras.modules.request.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "status")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Status {

    public static final String INITIAL_STATUS_NAME = StatusName.AGUARDANDO_APROVACAO.getValue();

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    public Status(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
