package net.centroweg.gerenciamentocompras.modules.request.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Entidade que representa um anexo de uma requisição de compra no sistema de gerenciamento de compras.
 */
@Entity
@Table(name = "request_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAttachment {

    /**
     * Identificador único do anexo, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome original do arquivo enviado, não pode ser nulo.
     */
    @Column(name = "original_name", nullable = false)
    private String originalName;

    /**
     * URL de acesso ao arquivo armazenado, não pode ser nula.
     */
    @Column(nullable = false, length = 1000)
    private String url;

    /**
     * Identificador público do arquivo no serviço de armazenamento, não pode ser nulo.
     */
    @Column(name = "public_id", nullable = false)
    private String publicId;

    /**
     * Tipo do recurso armazenado (ex.: imagem, documento).
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * Tipo de conteúdo (MIME type) do arquivo.
     */
    @Column(name = "content_type")
    private String contentType;

    /**
     * Tamanho do arquivo em bytes, não pode ser nulo.
     */
    @Column(nullable = false)
    private Long size;

    /**
     * Data e hora em que o arquivo foi enviado, preenchida automaticamente e não pode ser alterada após a criação.
     */
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    /**
     * Relacionamento com a entidade solicitação, vários anexos pertencem à mesma solicitação.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    /**
     * Define a data de upload do anexo antes da persistência inicial.
     */
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}