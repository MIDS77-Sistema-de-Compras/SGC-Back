package net.centroweg.gerenciamentocompras.modules.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    /**
     * Identificador único do usuário, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Nome do usuário, não pode ser nulo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * CPF do usuário, não pode ser nulo.
     */
    @Column(nullable = false)
    private String cpf;

    /**
     * Endereço de email institucional do usuário, não pode ser nulo.
     */
    @Column(nullable = false)
    private String email;

    /**
     * Senha de acesso do usuário, não pode ser nula.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Ramal para contato interno com o usuário, não pode ser nulo.
     */
    @Column(nullable = false)
    private String extensionNumber;

    /**
     * Indica se o usuário está ativo, não pode ser nulo.
     */
    @Column(nullable = false)
    private Boolean active;

    /**
     * Data e hora de criação do usuário, preenchida automaticamente e não pode ser alterada após a criação.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data e hora da última atualização do usuário, não pode ser nula.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Relacionamento com a entidade nível de acesso, vários usuários podem compartilhar o mesmo nível de acesso.
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * Foto de perfil do usuário.
     */
    @Column(nullable = true)
    private String profilePicture;

    /**
     * Construtor utilizado para criar um novo usuário, sem ID definido.
     * @param name nome completo do usuário.
     * @param cpf CPF do usuário.
     * @param email endereço de email do usuário.
     * @param password senha de acesso do usuário.
     * @param extensionNumber ramal para contato interno do usuário.
     * @param active atividade do usuário.
     */
    public User(String name, String cpf, String email, String password, String extensionNumber, Boolean active) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.extensionNumber = extensionNumber;
        this.active = active;
    }

    /**
     * Define a data de criação e a data de atualização do usuário antes da persistência inicial.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
