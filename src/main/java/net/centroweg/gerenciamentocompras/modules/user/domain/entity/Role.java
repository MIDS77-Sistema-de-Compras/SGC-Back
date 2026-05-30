package net.centroweg.gerenciamentocompras.modules.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.RoleLevels;

import java.util.List;

/** Representa o nível de acesso do usuário */

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements RoleLevels {

    /** Código único da role */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Nome da role */
    @Column(nullable = false)
    private String name;

    @Override
    public String getRole(){
        return this.name;
    }

    /** Relacionamento JPA - usuário que possuem a role */
    @OneToMany(mappedBy = "role")
    private List<User> users;

    /** Método construtor */
    public Role(String name) {
        this.name = name;
    }
}
