package net.centroweg.gerenciamentocompras.modules.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.centroweg.gerenciamentocompras.modules.user.domain.rolelevels.RoleLevels;
import java.util.List;

/**
 * Entidade que representa o nível de acesso(role) de um usuário no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements RoleLevels {

    /**
     * Identificador único do nível de acesso, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome do nível de acesso, não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Retorna o nome do nível de acesso.
     * @return nome do nível de acesso.
     */
    @Override
    public String getRole(){
        return this.name;
    }

    /**
     * Relacionamento com a entidade usuário, um nível de acesso pode pertencer a vários usuários.
     */
    @OneToMany(mappedBy = "role")
    private List<User> users;

    /**
     * Construtor utilizado para criar um novo nível de acesso, sem ID definido.
     * @param name nome do nível de acesso.
     */
    public Role(String name) {
        this.name = name;
    }
}
