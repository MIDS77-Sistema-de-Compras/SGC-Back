package net.centroweg.gerenciamentocompras.shared.email.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Entidade que representa os dados padrão utilizados para o envio de um email.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DefaultEmail{

    /**
     * Assunto do email, não pode ser nulo.
     */
    @NonNull
    private String subject;

    /**
     * Endereço de email do destinatário, não pode ser nulo.
     */
    @NonNull
    private String sendTo;

}