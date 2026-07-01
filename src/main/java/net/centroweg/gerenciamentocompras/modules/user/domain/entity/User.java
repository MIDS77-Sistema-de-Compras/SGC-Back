package net.centroweg.gerenciamentocompras.modules.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Classe que representa os usuários da aplicação
 * @author Maria Eduarda Zabel
 */

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    /**
     * Identificador único do usuário, vai ser gerado automaticamente pelo banco de dados.
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
     * Senha de acesso do usuário, não pode ser nulo.
     */

    @Column(nullable = false)
    private String password;

    /**
     * Ramal para contato inteno com o usuário, não pode ser nulo.
     */

    @Column(nullable = false)
    private String extensionNumber;

    /**
     * Atividade do usuário, se continua trabalhando na empresa, não pode ser nulo.
     */

    @Column(nullable = false)
    private Boolean active;

    /**
     * Horário de criação do usuário, não pode ser nulo e nem editado.
     */

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Horário que será atribuído quando o usuário for atualizado.
     */

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Integração de muitos para um com a permissão, pois temos muitos usuários para apenas uma permissão.
     *
     * @see Role
     */
    // EAGER: role precisa estar disponível fora de sessão, em SecurityFilter/UserPrincipal.getAuthorities()
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = true)
    private String profilePicture;

    /**
     * Cria um novo usuário com os dados obrigatórios.
     * @param name nome completo do usuário
     * @param cpf cpf do usuário
     * @param email endereço de email do usuário
     * @param password senha de acesso do usuário
     * @param extensionNumber ramal para contato interno do usuário
     * @param active atividade do usuário
     */
    public User(String name, String cpf, String email, String password, String extensionNumber, Boolean active) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.extensionNumber = extensionNumber;
        this.active = active;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
